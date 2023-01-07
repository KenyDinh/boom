let isSending = false;
const socket_info = {
	api_key : "",
	status : "---",
}

$(document).ready(function() {
	loadSocketInfo();
	isSending = false;
	$('#looking-for-menu').click(function() {
		if (socket_info.status != 'Open') {
			return;
		}
		looking_for_menu({type:'looking_for_menu'});
	});
	$('#btn-connect').click(function() {
		let _apiKey = $('#api-key').val() || "";
		_apiKey = _apiKey.trim();
		if (_apiKey.length <= 0) {
			return;
		}
		if (_apiKey == socket_info.api_key && socket_info.status == 'Open') {
			return;
		}
		socket_info.api_key = '';
		socket_info.status = '---';
		sendMessToBackground({type:'connect_socket',api_key:_apiKey}, function(data) {
			updateStatus();
			setTimeout(loadSocketInfo, 1000);
			//loadSocketInfo();
		});
	});
});

function loadSocketInfo() {
	sendMessToBackground({type:'socket_info'}, function(info) {
		updateStatus();
		if (info) {
			socket_info.api_key = info.key ?? socket_info.api_key;
			socket_info.status = info.stt ?? socket_info.status;
		}
		$('#api-key').val(socket_info.api_key);
		if (socket_info.status == 'Open') {
			$('#socket-status').removeClass('close').addClass('open');
		} else {
			$('#socket-status').removeClass('open').addClass('close');
		}
		$('#socket-status').text(socket_info.status);
	});
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