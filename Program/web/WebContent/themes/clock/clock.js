$j(function() {
    const SEC_ROTATION_NAME = "sec-plate";
    const MIN_ROTATION_NAME = "min-plate";
    const HOUR_ROTATION_NAME = "hour-plate";
    const DAY_ROTATION_NAME = "day-plate";
    const DATE_ROTATION_NAME = "date-plate";
    const MONTH_ROTATION_NAME = "month-plate";
    const BRAN_ROTATION_NAME = "bran-plate";
    const STEM_ROTATION_NAME = "stem-plate";
    const YEAR_ROTATION_NAME = "year-plate";
    var interval;
    var time_worker;
    var count = 0
    const INIT_TRANSITION_TIME = 2;
    const FULL_ROTATION_DEG = 360;
    const TOTAL_SECOND = 60;
    const TOTAL_MINUTE = 60;
    const TOTAL_HOUR = 24;
    const MONTH_OF_YEAR = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];
    const DAYS_IN_WEEK = ["Sun","Mon","Tue","Wed","Thu","Fri","Sat"];
    const STEMS = ["Canh","Tân","Nhâm","Quý","Giáp","Ất","Bính","Đinh","Mậu","Kỷ"];
    const BRANCHES = ["Thân","Dậu","Tuất","Hợi","Tý","Sửu","Dần","Mão","Thìn","Tỵ","Ngọ","Mùi"];
    let TOTAL_DAY = 30;
    
    const days_in_month = () => {
        const date = new Date();
        const year = date.getFullYear();
        const month = date.getMonth();
        return new Date(year, month + 1, 0).getDate();
    }
    const format_time = (t) => {
        if (typeof t === "undefined" || t === null || isNaN(t)) {
            return "00";
        }
        t = Number(t);
        return (t < 10 ? ("0" + t) : t.toString());
    }
    const update_time = (trigger_id) => {
        switch (trigger_id) {
            case SEC_ROTATION_NAME:
                update_min_rotation();
                break;
            case MIN_ROTATION_NAME:
                update_hour_rotation();
                break;
            case HOUR_ROTATION_NAME:
                update_day_rotation();
                update_day_in_week_rotation();
                break;
            case DATE_ROTATION_NAME:
                update_month_rotation();
                const days = days_in_month();
                if (days != TOTAL_DAY) {
                	update_number_of_day();
                }
                break;
            case MONTH_ROTATION_NAME:
                update_bran_rotation();
                update_stem_rotation();
                init_year();
                break;
            default:
                break;
        }
    }
    const start_rotation = () => {
        if (interval) {
            clearInterval(interval);
            interval = undefined;
        }
        if (typeof(time_worker) !== "undefined") {
        	time_worker.terminate();
        	time_worker = undefined;
        }
        // count = 0;
        const _DATE = new Date();
        const second = _DATE.getSeconds();
        const minute = _DATE.getMinutes();
        const hour = _DATE.getHours();
        const day_in_week = _DATE.getDay();
        const day = _DATE.getDate();
        const month = _DATE.getMonth() + 1;
        const year = _DATE.getFullYear();
        const bran = year % 12;
        const stem = year % 10;
        const sec_deg = (FULL_ROTATION_DEG / TOTAL_SECOND) * second;
        const min_deg = (FULL_ROTATION_DEG / TOTAL_MINUTE) * minute;
        const hour_deg = (FULL_ROTATION_DEG / TOTAL_HOUR) * hour;
        const diw_deg = (FULL_ROTATION_DEG / DAYS_IN_WEEK.length) * day_in_week;
        const day_deg = (FULL_ROTATION_DEG / TOTAL_DAY) * (day - 1);
        const month_deg = (FULL_ROTATION_DEG / MONTH_OF_YEAR.length) * (month - 1);
        const bran_deg = (FULL_ROTATION_DEG / BRANCHES.length) * bran;
        const stem_deg = (FULL_ROTATION_DEG / STEMS.length) * stem;
        /*
        console.log('sec : ' + second + ", deg : " + sec_deg);
        console.log('min : ' + minute + ", deg : " + min_deg);
        console.log('hour : ' + hour + ", deg : " + hour_deg);
        console.log('day in week : ' + day_in_week + ", deg : " + diw_deg);
        console.log('day : ' + day + ", deg : " + day_deg);
        console.log('month : ' + month + ", deg : " + month_deg);
        console.log('bran : ' + bran + ", deg : " + bran_deg);
        console.log('stem : ' + stem + ", deg : " + stem_deg);
        */
        init_sec_rotation_data(sec_deg, second);
        init_sec_rotation_data(sec_deg, second);
        init_sec_rotation_data(sec_deg, second);
        init_min_rotation_data(min_deg, minute);
        init_hour_rotation_data(hour_deg, hour);
        init_day_in_week_rotation_data(diw_deg, day_in_week);
        init_day_rotation_data(day_deg, day - 1);
        init_month_rotation_data(month_deg, month - 1);
        init_bran_rotation_data(bran_deg, bran);
        init_stem_rotation_data(stem_deg, stem);
        $j('div#' + YEAR_ROTATION_NAME).addClass('highlight').html(year);
    
        // move to web worker later.
        if (typeof(Worker) !== "undefined") {
            if (typeof(time_worker) == "undefined") {
                time_worker = new Worker(CONTEXT + "/themes/clock/time-worker.js");
            }
            time_worker.onmessage = (e) => {
                update_sec_rotation();
            }
        } else {
            update_sec_rotation();
            interval = setInterval(() => {
               update_sec_rotation();
            }, 1000);
        }
    }
    const stop_rotaion = () => {

    }
    const get_rotate_style = (sdeg) => {
        return {
            '-ms-transform' : 'translate(-50%, -50%) rotateZ('+ sdeg +'deg)',
            '-webkit-transform' : 'translate(-50%, -50%) rotateZ('+ sdeg +'deg)',
            'transform' : 'translate(-50%, -50%) rotateZ('+ sdeg +'deg)'
        }
    }
    /**
     * 
     * @param {*} id element id
     * @param {*} deg init degrees
     * @param {*} idx current value
     */
    const init_rotation_data = (id, deg, idx) => {
        const obj_rotation = $j('div#' + id);
        if (obj_rotation.length <= 0) {
            console.error("Object rotation not found!!! id : " + id);
            return;
        }
        obj_rotation.data('deg', deg);
        obj_rotation.find('div.child').each(function() {
            if ($j(this).data('idx') == idx) {
                $j(this).addClass('highlight');
            } else if ($j(this).hasClass('highlight')) {
                $j(this).removeClass('highlight');
            }
        });
        idx++; // next value
        if (idx >= obj_rotation.data('max')) {
            idx = 0;
        }
        obj_rotation.data('idx', idx);
        obj_rotation.css(get_rotate_style(-deg));
    }
    const init_sec_rotation_data = (deg, idx) => {
        init_rotation_data(SEC_ROTATION_NAME, deg, idx);
    }
    const init_min_rotation_data = (deg, idx) => {
        init_rotation_data(MIN_ROTATION_NAME, deg, idx);
    }
    const init_hour_rotation_data = (deg, idx) => {
        init_rotation_data(HOUR_ROTATION_NAME, deg, idx);
    }
    const init_day_in_week_rotation_data = (deg, idx) => {
        init_rotation_data(DAY_ROTATION_NAME, deg, idx);
    }
    const init_day_rotation_data = (deg, idx) => {
        init_rotation_data(DATE_ROTATION_NAME, deg, idx);
    }
    const init_month_rotation_data = (deg, idx) => {
        init_rotation_data(MONTH_ROTATION_NAME, deg, idx);
    }
    const init_bran_rotation_data = (deg, idx) => {
        init_rotation_data(BRAN_ROTATION_NAME, deg, idx);
    }
    const init_stem_rotation_data = (deg, idx) => {
        init_rotation_data(STEM_ROTATION_NAME, deg, idx);
    }
    /**
     * 
     * @param {*} id element id
     */
    const update_obj_rotation = (id) => {
        const obj_rotation = $j('div#' + id);
        if (obj_rotation.length <= 0) {
            console.error("Object rotation not found!!! id : " + id);
            return;
        }
        const inc = obj_rotation.data('inc');
        let deg = obj_rotation.data('deg') + inc;
        deg = (deg + 1 >= FULL_ROTATION_DEG ? 0 : deg);
        obj_rotation.data('deg', deg);
        let idx = obj_rotation.data('idx');
        obj_rotation.find('div.child').each(function() {
            if ($j(this).hasClass('highlight')) {
                $j(this).removeClass('highlight');
            } else if ($j(this).data('idx') == idx) {
                $j(this).addClass('highlight');
            }
        });
        idx++;
        if (idx >= obj_rotation.data('max')) {
            idx = 0;
        }
        obj_rotation.data('idx', idx);
        //console.info('rotate : ' + deg);
        if (deg == 0) {
            obj_rotation.css('transition-property','none');
            obj_rotation.css('transition-duration','0.95s');
            obj_rotation.css(get_rotate_style(inc));
            setTimeout(() => {
                obj_rotation.css('transition-property','');
                obj_rotation.css('transition-duration','');
                obj_rotation.css(get_rotate_style(deg));
            }, 50);
            update_time(id);
        } else {
            obj_rotation.css(get_rotate_style(-deg));
        }
    }
    const update_sec_rotation = () => {
        $j('div#' + SEC_ROTATION_NAME).find('div.child').css({
            'transition-property' : 'font-weight, color',
            'transition-duration' : '0.8s'
        });
        update_obj_rotation(SEC_ROTATION_NAME);
        // count++;
        // if (count > 3600) {
        //     start_rotation();
        //     return;
        // }
    }
    const update_min_rotation = () => {
        update_obj_rotation(MIN_ROTATION_NAME); 
    }
    const update_hour_rotation = () => {
        update_obj_rotation(HOUR_ROTATION_NAME); 
    }
    const update_day_in_week_rotation = () => {
        update_obj_rotation(DAY_ROTATION_NAME); 
    }
    const update_day_rotation = () => {
        update_obj_rotation(DATE_ROTATION_NAME); 
    }
    const update_month_rotation = () => {
        update_obj_rotation(MONTH_ROTATION_NAME); 
    }
    const update_bran_rotation = () => {
        update_obj_rotation(BRAN_ROTATION_NAME); 
    }
    const update_stem_rotation = () => {
        update_obj_rotation(STEM_ROTATION_NAME); 
    }
    /**
     * 
     * @param {*} id element id
     * @param {*} data_list data for rotation
     * @param {*} ts_time transistion time when rotating
     */
    const init_obj_rotation = (id, data_list, ts_time) => {
        if (!id || !data_list || !ts_time) {
            console.error("Invalid parameters!!!");
            return;
        }
        if (!data_list.length || ts_time <= 0) {
            console.error("Invalid parameters!!!");
            return;
        }
        const obj = $j('div#' + id);// TODO
        if (obj.length <= 0) {
            console.error("No object found!!! id: " + id);
            return;
        }
        const size = data_list.length;
        const inc = (FULL_ROTATION_DEG / size);
        for (let i = 0; i < size; i++) {
            const deg = FULL_ROTATION_DEG - (inc * i);
            const dur = ts_time - (ts_time / size) * i;
            const child = $j('<div class="elem child">' + data_list[i] + '</div>');
            child.css('transition-duration', dur + 's');
            child.data('deg', deg);
            child.data('idx', i);
            child.appendTo(obj);
            child.appendTo(obj);
        }
        obj.data('idx', 0);
        obj.data('max', size);
        obj.data('deg', 0);
        obj.data('inc', inc);
        setTimeout(() => {
            $j('div#' + id).find('div.child').each(function() {
                const deg = $j(this).data('deg');
                $j(this).css('-ms-transform','translate(-50%, -50%) rotateZ(-' + deg + 'deg)');
                $j(this).css('transform','translate(-50%, -50%) rotateZ(-' + deg + 'deg)');
            });
        }, 100);
    }
    const init_sec = () => {
        const data_list = [];
        for (let i = 0; i < TOTAL_SECOND; i++) {
            data_list.push(format_time(i));
        }
        init_obj_rotation(SEC_ROTATION_NAME, data_list, INIT_TRANSITION_TIME);
    }
    const init_min = () => {
        const data_list = [];
        for (let i = 0; i < TOTAL_MINUTE; i++) {
            data_list.push(format_time(i));
        }
        init_obj_rotation(MIN_ROTATION_NAME, data_list, INIT_TRANSITION_TIME);
    }
    const init_hour = () => {
        const data_list = [];
        for (let i = 0; i < TOTAL_HOUR; i++) {
            data_list.push(format_time(i));
        }
        init_obj_rotation(HOUR_ROTATION_NAME, data_list, INIT_TRANSITION_TIME);
    }
    const init_day_in_week = () => {
        init_obj_rotation(DAY_ROTATION_NAME, DAYS_IN_WEEK, INIT_TRANSITION_TIME);
    }
    const init_day = () => {
        TOTAL_DAY = days_in_month();
        const data_list = [];
        for (let i = 0; i < TOTAL_DAY; i++) {
            data_list.push(format_time(i + 1));
        }
        init_obj_rotation(DATE_ROTATION_NAME, data_list, INIT_TRANSITION_TIME);
    }
    const init_month = () => {
        init_obj_rotation(MONTH_ROTATION_NAME, MONTH_OF_YEAR, INIT_TRANSITION_TIME);
    }
    const init_bran = () => {
        init_obj_rotation(BRAN_ROTATION_NAME, BRANCHES, INIT_TRANSITION_TIME);
    }
    const init_stem = () => {
        init_obj_rotation(STEM_ROTATION_NAME, STEMS, INIT_TRANSITION_TIME);
    }
    const init_year = () => {
        $j('div#' + YEAR_ROTATION_NAME).html((new Date()).getFullYear());
    }
    const update_number_of_day = () => {
    	const obj = $j('div#' + DATE_ROTATION_NAME);
    	if (obj.length <= 0) {
            return;
        }
    	obj.empty();
    	obj.data('idx', 0).data('max', 0).data('deg', 0).data('inc', 0).removeAttr('style');
    	init_day();
    	setTimeout(() => {
    		const _DATE = new Date();
            const day = _DATE.getDate();
            const day_deg = (FULL_ROTATION_DEG / TOTAL_DAY) * (day - 1);
            init_day_rotation_data(day_deg, day - 1);
        }, INIT_TRANSITION_TIME * 1000); // 
    }
    const resize_clock = () => {
        const clock = $j('div#clock');
        if (clock.length <= 0) {
            console.error("Clock not found!!!");
            return;
        }
        const max_height = $j('div#calendar').outerHeight();
        const max_width = $j('div#calendar').outerWidth();
        let _size = Math.min(max_height, max_width);
        _size = _size * 0.9;
        clock.css({
            'width': _size + 'px',
            'height': _size + 'px'
        });
    }
    /// start
    resize_clock();
    $j(window).on('resize', function() {
		resize_clock();
	});
    const funcs = [
        {
            "id" : SEC_ROTATION_NAME,
            "func" : init_sec
        },
        {
            "id" : MIN_ROTATION_NAME,
            "func" : init_min
        },
        {
            "id" : HOUR_ROTATION_NAME,
            "func" : init_hour
        },
        {
            "id" : DAY_ROTATION_NAME,
            "func" : init_day_in_week
        },
        {
            "id" : DATE_ROTATION_NAME,
            "func" : init_day
        },
        {
            "id" : MONTH_ROTATION_NAME,
            "func" : init_month
        },
        {
            "id" : BRAN_ROTATION_NAME,
            "func" : init_bran
        },
        {
            "id" : STEM_ROTATION_NAME,
            "func" : init_stem
        },
    ];
    let t_step = 1000;
    let cnt = 0, total = 0;
    init_year();
    for (let i = funcs.length - 1; i >= 0; i--) {
        if ($j('div#' + funcs[i].id).is(':visible')) {
            let time_wait = t_step * cnt;
            if (time_wait > 0) {
                setTimeout(() => {
                    funcs[i].func();
                }, time_wait);
            } else {
                funcs[i].func();
            }
            cnt++;
        } else {
            funcs[i].func();
        }
    }
    setTimeout(() => {
        start_rotation();
    }, t_step * (cnt + 1));
});