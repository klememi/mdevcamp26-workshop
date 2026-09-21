import puppeteer from 'puppeteer';
import { createServer } from 'http';
import { readFileSync, existsSync } from 'fs';
import { join, extname } from 'path';
import { fileURLToPath } from 'url';
import { dirname } from 'path';

const __dirname = dirname(fileURLToPath(import.meta.url));
const PUBLIC = join(__dirname, 'public');

const MIME = {
  '.html': 'text/html', '.css': 'text/css', '.js': 'application/javascript',
  '.png': 'image/png', '.jpg': 'image/jpeg', '.svg': 'image/svg+xml',
  '.otf': 'font/otf', '.ttf': 'font/ttf', '.woff': 'font/woff', '.woff2': 'font/woff2',
  '.json': 'application/json',
};

const pages = [
  'index.html',
  'schedule.html',
  'session-1.html',
  'session-2.html',
  'session-3.html',
  'wrap-up.html',
];

const outArg = process.argv.find(a => a.startsWith('--out='))?.slice(6);
const outFile = outArg || 'bridge-the-gap.pdf';

const server = createServer((req, res) => {
  let path = join(PUBLIC, decodeURIComponent(req.url.split('?')[0]));
  if (path.endsWith('/')) path += 'index.html';
  if (!existsSync(path)) { res.writeHead(404); res.end(); return; }
  const ext = extname(path);
  res.writeHead(200, { 'Content-Type': MIME[ext] || 'application/octet-stream' });
  res.end(readFileSync(path));
});

await new Promise(r => server.listen(0, '127.0.0.1', r));
const port = server.address().port;
const base = `http://127.0.0.1:${port}`;

console.log(`Server on ${base}, output: ${outFile}`);

const browser = await puppeteer.launch({ headless: true });
const page = await browser.newPage();
await page.setViewport({ width: 1200, height: 800, deviceScaleFactor: 2 });

let allSlides = [];

for (const pageFile of pages) {
  const url = `${base}/${pageFile}`;
  console.log(`Loading ${pageFile}...`);
  await page.goto(url, { waitUntil: 'networkidle0', timeout: 15000 });

  await page.evaluate(() => {
    const hide = 'nav, .slide-nav, .nav-legend, .slide-counter, .feedback-badge, .feedback-panel, .dev-badge, .dev-panel, .dev-marker, .feedback-pin, .lang-toggle, .workshop-timer, .quiz-btn';
    document.querySelectorAll(hide).forEach(el => el.style.display = 'none');
  });

  const slideCount = await page.evaluate(() => {
    return document.querySelectorAll('.container > .hero, .container > section').length;
  });

  console.log(`  ${slideCount} slides`);

  for (let i = 0; i < slideCount; i++) {
    await page.evaluate((idx) => {
      const slides = document.querySelectorAll('.container > .hero, .container > section');
      if (slides[idx]) slides[idx].scrollIntoView();
    }, i);
    await new Promise(r => setTimeout(r, 300));

    const clip = await page.evaluate((idx) => {
      const slides = document.querySelectorAll('.container > .hero, .container > section');
      const el = slides[idx];
      if (!el) return null;
      const rect = el.getBoundingClientRect();
      return { x: rect.x, y: rect.y, width: rect.width, height: rect.height };
    }, i);

    if (clip && clip.width > 0 && clip.height > 0) {
      const screenshot = await page.screenshot({ clip, type: 'png', encoding: 'base64' });
      allSlides.push({ width: clip.width, height: clip.height, data: screenshot });
    }
  }
}

console.log(`Generating PDF with ${allSlides.length} slides...`);

const pdfPage = await browser.newPage();
const slideWidth = allSlides[0]?.width || 960;
const slideHeight = allSlides[0]?.height || 680;

const html = `<!DOCTYPE html><html><head><style>
  * { margin: 0; padding: 0; }
  body { background: #002823; }
  .slide { page-break-after: always; width: ${slideWidth}px; height: ${slideHeight}px; overflow: hidden; }
  .slide:last-child { page-break-after: auto; }
  .slide img { width: 100%; height: 100%; object-fit: contain; }
</style></head><body>
${allSlides.map(s => `<div class="slide"><img src="data:image/png;base64,${s.data}"></div>`).join('\n')}
</body></html>`;

await pdfPage.setContent(html, { waitUntil: 'networkidle0' });
await pdfPage.pdf({
  path: outFile,
  width: `${slideWidth}px`,
  height: `${slideHeight}px`,
  printBackground: true,
  margin: { top: 0, right: 0, bottom: 0, left: 0 },
});

console.log(`Done! ${outFile} (${allSlides.length} slides)`);

await browser.close();
server.close();
