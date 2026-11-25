const Habits = {
    _cache: [],

    async reload() {
        try {
            this._cache = await Api.get('/habits') || [];
            this._render();
        } catch (e) {
            toast(e.message, true);
        }
    },

    _render() {
        const list = document.getElementById('habitList');
        if (!this._cache.length) {
            list.innerHTML = '<p class="muted">Пока нет привычек</p>';
            return;
        }

        list.innerHTML = this._cache.map(h => `
      <div class="habit-item ${h.active ? '' : 'done'}">
        <input type="checkbox" ${h.active ? '' : 'checked'}
               onchange="Habits.toggle(${h.id}, this.checked)">
        <span class="habit-title">${this._escape(h.title)}</span>
        <button class="danger" onclick="Habits.remove(${h.id})">✕</button>
      </div>
    `).join('');
    },

    async add() {
        const input = document.getElementById('habitTitle');
        const title = input.value.trim();
        if (!title) return toast('Введите название привычки', true);
        try {
            await Api.post('/habits', { title, active: true });
            input.value = '';
            await this.reload();
            toast('Привычка добавлена');
        } catch (e) {
            toast(e.message, true);
        }
    },

    async toggle(id, isDone) {
        const h = this._cache.find(x => x.id === id);
        if (!h) return;
        try {
            await Api.put('/habits/' + id, { title: h.title, active: !isDone });
            await this.reload();
        } catch (e) {
            toast(e.message, true);
        }
    },

    async remove(id) {
        if (!confirm('Удалить привычку?')) return;
        try {
            await Api.del('/habits/' + id);
            await this.reload();
        } catch (e) {
            toast(e.message, true);
        }
    },

    _escape(s) {
        return String(s).replace(/[&<>"']/g, c => ({
            '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
        }[c]));
    }
};