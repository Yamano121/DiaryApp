let calYear, calMonth;

// Логика сворачивания меню
function initSidebar() {
    const toggleBtn = document.getElementById('toggleSidebarBtn');
    const appLayout = document.getElementById('appLayout');

    // Проверка сохраненного состояния в localStorage (только для ПК)
    const isCollapsed = localStorage.getItem('sidebarCollapsed') === 'true';
    if (isCollapsed && window.innerWidth > 768) {
        appLayout.classList.add('collapsed');
    }

    if (toggleBtn) {
        toggleBtn.onclick = () => {
            if (window.innerWidth <= 768) {
                // На мобилках меню выезжает/заезжает
                appLayout.classList.toggle('sidebar-open');
            } else {
                // На десктопе меню сворачивается в иконки
                appLayout.classList.toggle('collapsed');
                const nowCollapsed = appLayout.classList.contains('collapsed');
                localStorage.setItem('sidebarCollapsed', nowCollapsed);
            }
        };
    }
}

function renderCalendar() {
    const grid = document.getElementById('calendarGrid');
    if (!grid) return;
    grid.innerHTML = '';

    const months = ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь',
        'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'];
    document.getElementById('currentMonthLabel').textContent = `${months[calMonth]} ${calYear}`;

    const firstDay = new Date(calYear, calMonth, 1).getDay();
    const daysInMonth = new Date(calYear, calMonth + 1, 0).getDate();
    let startOffset = firstDay === 0 ? 6 : firstDay - 1;

    for (let i = 0; i < startOffset; i++) {
        grid.insertAdjacentHTML('beforeend', `<div></div>`);
    }

    for (let day = 1; day <= daysInMonth; day++) {
        const dateStr = `${calYear}-${String(calMonth + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
        const el = document.createElement('div');
        el.className = 'calendar-day';
        el.textContent = day;

        if (dateStr === Diary.currentDate) {
            el.classList.add('selected');
        }

        el.onclick = () => {
            Diary.changeDate(dateStr);
            // На мобилках закрываем меню после выбора даты
            if (window.innerWidth <= 768) {
                document.getElementById('appLayout').classList.remove('sidebar-open');
            }
        };

        grid.appendChild(el);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    initSidebar();

    const today = new Date();
    calYear = today.getFullYear();
    calMonth = today.getMonth();

    Diary.currentDate = today.toISOString().slice(0, 10);
    document.getElementById('viewTitle').textContent = 'Дневник дня — ' + Diary.currentDate;

    renderCalendar();
    Diary.load();

    document.getElementById('prevMonth').onclick = () => {
        calMonth--;
        if (calMonth < 0) { calMonth = 11; calYear--; }
        renderCalendar();
    };
    document.getElementById('nextMonth').onclick = () => {
        calMonth++;
        if (calMonth > 11) { calMonth = 0; calYear++; }
        renderCalendar();
    };

    // Проверка авторизации
    fetch('/auth/me')
        .then(r => { if (r.status === 401) location.href = '/index.html'; })
        .catch(() => {});
});