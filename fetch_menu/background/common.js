var CommonMethod = {
	
	isValidData : function(obj) {
		if (obj === undefined || obj === null) {
			return false;
		}
		if (typeof obj == 'string' && obj.length == 0) {
			return false;
		}
		return true;
	},
	updateUrl : function(tab_id, url) {
	},
	getTabId : function(match, callback) {
		if (typeof callback == 'function') {
			chrome.tabs.query({currentWindow:true},function(tabs){
				if (CommonMethod.isValidData(tabs)) {
					for (var i = 0; i < tabs.length; i++) {
						if (tabs[i].url.includes(match)) {
							callback(tabs[i]);
							return;
						}
					}
					callback(null);
				}
			});
		}
	},
	sendMessToPage : function(tab_id, obj, callback) {
		if (typeof callback == 'function') {
			chrome.tabs.sendMessage(tab_id,obj,callback);
		} else {
			chrome.tabs.sendMessage(tab_id,obj);
		}
	},
	arrayRemove : function(array, element) {
		const index = array.indexOf(element);
		array.splice(index, 1);
	},
	minMax : function(min,value,max) {
		if (CommonMethod.isValidData(value)) {
			if (CommonMethod.isValidData(min) && value < min) {
				return min;
			}
			if (CommonMethod.isValidData(max) && value > max) {
				return max;
			}
			return value;
		}
		return 0;
	},
	isEmpty : function(object) {
		if (CommonMethod.isValidData(object) == false) {
			return false;
		}
		for (var key in object) {
			if (object.hasOwnProperty(key)) {
				return false;
			}
		}
		return true;
	},
	getDateFormat : function(date) {
		var ret = date.getFullYear();
		ret += '/';
		ret += (date.getMonth() < 10) ? ('0' + date.getMonth()) : date.getMonth();
		ret += '/';
		ret += (date.getDate() < 10) ? ('0' + date.getDate()) : date.getDate();
		ret += ' ';
		ret += (date.getHours() < 10) ? ('0' + date.getHours()) : date.getHours();
		ret += ':';
		ret += (date.getMinutes() < 10) ? ('0' + date.getMinutes()) : date.getMinutes();
		ret += ':';
		ret += (date.getSeconds() < 10) ? ('0' + date.getSeconds()) : date.getSeconds();
		return ret;
	},
	capitalize : function(txt) {
		if (txt && typeof txt == 'string' && txt.length > 0) {
			return txt.charAt(0).toUpperCase() + txt.substring(1);
		}
		return 'None';
	}
	
};