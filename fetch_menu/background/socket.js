var socket = null;
//initWebSocket();

function initWebSocket() {
	if ("WebSocket" in window) {
		try {
			socket = new WebSocket("ws://localhost/friday/socket/friday?friday_token=6b41bb111698cadeba27964ce0691834");
		} catch (e) {
			console.log(JSON.stringify(e));
			return;
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
			
			window.beforeunload = function() {
				socket.close();
			}
		}
	} else {
		alert("Your Browser is NOT support WebSocket!");
	}
	
}
function isSocketOpened() {
	if (socket != null && socket.readyState == WebSocket.OPEN) {
		return true;
	}
	return false;
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
	default:
		break;
	}
}
function sendMessageSocket(msg) {
	if (socket != null && socket.readyState == WebSocket.OPEN) {
		socket.send(msg);
	} else {
		console.log("Socket is not open yet!");
	}
}

//=============================================//
var milktea_order;
function doMilkteaOrder(obj) {
	switch (obj.step) {
	case "prepare":
		milktea_order = {};
		milktea_order.url = obj.url;
		milktea_order.order_list = [];
		sendMessageSocket("ORDER_DETAIL");
		break;
	case "detail":
		milktea_order.order_list.push(obj.order);
		sendMessageSocket("ORDER_DETAIL");
		break;
	case "order":
		placeOrder();
		break;
	default:
		break;
	}
}

//=============================================//
var index = 0;
var list_object = [];
var flag = 0;
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
		sendMessageSocket('finish_update' + flag);
	}
}
//=============================================//