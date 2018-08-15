var isSending = false;

$(document).ready(function() {
	sendMessToBackground({type:'init_socket'});
	isSending = false;
	$('#looking-for-menu').click(function() {
		looking_for_menu();
	});
	$('#placeOrderTest').click(function() {
		placeOrderTest();
	});
});

function placeOrderTest() {
	sendMessToBackground({type:'place_order_test'});
}

function looking_for_menu() {
	sendMessToBackground({type:'looking_for_menu'});
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