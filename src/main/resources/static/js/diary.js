const Diary = {
    currentDate: null,
    isDirty: false,
    pendingDate: null, // Дата, на которую пользователь хочет перейти

    _today() { return new Date().toISOString().slice(0, 10); },
    _val(id) { return document.getElementById(id).value; },
    _num(id) {
        const v = document.getElementById(id).value;
        return v === '' ? null : Number(v);
    },

    markDirty() {
        this.isDirty = true;
        const el = document.getElementById('status');
        if (el) {
            el.className = 'status saving';
            el.textContent = 'Не сохранено...';
        }
    },

    clearForm() {
        ['hoursOfSleep', 'qualityOfSleep', 'level', 'productivity', 'ratingOfDay']
            .forEach(id => document.getElementById(id).value = '');
        document.getElementById('reflection').value = '';
        document.getElementById('oneSentence').value = '';
        document.getElementById('timeline').innerHTML = '';
        document.getElementById('goals').innerHTML = '';
        document.getElementById('habits').innerHTML = '';

        this.isDirty = false;
        const el = document.getElementById('status');
        if (el) el.textContent = '';
    },

    async changeDate(newDate) {
        if (this.currentDate === newDate) return;

        if (this.isDirty) {
            this.pendingDate = newDate;
            this.showModal();
            return;
        }

        await this._doChangeDate(newDate);
    },

    async _doChangeDate(newDate) {
        this.currentDate = newDate;
        document.getElementById('viewTitle').textContent = 'Дневник дня — ' + newDate;
        await this.load();
        renderCalendar();
    },

    showModal() {
        document.getElementById('confirmModal').style.display = 'flex';
    },

    closeModal() {
        document.getElementById('confirmModal').style.display = 'none';
        this.pendingDate = null;
    },

    confirmDiscard() {
        this.closeModal();
        const target = this.pendingDate;
        this.pendingDate = null;
        this.isDirty = false;
        this._doChangeDate(target);
    },

    async load() {
        this.clearForm();
        if (!this.currentDate) return;

        try {
            const d = await Api.get('/diary/by-date/' + this.currentDate);

            if (!d || !d.id) {
                const el = document.getElementById('status');
                if (el) el.textContent = 'Новая запись';
                return;
            }

            document.getElementById('hoursOfSleep').value   = d.hoursOfSleep   ?? '';
            document.getElementById('qualityOfSleep').value = d.qualityOfSleep ?? '';
            document.getElementById('level').value          = d.level          ?? '';
            document.getElementById('productivity').value   = d.productivity   ?? '';
            document.getElementById('ratingOfDay').value    = d.ratingOfDay    ?? '';
            document.getElementById('reflection').value     = d.reflection     ?? '';
            document.getElementById('oneSentence').value    = d.oneSentence    ?? '';

            (d.timeline || []).forEach(t => this.addTimeline(t));
            (d.goals    || []).forEach(g => this.addGoal(g));
            (d.habits   || []).forEach(h => this.addHabit(h));

            const el = document.getElementById('status');
            if (el) el.textContent = 'Загружено';
        } catch (e) {
            const el = document.getElementById('status');
            if (el) el.textContent = 'Новая запись';
        }
    },

    _buildPayload() {
        return {
            date: this.currentDate,
            hoursOfSleep:   this._num('hoursOfSleep'),
            qualityOfSleep: this._num('qualityOfSleep'),
            level:          this._num('level'),
            productivity:   this._num('productivity'),
            ratingOfDay:    this._num('ratingOfDay'),
            reflection:     this._val('reflection'),
            oneSentence:    this._val('oneSentence'),
            timeline: [...document.querySelectorAll('#timeline .row')].map(r => ({
                timeFrom: r.querySelector('.t-from').value,
                timeTo: r.querySelector('.t-to').value,
                activity: r.querySelector('.t-act').value
            })).filter(t => t.activity || t.timeFrom || t.timeTo),
            goals: [...document.querySelectorAll('#goals .row')].map(r => ({
                text: r.querySelector('.g-text').value,
                completed: r.querySelector('.g-done').checked
            })).filter(g => g.text),
            habits: [...document.querySelectorAll('#habits .row')].map(r => ({
                name: r.querySelector('.h-text').value,
                completed: r.querySelector('.h-done').checked
            })).filter(h => h.name)
        };
    },

    async save() {
        if (!this.currentDate) return;

        const el = document.getElementById('status');
        try {
            if (el) {
                el.className = 'status saving';
                el.textContent = 'Сохранение...';
            }

            await Api.post('/diary', this._buildPayload());

            this.isDirty = false;

            if (el) {
                el.className = 'status saved';
                el.textContent = 'Сохранено ✅';
            }

            setTimeout(() => {
                if (!this.isDirty && el) el.textContent = '';
            }, 3000);

        } catch (e) {
            if (el) {
                el.className = 'status error';
                el.textContent = 'Ошибка сохранения';
            }
            toast(e.message, true);
        }
    },

    addTimeline(data = {}) {
        const row = document.createElement('div');
        row.className = 'row';
        row.innerHTML = `
            <input type="time" class="t-from" value="${data.timeFrom || ''}" oninput="Diary.markDirty()">
            <span class="dash">—</span>
            <input type="time" class="t-to"   value="${data.timeTo   || ''}" oninput="Diary.markDirty()">
            <input type="text" class="t-act"  value="${data.activity || ''}" placeholder="Чем занимались" oninput="Diary.markDirty()">
            <button class="btn-x" type="button" onclick="this.parentElement.remove(); Diary.markDirty();">✕</button>`;
        document.getElementById('timeline').appendChild(row);
    },

    addGoal(data = {}) {
        const row = document.createElement('div');
        row.className = 'row';
        row.innerHTML = `
            <input type="checkbox" class="g-done" ${data.completed ? 'checked' : ''} onchange="Diary.markDirty()">
            <input type="text" class="g-text" value="${data.text || ''}" placeholder="Цель дня" oninput="Diary.markDirty()">
            <button class="btn-x" type="button" onclick="this.parentElement.remove(); Diary.markDirty();">✕</button>`;
        document.getElementById('goals').appendChild(row);
    },

    addHabit(data = {}) {
        const row = document.createElement('div');
        row.className = 'row';
        row.innerHTML = `
            <input type="checkbox" class="h-done" ${data.completed ? 'checked' : ''} onchange="Diary.markDirty()">
            <input type="text" class="h-text" value="${data.name || ''}" placeholder="Привычка" oninput="Diary.markDirty()">
            <button class="btn-x" type="button" onclick="this.parentElement.remove(); Diary.markDirty();">✕</button>`;
        document.getElementById('habits').appendChild(row);
    }
};