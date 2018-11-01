var isSending = false;

$(document).ready(function() {
	sendMessToBackground({type:'init_socket'}, function(response) {
		if (response && response.token) {
			$('#access_token').val(response.access_token);
			$('#access_token').prop('disabled', true);
			$('#access_token').click(function() {
				$('#access_token').prop('disabled',false);
			});
		}
	});
	isSending = false;
	$('#looking-for-menu').click(function() {
		let data = {type:'looking_for_menu'};
		if ($('#access_token').prop('disabled')) {
		} else {
			data.token = $('#access_token').val();
		}
		looking_for_menu(data);
	});
	$('#placeOrderTest').click(function() {
		placeOrderTest();
	});
});

function placeOrderTest() {
	sendMessToBackground({type:'place_order_test'});
}

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