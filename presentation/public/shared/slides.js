// Slide navigation system
(function() {
  const pages = [
    { label: 'Intro', href: 'index.html' },
    { label: 'Schedule', href: 'schedule.html' },
    { label: 'Session 1', href: 'session-1.html' },
    { label: 'Session 2', href: 'session-2.html' },
    { label: 'Session 3', href: 'session-3.html' },
    { label: 'Wrap-up', href: 'wrap-up.html' },
  ];

  const currentFile = window.location.pathname.split('/').pop() || 'index.html';
  const currentPageIndex = pages.findIndex(p => p.href === currentFile);

  // The scroll container is .container (has scroll-snap-type)
  function getScrollContainer() {
    return document.querySelector('.container');
  }

  function getSlides() {
    return document.querySelectorAll('.container > .hero, .container > section');
  }

  function getCurrentSlideIndex() {
    const slides = getSlides();
    let closest = 0;
    let closestDist = Infinity;
    slides.forEach((slide, i) => {
      const dist = Math.abs(slide.getBoundingClientRect().top);
      if (dist < closestDist) {
        closestDist = dist;
        closest = i;
      }
    });
    return closest;
  }

  function goToSlide(index) {
    const slides = getSlides();
    if (index >= 0 && index < slides.length) {
      slides[index].scrollIntoView({ behavior: 'smooth' });
    }
  }

  function goToPage(offset) {
    const newIndex = currentPageIndex + offset;
    if (newIndex >= 0 && newIndex < pages.length) {
      window.location.href = pages[newIndex].href;
    }
  }

  // --- Keyboard navigation ---
  document.addEventListener('keydown', function(e) {
    const tag = document.activeElement.tagName;
    if (tag === 'TEXTAREA' || tag === 'INPUT') return;

    const idx = getCurrentSlideIndex();
    const slides = getSlides();

    switch(e.key) {
      case 'ArrowDown':
      case 'PageDown':
        e.preventDefault();
        if (idx < slides.length - 1) {
          goToSlide(idx + 1);
        } else {
          goToPage(1);
        }
        break;
      case 'ArrowUp':
      case 'PageUp':
        e.preventDefault();
        if (idx > 0) {
          goToSlide(idx - 1);
        } else {
          if (currentPageIndex > 0) {
            window.location.href = pages[currentPageIndex - 1].href + '#last';
          }
        }
        break;
      case 'ArrowRight':
        e.preventDefault();
        goToPage(1);
        break;
      case 'ArrowLeft':
        e.preventDefault();
        goToPage(-1);
        break;
    }
  });

  // Assign numeric IDs to all slides: #slide-1, #slide-2, etc.
  function ensureSlideIds() {
    getSlides().forEach((slide, i) => {
      slide.id = 'slide-' + (i + 1);
    });
  }

  // Restore slide position from URL hash on page load
  function restoreFromHash() {
    const hash = window.location.hash;

    // #last — scroll to last slide (used when navigating back from next page)
    if (hash === '#last') {
      requestAnimationFrame(() => {
        const slides = getSlides();
        if (slides.length > 0) {
          const sc = getScrollContainer();
          if (sc) sc.style.scrollBehavior = 'auto';
          slides[slides.length - 1].scrollIntoView();
          if (sc) sc.style.scrollBehavior = 'smooth';
          history.replaceState(null, '', window.location.pathname + '#slide-' + slides.length);
        }
      });
      return;
    }

    // #slide-N — scroll to specific slide number
    const match = hash.match(/^#slide-(\d+)$/);
    if (match) {
      const slideNum = parseInt(match[1], 10);
      requestAnimationFrame(() => {
        const slides = getSlides();
        const idx = slideNum - 1;
        if (idx >= 0 && idx < slides.length) {
          const sc = getScrollContainer();
          if (sc) sc.style.scrollBehavior = 'auto';
          slides[idx].scrollIntoView();
          if (sc) sc.style.scrollBehavior = 'smooth';
        }
      });
      return;
    }

    // Legacy fallback: support old named hashes (e.g. #framing)
    if (hash && hash !== '#') {
      setTimeout(() => {
        const target = document.querySelector(hash);
        if (target) {
          const sc = getScrollContainer();
          if (sc) { sc.style.scrollBehavior = 'auto'; target.scrollIntoView(); sc.style.scrollBehavior = 'smooth'; }
          else { target.scrollIntoView(); }
        }
      }, 100);
    }
  }

  // --- Pagination nav (dots + inline labels on hover) ---
  function getSlideTitle(slide) {
    if (slide.dataset.title) return slide.dataset.title;
    const h2 = slide.querySelector('h2');
    if (h2) return h2.textContent.trim();
    const h1 = slide.querySelector('h1');
    if (h1) return h1.textContent.trim();
    return '';
  }

  const navContainer = document.createElement('div');
  navContainer.className = 'slide-nav';

  const counterEl = document.createElement('div');
  counterEl.className = 'slide-counter';
  document.body.appendChild(counterEl);

  function buildNav() {
    navContainer.innerHTML = '';
    const slides = getSlides();
    slides.forEach((slide, i) => {
      const item = document.createElement('div');
      item.className = 'slide-nav-item';
      item.addEventListener('click', () => goToSlide(i));

      const dot = document.createElement('div');
      dot.className = 'slide-nav-dot';

      const label = document.createElement('span');
      label.className = 'slide-nav-label';
      label.textContent = getSlideTitle(slide) || 'Slide ' + (i + 1);

      item.appendChild(dot);
      item.appendChild(label);
      navContainer.appendChild(item);
    });

    document.body.appendChild(navContainer);
    updateNav();
  }

  function updateNav() {
    const idx = getCurrentSlideIndex();
    const slides = getSlides();
    const items = navContainer.querySelectorAll('.slide-nav-item');
    items.forEach((item, i) => {
      item.classList.toggle('active', i === idx);
    });
    if (counterEl) {
      counterEl.textContent = (idx + 1) + ' / ' + items.length;
    }
    // Update URL hash to current slide number
    const newHash = '#slide-' + (idx + 1);
    if (window.location.hash !== newHash) {
      history.replaceState(null, '', newHash);
    }
  }

  // --- Key legend ---
  const legend = document.createElement('div');
  legend.className = 'nav-legend hidden';
  legend.innerHTML = '<span><kbd>&uarr;</kbd><kbd>&darr;</kbd> slides</span><span class="legend-sep"></span><span><kbd>&larr;</kbd><kbd>&rarr;</kbd> pages</span><span class="legend-sep"></span><span><kbd>T</kbd> timer</span><span class="legend-sep"></span><span><kbd>D</kbd> dev</span><span class="legend-sep"></span><span><kbd>H</kbd> hints</span>';
  document.body.appendChild(legend);

  document.addEventListener('keydown', function(e) {
    if (e.key === 'h' || e.key === 'H') {
      const tag = (e.target.tagName || '').toLowerCase();
      if (tag === 'input' || tag === 'textarea' || tag === 'select') return;
      e.preventDefault();
      legend.classList.toggle('hidden');
    }
  });

  // --- Update dots on scroll ---
  const sc = getScrollContainer();
  if (sc) {
    sc.addEventListener('scroll', function() {
      requestAnimationFrame(updateNav);
    }, { passive: true });
  }

  // --- Initialize ---
  ensureSlideIds();
  restoreFromHash();
  buildNav();
})();
