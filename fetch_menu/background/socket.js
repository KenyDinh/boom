let socket = null;
let API_KEY = '';
//initWebSocket();

function initWebSocket(api_key) {
	if ("WebSocket" in window) {
		try {
			API_KEY = api_key;
			socket = new WebSocket("ws://10.24.36.13/friday/socket/friday?friday_token=" + API_KEY);
		} catch (e) {
			console.log(JSON.stringify(e));
			return false;
		}
		if (socket != null) {
			socket.onopen = function(event) {
				console.log('onopen::' + JSON.stringify(event, null, 4));
			}
			
			socket.onmessage = function(event) {
				var msg = event.data;
				console.log('onmessage::' + JSON.stringify(msg, null, 4));
				onMessageSocket(msg);
			}
			
			socket.onclose = function(event) {
				console.log('onclose::' + JSON.stringify(event, null, 4));
			}
			
			socket.onerror = function(event) {
				console.log('onerror::' + JSON.stringify(event, null, 4));
			}
			
			window.onbeforeunload = function() {
				socket.close();
			}
			return true;
		}
	} else {
		alert("Your Browser is NOT support WebSocket!");
	}
	return false;
}
function isSocketOpened() {
	if (socket !== undefined && socket !== null && socket.readyState == WebSocket.OPEN) {
		return true;
	}
	return false;
}
function invalidSocket() {
	socket.close();
	socket = null;
}
function onMessageSocket(msg) {
	var msg_object = JSON.parse(msg);
	switch (msg_object.type) {
	case "load_menu":
		doLoadMenu(msg_object.step);
		break;
	case "milktea_order":
		doMilkteaOrder(msg_object);
		break;
	case "open_menu":
		doOpenMenu(msg_object.url);
		break;
	default:
		break;
	}
}
function sendMessageSocket(msg) {
	if (socket !== undefined && socket != null && socket.readyState == WebSocket.OPEN) {
		socket.send(msg);
	} else {
		console.log("Socket is not open yet!");
	}
}
//=============================================//
var milktea_menu;
function doOpenMenu(url) {
	milktea_menu = {
		"url" : url,
		"status" : 0
	};
	openMenu(milktea_menu);
}

//=============================================//
var milktea_order;
function doMilkteaOrder(obj) {
	switch (obj.step) {
	case "prepare":
		milktea_order = {
			"url" : obj.url,
			"menu_order" : obj.menu_order
		};
		placeOrder();
		break;
	default:
		break;
	}
}

//=============================================//
var index = 0;
var list_object = [];
function doLoadMenu(step) {
	switch (step) {
	case "continue":
		index ++;
		loopSendingMessage();
		break;
	default:
		break;
	}
}

function registSendMenu(menu) {
	index = 0;
	list_object = [];
	list_object.push(getBaseMenuString(menu));
	for (var i in menu.list_item) {
		if (menu.list_item.hasOwnProperty(i)) {
			list_object.push(getBaseMenuItemString(menu.list_item[i]));
		}
	}
	loopSendingMessage();
}
function loopSendingMessage() {
	if (list_object.length > 0 && list_object.hasOwnProperty(index)) {
		sendMessageSocket(list_object[index]);
	} else {
		index = 0;
		list_object = [];
		sendMessageSocket('finish_update');
		feedbackMenu('complete');
	}
}
//=============================================//
var timeout = null;
var fail_count = 0;
function keepConnecting() {
	if (timeout) {
		clearTimeout(timeout);
	}
	if (fail_count > 30) {
		console.error("Socket connection fail more than 5 times, stop retrying!");
		return;
	}
	if (!isSocketOpened()) {
		fail_count++;
		initWebSocket();
	} else {
		fail_count = 0;
	}
	timeout = setTimeout(() => {
		keepConnecting();
	}, 30000);
}