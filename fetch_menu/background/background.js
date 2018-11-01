var access_token = '';
function looking_for_menu() {
	CommonMethod.getTabId('www.now.vn', function (tab) {
		if (CommonMethod.isValidData(tab)) {
			Log.info("Start looking for the menu.");
			chrome.tabs.sendMessage(tab.id, {type:'looking_for_menu'});
		} else {
			Log.error("Can not find the tab's id");
		}
	});
}

function retrieve_menu(menu) {
	if (CommonMethod.isValidData(menu)) {
		var pre_image_url = menu.pre_image_url;
		if (pre_image_url.length > 0) {
			if (pre_image_data.hasOwnProperty(pre_image_url)) {
				menu.pre_image_url = pre_image_data[pre_image_url];
			} else {
				menu.pre_image_url = "";
			}
		}
		registSendMenu(menu);
		//console.log(JSON.stringify(menu));
	} else {
		Log.error("Menu not found!");
	}
}

function feedBackOrder(result) {
	sendMessageSocket("ORDER_FEED_BACK:" + result);
}

function placeOrder() {
	if (access_token.length <= 0) {
		feedBackOrder('error');
		return;
	}
	if (CommonMethod.isValidData(milktea_order)) {
		if (CommonMethod.isValidData(milktea_order.url)) {
			chrome.tabs.create({
				url:milktea_order.url,
				active:false,
				selected:false
			}, function(tab) {
				milktea_order.is_placing = true;
			});
		}
	}
}

function placeOrderTest() {
	var order_list = [
		{
			name:"Trà Alisan Cheese Milkfoam",
			price:35000,
			options:[{name:"Size M"},{name:"70% đường"},{name:"50% đá"},{name:"Nha Đam"}]
		},
		{
			name:"Trà Bí Đao Cheese Milkfoam",
			price:35000,
			options:[{name:"Size L"},{name:"70% đường"},{name:"50% đá"},{name:"Trân Châu Trắng"}]
		},
		{
			name:"Trà Bí Đao Cheese Milkfoam",
			price:35000,
			options:[{name:"Size L"},{name:"70% đường"},{name:"50% đá"},{name:"Trân Châu Trắng"}]
		}
	];
	milktea_order = {
		url:"https://www.now.vn/ha-noi/tra-sua-gong-cha-giang-vo",
		order_list:order_list
	};
	placeOrder();

}

function reInitSocket(sendResponse) {
	if (isSocketOpened()) {
		return;
	}
	initWebSocket();
	sendResponse({token:access_token});
}

//page update
chrome.tabs.onUpdated.addListener(function(tabId,changeInfo,tab){
	if(tab.status === 'complete'){
		if (CommonMethod.isValidData(milktea_order)) {
			if (tab.url == milktea_order.url && milktea_order.is_placing) {
				chrome.tabs.sendMessage(tab.id, {type:'place_order',token:access_token,menu_order:milktea_order.menu_order}, function (response) {
					//
				});
			}
		}
	}
});

//receive message from ex page.
chrome.extension.onMessage.addListener(function(request, sender, sendResponse) {
	Log.log(JSON.stringify(request.type));
	if (request) {
		switch (request.type) {
		case 'looking_for_menu':
			if (request.token) {
				access_token = request.token;
			}
			looking_for_menu();
			break;
		case 'place_order_test':
			placeOrderTest();
			break;
		case 'place_order_result':
			milktea_order = null;
			feedBackOrder(request.result);
			break;
		case 'init_socket':
			reInitSocket(sendResponse);
			break;
		case 'retrieve_menu':
			retrieve_menu(request.menu_data);
			break;
		default:
			break;
		}
	}
});