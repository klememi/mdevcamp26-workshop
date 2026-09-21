// Shared navigation — Bridge the Gap (KMP Workshop)
(function() {
  const pages = [
    { label: 'Intro', href: 'index.html' },
    { label: 'Schedule', href: 'schedule.html' },
    { divider: true },
    { label: 'Session 1', href: 'session-1.html' },
    { label: 'Session 2', href: 'session-2.html' },
    { label: 'Session 3', href: 'session-3.html' },
    { divider: true },
    { label: 'Wrap-up', href: 'wrap-up.html' },
  ];

  const currentFile = window.location.pathname.split('/').pop() || 'index.html';

  const primaryLinks = pages.map(p => {
    if (p.divider) return '<span class="nav-divider"></span>';
    const isActive = currentFile === p.href;
    return `<a href="${p.href}"${isActive ? ' class="active"' : ''}>${p.label}</a>`;
  }).join('');

  const nav = document.createElement('nav');
  nav.innerHTML = `
    <a href="index.html" class="nav-logo-link"><img class="nav-logo" src="livesport-presentations-claude-pack/logo-negative.png" alt="Livesport"></a>
    <span class="nav-logo-sep"></span>
    <img class="nav-logo nav-logo-mdc" src="images/mdevcamp.svg" alt="mDevCamp">
    <div class="nav-primary">${primaryLinks}</div>
  `;
  document.body.prepend(nav);
})();
