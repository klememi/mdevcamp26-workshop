/* ==========================================
   Workshop Tools — Timer
   ========================================== */
(function () {
  'use strict';

  var ICON_CLOCK = '<svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>';

  var DEFAULT_MIN = 45;
  var STEP_MIN = 5;
  var MIN_MIN = 1;
  var MAX_MIN = 180;

  var timerEl = document.createElement('div');
  timerEl.className = 'workshop-timer';
  timerEl.innerHTML =
    '<div class="timer-main">' +
      '<button class="timer-step" id="timer-minus" aria-label="Decrease">−</button>' +
      '<div class="timer-display" id="timer-display">' + DEFAULT_MIN + ':00</div>' +
      '<button class="timer-step" id="timer-plus" aria-label="Increase">+</button>' +
    '</div>' +
    '<div class="timer-bar-row">' + ICON_CLOCK + '<div class="timer-bar"><div class="timer-bar-fill" id="timer-fill" style="width:0%"></div></div></div>' +
    '<div class="timer-controls">' +
      '<button id="timer-start"><span class="icon">▶</span><span class="label">Start</span></button>' +
      '<button id="timer-pause" aria-label="Pause"><span class="icon">⏸</span><span class="label">Pause</span></button>' +
      '<button id="timer-resume" aria-label="Resume"><span class="icon">▶</span><span class="label">Resume</span></button>' +
      '<button id="timer-reset" aria-label="Reset"><span class="icon">↻</span><span class="label">Reset</span></button>' +
    '</div>';
  document.body.appendChild(timerEl);

  var timerInterval = null;
  var timerSeconds = DEFAULT_MIN * 60;
  var timerTotal = DEFAULT_MIN * 60;
  var timerRunning = false;

  function updateTimerDisplay() {
    var m = Math.floor(timerSeconds / 60);
    var s = timerSeconds % 60;
    document.getElementById('timer-display').textContent = m + ':' + (s < 10 ? '0' : '') + s;
    var elapsed = timerTotal > 0 ? ((timerTotal - timerSeconds) / timerTotal * 100) : 0;
    document.getElementById('timer-fill').style.width = elapsed + '%';
    timerEl.classList.toggle('warning', timerSeconds <= 60 && timerSeconds > 0);
    timerEl.classList.toggle('expired', timerSeconds <= 0);
  }

  function adjustTimer(deltaMin) {
    if (timerEl.classList.contains('started')) return;
    var newMin = Math.round(timerTotal / 60) + deltaMin;
    if (newMin < MIN_MIN) newMin = MIN_MIN;
    if (newMin > MAX_MIN) newMin = MAX_MIN;
    timerTotal = newMin * 60;
    timerSeconds = timerTotal;
    updateTimerDisplay();
  }

  function tick() {
    timerInterval = setInterval(function () {
      timerSeconds--;
      updateTimerDisplay();
      if (timerSeconds <= 0) {
        clearInterval(timerInterval);
        timerRunning = false;
      }
    }, 1000);
  }

  function startTimer() {
    if (timerRunning) return;
    timerRunning = true;
    timerEl.classList.add('started');
    timerEl.classList.remove('paused');
    tick();
  }

  function pauseTimer() {
    if (!timerRunning) return;
    clearInterval(timerInterval);
    timerRunning = false;
    timerEl.classList.add('paused');
  }

  function resumeTimer() {
    if (timerRunning || timerSeconds <= 0) return;
    timerRunning = true;
    timerEl.classList.remove('paused');
    tick();
  }

  function resetTimer() {
    clearInterval(timerInterval);
    timerRunning = false;
    timerSeconds = timerTotal;
    timerEl.classList.remove('warning', 'expired', 'started', 'paused');
    updateTimerDisplay();
  }

  document.getElementById('timer-minus').addEventListener('click', function () { adjustTimer(-STEP_MIN); });
  document.getElementById('timer-plus').addEventListener('click', function () { adjustTimer(STEP_MIN); });
  document.getElementById('timer-start').addEventListener('click', startTimer);
  document.getElementById('timer-pause').addEventListener('click', pauseTimer);
  document.getElementById('timer-resume').addEventListener('click', resumeTimer);
  document.getElementById('timer-reset').addEventListener('click', resetTimer);

  document.addEventListener('keydown', function (e) {
    if (e.key === 't' || e.key === 'T') {
      var tag = (e.target.tagName || '').toLowerCase();
      if (tag === 'input' || tag === 'textarea' || tag === 'select') return;
      e.preventDefault();
      timerEl.classList.toggle('active');
    }
  });

})();
