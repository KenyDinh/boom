function looking_for_menu() {
	CommonMethod.getTabId('www.now.vn', function (tab) {
		if (CommonMethod.isValidData(tab)) {
			Log.info("Start looking for the menu.");
			chrome.tabs.sendMessage(tab.id, {type:'looking_for_menu'}, function (response) {
				retrieve_menu(response);
			});
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
	} else {
		Log.error("Menu not found!");
	}
}

function placeOrderTest() {
	CommonMethod.getTabId('www.now.vn', function (tab) {
		if (CommonMethod.isValidData(tab)) {
			Log.info("Start placing order.");
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
			chrome.tabs.sendMessage(tab.id, {type:'place_order', order_list:order_list}, function (response) {
//				if (response && response.success) {
//					chrome.tabs.executeScript(tab.id, {
//						file: "content/inject.js"
//					});
//				} else {
//					console.log("failed!");
//				}
			});
		} else {
			Log.error("Can not find the tab's id");
		}
	});
}

function reInitSocket() {
	if (isSocketOpened()) {
		return;
	}
	initWebSocket();
}

//receive message from ex page.
chrome.extension.onMessage.addListener(function(request, sender, sendResponse) {
	Log.log(JSON.stringify(request));
	if (request) {
		switch (request.type) {
		case 'looking_for_menu':
			if (request.flag) {
				flag = request.flag;
				looking_for_menu();
			}
			break;
		case 'place_order_test':
			placeOrderTest();
			break;
		case 'init_socket':
			reInitSocket();
			break;
		default:
			break;
		}
	}
});