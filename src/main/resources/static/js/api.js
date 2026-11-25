// Универсальная обёртка над fetch + toast
const Api = {
    async request(url, opts = {}) {
        const res = await fetch(url, {
            headers: { 'Content-Type': 'application/json' },
            ...opts
        });

        // Неавторизован → на логин
        if (res.status === 401) {
            if (location.pathname !== '/index.html' && location.pathname !== '/') {
                location.href = '/index.html';
            }
            return null;
        }

        if (!res.ok) {
            const err = await res.json().catch(() => ({}));
            throw new Error(err.message || `Ошибка ${res.status}`);
        }

        return res.status === 204 ? null : res.json().catch(() => null);
    },

    get(url)        { return this.request(url); },
    post(url, body) { return this.request(url, { method: 'POST', body: JSON.stringify(body) }); },
    put(url, body)  { return this.request(url, { method: 'PUT',  body: JSON.stringify(body) }); },
    del(url)        { return this.request(url, { method: 'DELETE' }); }
};

// Toast-уведомления
function toast(msg, isError = false) {
    const el = document.getElementById('toast');
    if (!el) return;
    el.textContent = msg;
    el.classList.toggle('error', isError);
    el.classList.add('show');
    clearTimeout(el._t);
    el._t = setTimeout(() => el.classList.remove('show'), 2500);
}