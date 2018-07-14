var isSending = false;

$(document).ready(function() {
	sendMessToBackground({type:'init_socket'});
	isSending = false;
	$('#looking-for-menu').click(function() {
		looking_for_menu();
	});
});

function looking_for_menu() {
	var flag = 0;
	if ($('input#new-shop').is(':checked')) {
		flag |= $('input#new-shop').val();
	}
	if ($('input#new-menu').is(':checked')) {
		flag |= $('input#new-menu').val();
	}
	if ($('input#update-detail').is(':checked')) {
		flag |= $('input#update-detail').val();
	}
	if (flag == 0) {
		alert("Flag is not set!");
		return;
	}
	sendMessToBackground({type:'looking_for_menu',flag:flag});
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