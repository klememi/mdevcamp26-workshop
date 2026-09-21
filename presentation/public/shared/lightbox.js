/* Lightweight image lightbox — only for screenshot images */
(function () {
  var overlay = document.createElement('div');
  overlay.className = 'lightbox-overlay';
  overlay.innerHTML = '<span class="lightbox-close">&times;</span><img>';
  document.body.appendChild(overlay);

  var lbImg = overlay.querySelector('img');

  function close() { overlay.classList.remove('active'); }
  overlay.addEventListener('click', close);
  document.addEventListener('keydown', function (e) {
    if (e.key === 'Escape') close();
  });

  // Only attach to images inside slide sections that reference screenshots
  document.querySelectorAll('section img, .card img, .diagram img').forEach(function (img) {
    var src = img.getAttribute('src') || '';
    // Only screenshots — local images in screenshots/ folder
    if (src.indexOf('screenshots/') === -1) return;

    img.classList.add('zoomable');
    img.addEventListener('click', function (e) {
      e.stopPropagation();
      lbImg.src = img.src;
      lbImg.alt = img.alt || '';
      overlay.classList.add('active');
    });
  });
})();
