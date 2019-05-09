var isSending = false;

$(document).ready(function() {
	sendMessToBackground({type:'init_socket'});
	isSending = false;
	$('#looking-for-menu').click(function() {
		looking_for_menu({type:'looking_for_menu'});
	});
});

function looking_for_menu(data) {
	sendMessToBackground(data);
}

function updateStatus(obj) {
	isSending = false;
}

function sendMessToBackground(data, callback){
	if (isSending === true) {
		return;
	}
	isSending = true;
	if (typeof callback == 'function') {
		chrome.extension.sendMessage(data, callback);
	} else {
		chrome.extension.sendMessage(data);
	}
}