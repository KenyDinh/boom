//--------------------------------------------/
var ColorFormatlog = {
	none 		: 'font-style:italic;color:#666666;',
	info 		: 'background-color:#CFCFCF;color:#27B227',
	warn 		: 'background-color:#505050;color:#FF6100',
	error		: 'background-color:#E7E7E7;color:#EE1A1A',
	highlight	: 'background-color:#E9F34B;color:#008A04'
};
//--------------------------------------------/

//--------------------------------------------/
var Log = {
	info : function(msg) {
		Log.log(msg,'info');
	},
	warn : function(msg) {
		Log.log(msg,'warn');
	},
	error : function(msg) {
		Log.log(msg,'error');
	},
	highlight : function(msg) {
		Log.log(msg,'highlight');
	},
	log : function(msg,type) {
		var date = new Date();
		if (CommonMethod.isValidData(type) === false || ColorFormatlog.hasOwnProperty(type) === false) {
			type = 'none';
		}
		console.log('%c[' + CommonMethod.getDateFormat(date) + '] %c' + msg, 'font-weight:bold;', ColorFormatlog[type]);
	},
	clear : function() {
		if (typeof console.clear == 'function') {
			console.clear();
		} else {
			Log.error('This browser not support console.clear function!');
		}
	}
};