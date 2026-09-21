(function() {
  'use strict';
  var PAGE = window.location.pathname.split('/').pop() || 'index.html';
  var STORAGE_KEY = 'dev-notes';
  var devMode = false;
  var markerEls = [];
  var TARGET_SELECTORS = 'section, .card, .diagram, .stat-box, .stat-row, .stat-num, .stat-label, h1, h2, h3, p, li, td, th, .q, .fw-box, table, .hero, .person-card, .person-story, .person-saving, .person-name, .donut-wrap, .donut-legend-item, .arg-card, .risk-card, .mini-quote, .plan-card, img, svg, span.accent, div[style]';

  // --- Badge ---
  var badge = document.createElement('div');
  badge.className = 'dev-badge';
  badge.innerHTML = '<span style="margin-right:6px;">&#128736;</span> DEV MODE <span style="font-weight:400;opacity:0.7;margin-left:8px;">(D to exit)</span>';
  document.body.appendChild(badge);

  // --- Panel ---
  var panel = document.createElement('div');
  panel.className = 'dev-panel';
  document.body.appendChild(panel);

  function openPanel() { panel.classList.add('open'); }
  function closePanel() { panel.classList.remove('open'); }

  // --- Keyboard ---
  document.addEventListener('keydown', function(e) {
    if (e.key === 'd' || e.key === 'D') {
      var tag = (e.target.tagName || '').toLowerCase();
      if (tag === 'input' || tag === 'textarea' || tag === 'select') return;
      e.preventDefault();
      toggleDevMode();
    }
  });

  function toggleDevMode() {
    devMode = !devMode;
    document.body.classList.toggle('dev-mode', devMode);
    if (devMode) { markTargets(); renderMarkers(); }
    else { closePanel(); unmarkTargets(); clearMarkers(); }
  }

  // --- Storage ---
  function loadNotes() {
    try { return JSON.parse(localStorage.getItem(STORAGE_KEY) || '[]'); } catch(e) { return []; }
  }
  function saveNotes(notes) { localStorage.setItem(STORAGE_KEY, JSON.stringify(notes)); }

  // --- Target marking ---
  function markTargets() {
    document.querySelectorAll(TARGET_SELECTORS).forEach(function(el) {
      if (el.closest('.dev-panel') || el.closest('.dev-badge') || el.closest('nav') || el.closest('.feedback-panel') || el.closest('.feedback-badge')) return;
      el.setAttribute('data-dev-target', '1');
    });
    document.addEventListener('click', onTargetClick, true);
  }

  function unmarkTargets() {
    document.querySelectorAll('[data-dev-target]').forEach(function(el) {
      el.removeAttribute('data-dev-target');
    });
    document.removeEventListener('click', onTargetClick, true);
  }

  function onTargetClick(e) {
    if (!devMode) return;
    if (e.target.closest('.dev-panel') || e.target.closest('.dev-badge') || e.target.closest('.dev-marker') || e.target.closest('nav')) return;
    var el = e.target.closest('[data-dev-target]');
    if (!el) return;
    e.preventDefault(); e.stopPropagation();
    openNewNote(el);
  }

  // --- Element identification ---
  function getSlideInfo(el) {
    var sec = el.closest('section') || el.closest('.hero');
    var title = sec ? (sec.getAttribute('data-title') || '') : '';
    var slides = document.querySelectorAll('.container > .hero, .container > section');
    var index = -1;
    slides.forEach(function(s, i) { if (s === sec) index = i + 1; });
    return { title: title, index: index };
  }

  function getSelector(el) {
    // Tag each noted element with a unique data-dev-id for reliable re-selection
    var devId = el.getAttribute('data-dev-id');
    if (!devId) {
      devId = 'dev-' + Date.now() + '-' + Math.random().toString(36).substr(2, 5);
      el.setAttribute('data-dev-id', devId);
    }

    // Also build a human-readable CSS path for context
    var parts = [];
    var cur = el;
    for (var i = 0; i < 8 && cur && cur !== document.body; i++) {
      var tag = cur.tagName.toLowerCase();
      if (cur.id && !cur.id.startsWith('dev-')) { parts.unshift('#' + cur.id); break; }
      var cls = Array.from(cur.classList).filter(function(c) {
        return !c.startsWith('dev') && c !== 'dev-noted';
      }).slice(0, 2);
      if (cls.length) tag += '.' + cls.join('.');
      // nth-of-type for disambiguation
      if (cur.parentElement) {
        var siblings = Array.from(cur.parentElement.children).filter(function(c) {
          return c.tagName === cur.tagName;
        });
        if (siblings.length > 1) {
          var idx = siblings.indexOf(cur) + 1;
          tag += ':nth-of-type(' + idx + ')';
        }
      }
      parts.unshift(tag);
      cur = cur.parentElement;
    }
    return parts.join(' > ');
  }

  function getTextContent(el) {
    return (el.textContent || '').trim().replace(/\s+/g, ' ').substring(0, 120);
  }

  // --- New note panel ---
  function openNewNote(el) {
    var slide = getSlideInfo(el);
    var selector = getSelector(el);
    var textContent = getTextContent(el);

    panel.innerHTML =
      '<div class="dev-panel-header">' +
        '<h3>New Dev Note</h3>' +
        '<button class="dev-panel-close" id="dev-close">&times;</button>' +
      '</div>' +
      '<div class="dev-panel-body">' +
        '<div class="dev-target-info">' +
          '<span class="dev-ti-page">' + PAGE + '</span>' +
          '<span class="dev-ti-slide">Slide ' + slide.index + ': ' + escapeHtml(slide.title) + '</span>' +
          '<span class="dev-ti-selector">' + escapeHtml(selector) + '</span>' +
          '<span class="dev-ti-text">"' + escapeHtml(textContent.substring(0, 80)) + (textContent.length > 80 ? '...' : '') + '"</span>' +
        '</div>' +
        '<textarea id="dev-note-text" placeholder="Co s t\u00EDmhle ud\u011Blat..."></textarea>' +
        '<div class="dev-actions">' +
          '<button class="dev-btn dev-btn-cancel" id="dev-cancel">Cancel</button>' +
          '<button class="dev-btn dev-btn-save" id="dev-save">Save</button>' +
        '</div>' +
      '</div>';

    openPanel();
    setTimeout(function() { document.getElementById('dev-note-text').focus(); }, 100);

    document.getElementById('dev-close').onclick = closePanel;
    document.getElementById('dev-cancel').onclick = closePanel;
    document.getElementById('dev-save').onclick = function() {
      var text = document.getElementById('dev-note-text').value.trim();
      if (!text) return;
      var notes = loadNotes();
      var devId = el.getAttribute('data-dev-id') || '';
      notes.push({
        id: Date.now().toString(),
        page: PAGE,
        slide: slide.title,
        slideIndex: slide.index,
        selector: selector,
        devId: devId,
        textContent: textContent,
        note: text,
        createdAt: new Date().toISOString()
      });
      saveNotes(notes);
      renderMarkers();
      closePanel();
    };
  }

  // --- View note panel ---
  function openViewNote(note) {
    panel.innerHTML =
      '<div class="dev-panel-header">' +
        '<h3>Dev Note</h3>' +
        '<button class="dev-panel-close" id="dev-close">&times;</button>' +
      '</div>' +
      '<div class="dev-panel-body">' +
        '<div class="dev-target-info">' +
          '<span class="dev-ti-page">' + escapeHtml(note.page) + '</span>' +
          '<span class="dev-ti-slide">Slide ' + note.slideIndex + ': ' + escapeHtml(note.slide) + '</span>' +
          '<span class="dev-ti-selector">' + escapeHtml(note.selector) + '</span>' +
          '<span class="dev-ti-text">"' + escapeHtml((note.textContent || '').substring(0, 80)) + '"</span>' +
        '</div>' +
        '<div style="background:rgba(245,158,11,0.08);border-radius:6px;padding:14px;color:#c8d6dc;font-size:0.9em;line-height:1.6;white-space:pre-wrap;border-left:3px solid #f59e0b;">' + escapeHtml(note.note) + '</div>' +
        '<div style="font-size:0.75em;color:#8a9ba3;">' + new Date(note.createdAt).toLocaleString('cs-CZ') + '</div>' +
        '<div class="dev-actions">' +
          '<button class="dev-btn dev-btn-delete" id="dev-delete">Delete</button>' +
          '<button class="dev-btn dev-btn-cancel" id="dev-close2">Close</button>' +
        '</div>' +
      '</div>';

    openPanel();
    document.getElementById('dev-close').onclick = closePanel;
    document.getElementById('dev-close2').onclick = closePanel;
    document.getElementById('dev-delete').onclick = function() {
      var notes = loadNotes().filter(function(n) { return n.id !== note.id; });
      saveNotes(notes);
      renderMarkers();
      closePanel();
    };
  }

  // --- List all notes panel ---
  function openNotesList() {
    var notes = loadNotes();
    var pageNotes = notes.filter(function(n) { return n.page === PAGE; });
    var otherNotes = notes.filter(function(n) { return n.page !== PAGE; });

    var html =
      '<div class="dev-panel-header">' +
        '<h3>All Dev Notes (' + notes.length + ')</h3>' +
        '<button class="dev-panel-close" id="dev-close">&times;</button>' +
      '</div>' +
      '<div class="dev-panel-body">';

    if (notes.length === 0) {
      html += '<div style="color:#8a9ba3;font-size:0.9em;text-align:center;padding:20px;">No notes yet. Click on any element to add one.</div>';
    } else {
      if (pageNotes.length > 0) {
        html += '<div style="font-size:0.72em;color:#0891b2;font-weight:700;text-transform:uppercase;letter-spacing:0.05em;margin-bottom:4px;">This page (' + pageNotes.length + ')</div>';
        pageNotes.forEach(function(n, i) {
          html += '<div class="dev-note-item" data-note-id="' + n.id + '">' +
            '<div class="dev-note-text">' + escapeHtml(n.note) + '</div>' +
            '<div class="dev-note-meta">Slide ' + n.slideIndex + ': ' + escapeHtml(n.slide) + '</div>' +
          '</div>';
        });
      }
      if (otherNotes.length > 0) {
        html += '<div style="font-size:0.72em;color:#8a9ba3;font-weight:700;text-transform:uppercase;letter-spacing:0.05em;margin:12px 0 4px;">Other pages (' + otherNotes.length + ')</div>';
        otherNotes.forEach(function(n) {
          html += '<div class="dev-note-item" data-note-id="' + n.id + '">' +
            '<div class="dev-note-text">' + escapeHtml(n.note) + '</div>' +
            '<div class="dev-note-meta">' + escapeHtml(n.page) + ' &middot; Slide ' + n.slideIndex + ': ' + escapeHtml(n.slide) + '</div>' +
          '</div>';
        });
      }
    }

    html += '<div class="dev-actions" style="margin-top:12px;">' +
      '<button class="dev-btn dev-btn-save" id="dev-copy-claude" style="flex:2;">Copy for Claude</button>' +
      '<button class="dev-btn dev-btn-export" id="dev-export">JSON</button>' +
      '<button class="dev-btn dev-btn-delete" id="dev-clear">Clear</button>' +
    '</div></div>';

    panel.innerHTML = html;
    openPanel();

    document.getElementById('dev-close').onclick = closePanel;

    // Click on note items
    panel.querySelectorAll('.dev-note-item').forEach(function(item) {
      item.addEventListener('click', function() {
        var id = item.getAttribute('data-note-id');
        var note = notes.find(function(n) { return n.id === id; });
        if (note) openViewNote(note);
      });
    });

    document.getElementById('dev-copy-claude').onclick = function() {
      var prompt = 'Implement these dev notes from the presentation. For each note, make the requested change in the corresponding file. After completing all changes, tell me to clear the dev notes from localStorage.\n\n';
      prompt += '```json\n' + JSON.stringify(notes, null, 2) + '\n```';
      navigator.clipboard.writeText(prompt).then(function() {
        var btn = document.getElementById('dev-copy-claude');
        btn.textContent = 'Copied!';
        btn.style.background = '#58d68d';
        btn.style.color = '#000';
        setTimeout(function() { btn.textContent = 'Copy for Claude'; btn.style.background = ''; btn.style.color = ''; }, 2000);
      });
    };

    document.getElementById('dev-export').onclick = function() {
      var blob = new Blob([JSON.stringify(notes, null, 2)], { type: 'application/json' });
      var a = document.createElement('a');
      a.href = URL.createObjectURL(blob);
      a.download = 'dev-notes.json';
      a.click();
      URL.revokeObjectURL(a.href);
    };

    document.getElementById('dev-clear').onclick = function() {
      if (confirm('Delete all dev notes?')) {
        saveNotes([]);
        renderMarkers();
        closePanel();
      }
    };
  }

  // --- Markers ---
  function renderMarkers() {
    clearMarkers();
    var notes = loadNotes();
    var pageNotes = notes.filter(function(n) { return n.page === PAGE; });

    // Remove old dev-noted class
    document.querySelectorAll('.dev-noted').forEach(function(el) {
      el.classList.remove('dev-noted');
    });

    pageNotes.forEach(function(note, i) {
      var el = null;
      // Primary: find by data-dev-id (most reliable)
      if (note.devId) { el = document.querySelector('[data-dev-id="' + note.devId + '"]'); }
      // Fallback: CSS selector
      if (!el && note.selector) { try { el = document.querySelector(note.selector); } catch(e) {} }
      // Last resort: slide section
      if (!el && note.slide) {
        el = document.querySelector('[data-title="' + note.slide + '"]');
      }
      if (!el) return;

      if (!el.style.position || el.style.position === 'static') el.style.position = 'relative';
      el.classList.add('dev-noted');

      var marker = document.createElement('div');
      marker.className = 'dev-marker';
      marker.textContent = (i + 1);
      marker.title = note.note.substring(0, 80);
      marker.addEventListener('click', function(e) {
        e.stopPropagation(); e.preventDefault();
        openViewNote(note);
      });
      el.appendChild(marker);
      markerEls.push(marker);
    });

    // Update badge with count
    var total = notes.length;
    badge.innerHTML = '<span style="margin-right:6px;">&#128736;</span> DEV MODE' +
      (total > 0 ? ' <span style="background:rgba(245,158,11,0.9);color:#000;padding:1px 7px;border-radius:10px;font-size:0.85em;margin-left:6px;">' + total + '</span>' : '') +
      ' <span style="font-weight:400;opacity:0.7;margin-left:8px;">(D to exit)</span>';

    // Make badge clickable to open list
    badge.style.cursor = 'pointer';
    badge.onclick = function(e) {
      e.stopPropagation();
      openNotesList();
    };
  }

  function clearMarkers() {
    markerEls.forEach(function(m) { if (m.parentNode) m.parentNode.removeChild(m); });
    markerEls = [];
  }

  function escapeHtml(s) { var d = document.createElement('div'); d.textContent = s || ''; return d.innerHTML; }
})();
