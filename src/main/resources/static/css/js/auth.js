const Auth = {
    _getCreds() {
        return {
            login: document.getElementById('login').value.trim(),
            password: document.getElementById('password').value
        };
    },

    _showError(msg) {
        const el = document.getElementById('err');
        if (el) el.textContent = msg || '';
    },

    async login() {
        this._showError('');
        try {
            await Api.post('/auth/login', this._getCreds());
            location.href = '/app.html';
        } catch (e) {
            this._showError(e.message);
        }
    },

    async register() {
        this._showError('');
        try {
            await Api.post('/auth/register', this._getCreds());
            // после регистрации сразу логинимся
            await Api.post('/auth/login', this._getCreds());
            location.href = '/app.html';
        } catch (e) {
            this._showError(e.message);
        }
    },

    async logout() {
        try { await Api.post('/auth/logout', {}); } catch (_) {}
        location.href = '/index.html';
    }
};