// Навигация между разделами
function switchView(name) {
    document.querySelectorAll('.nav-link').forEach(a =>
        a.classList.toggle('active', a.dataset.view === name));
    document.getElementById('view-diary').style.display  = name === 'diary'  ? '' : 'none';
    document.getElementById('view-habits').style.display = name === 'habits' ? '' : 'none';

    if (name === 'habits') Habits.reload();
}

document.addEventListener('DOMContentLoaded', () => {
    // навигация
    document.querySelectorAll('.nav-link').forEach(a => {
        a.addEventListener('click', e => {
            e.preventDefault();
            switchView(a.dataset.view);
        });
    });

    // дата по умолчанию
    const dateInput = document.getElementById('date');
    if (dateInput) {
        dateInput.value = Diary._todayStr();
        dateInput.addEventListener('change', () => Diary.load());
    }

    // проверяем, что сессия жива
    Api.get('/auth/me')
        .then(me => { if (!me) location.href = '/index.html'; })
        .catch(() => { location.href = '/index.html'; });

    // пробуем загрузить дневник за сегодня
    Diary.load();
});