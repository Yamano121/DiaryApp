const Diary = {
    _todayStr() {
        return new Date().toISOString().slice(0, 10);
    },

    _val(id) { return document.getElementById(id).value; },
    _num(id) { return parseInt(document.getElementById(id).value, 10); },
    _set(id, v) { document.getElementById(id).value = v ?? ''; },

    async load() {
        const date = this._val('date') || this._todayStr();
        try {
            const d = await Api.get('/diary/by-date/' + date);
            if (!d) return;
            this._set('target', d.target);
            this._set('startTime', d.startTime ?? '09:00');
            this._set('endTime', d.endTime ?? '18:00');
            this._set('hoursOfSleep', d.hoursOfSleep);
            this._set('qualityOfSleep', d.qualityOfSleep);
            this._set('level', d.level);
            this._set('productivity', d.productivity);
            this._set('ratingOfDay', d.ratingOfDay);
            this._set('resultOfDay', d.resultOfDay);
            toast('Запись за ' + date + ' загружена');
        } catch (e) {
            toast('Нет записи за ' + date + ' — можно создать');
        }
    },

    async save() {
        const date = this._val('date') || this._todayStr();
        const body = {
            target: this._val('target'),
            date,
            startTime: this._val('startTime'),
            endTime: this._val('endTime'),
            hoursOfSleep: this._num('hoursOfSleep'),
            qualityOfSleep: this._num('qualityOfSleep'),
            level: this._num('level'),
            productivity: this._num('productivity'),
            ratingOfDay: this._num('ratingOfDay'),
            resultOfDay: this._val('resultOfDay')
        };

        if (!body.target) return toast('Укажите цель дня', true);

        try {
            await Api.post('/diary', body);
            toast('Сохранено ✅');
        } catch (e) {
            toast(e.message, true);
        }
    }
};