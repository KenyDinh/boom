var access_token = '';
//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_//
function looking_for_menu() {
	CommonMethod.getTabId('shopeefood.vn', function (tab) {
		if (CommonMethod.isValidData(tab)) {
			Log.info("Start looking for the menu.");
			chrome.tabs.sendMessage(tab.id, {type:'looking_for_menu'}, retreiveMenu);
		} else {
			Log.error("Can not find the tab's id");
		}
	});
}
//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_//
function openMenu() {
	if (CommonMethod.isValidData(milktea_menu)) {
		if (CommonMethod.isValidData(milktea_menu.url) && milktea_menu.status == 0) {
			chrome.tabs.query({active:true,currentWindow:true},function(tabs){
				chrome.tabs.update(tabs[0].id,{url:milktea_menu.url,active:true});
				milktea_menu.status = 1;
			});
		}
	}
}
function feedbackMenu(result) {
	if (CommonMethod.isValidData(milktea_menu)) {
		milktea_menu = null;
		sendMessageSocket("OPEN_FEED_BACK:" + result);
	}
}
//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_//
function feedbackOrder(result) {
	sendMessageSocket("ORDER_FEED_BACK:" + result);
}

function placeOrder() {
//	if (access_token.length <= 0) {
//		Log.error("No access token!");
//		feedbackOrder('error');
//		return;
//	}
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
function sendOrderItem() {
	if (!CommonMethod.isValidData(milktea_order)) {
		Log.error("No menu order found!");
		return;
	}
	let menu_order = milktea_order.menu_order;
	let pathname = new URL(milktea_order.url).pathname.substr(1);
	$.ajax({
		url: 'https://gappapi.deliverynow.vn/api/delivery/get_from_url',
		data: 'url=' + pathname,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		headers: get_headers(),
		success: function(ret) {
			if (ret.result == 'success') {
				menu_order.store_id = ret.reply.restaurant_id;
				menu_order.delivery_type = 1;
				menu_order.shipping_type = 1;
				menu_order.delivery_time = get_format_now();
				delete menu_order.cart_id;
				console.log(JSON.stringify(menu_order));
				///order
				$.ajax({
					url: 'https://gappapi.deliverynow.vn/api/v5/cart/add_items',
					type: 'post',
					data: JSON.stringify(menu_order),
					dataType: 'json',
					contentType: 'application/json; charset=utf-8',
					headers: get_headers_2(),
					success: function(ret) {
						let _result;
						if (ret.msg == 'success') {
							_result = 'success';
						} else {
							_result = 'error';
						}
						milktea_order = null;
						feedbackOrder(_result);
					},
					error: function() {
						console.log('error request!');
					}
				});
			}
		},
		error: function() {
			console.log('error request!');
		}
	});
}
//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_//
function get_headers() {
	return {
		'x-foody-access-token': access_token,
		'x-foody-api-version': 1,
		'x-foody-app-type': 1004,
		'x-foody-client-id': '',
		'x-foody-client-language': 'vi',
		'x-foody-client-type': 1,
		'x-foody-client-version': '3.0.0',
	};
}
function get_headers_2() {
	return {
		'x-foody-access-token': access_token,
		'x-foody-api-version': 1,
		'x-foody-app-type': 1004,
		'x-foody-client-id': '25328836-4543-4657-93a1-cda1a866e4cd',
		'x-foody-client-language': 'vi',
		'x-foody-client-type': 4,
		'x-foody-client-version': '5.28.1',
	};
}
function get_format_now() {
	const dateNow = new Date();
	let date = dateNow.getDate();
	let month = dateNow.getMonth() + 1;
	let year = dateNow.getFullYear();
	let hour = dateNow.getHours();
	let minute = dateNow.getMinutes();
	let second = dateNow.getSeconds();
	return '' + year + '-' + (month < 10 ? '0' + month : month) + '-' + (date < 10 ? '0' + date : date) + ' ' + (hour < 10 ? '0' + hour : hour) + ':' + (minute < 10 ? '0' + minute : minute) + ':' + (second < 10 ? '0' + second : second);
}
//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_//
function retreiveMenu(response) {
	if (typeof response === 'undefined') {
		console.log('no response for menu data!');
		feedbackMenu('No response for menu data!');
		return;
	}
	let menu = response.menu_data;
	let pathname = response.path;
	$.ajax({
		url: 'https://gappapi.deliverynow.vn/api/delivery/get_from_url',
		data: 'url=' + pathname,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		headers: get_headers(),
		success: function(ret) {
			if (ret.result == 'success') {
				$.ajax({
					url: 'https://gappapi.deliverynow.vn/api/dish/get_delivery_dishes',
					data: 'id_type=2&request_id=' + ret.reply.delivery_id,
					dataType: 'json',
					contentType: 'application/json; charset=utf-8',
					headers: get_headers(),
					success: function(ret) {
						if (ret.result == 'success') {
							let menuDatas = ret.reply;
							if (menuDatas == null) {
								console.log('No menu data!');
								feedbackMenu('No menu found!');
								return;
							}
							console.log(ret.reply);
							//Log.log(JSON.stringify(ret.reply));
							addMenuItem(menu, menuDatas);
						}
					},
					error: function() {
						console.log('error request!');
					}
				});
			}
		},
		error: function() {
			console.log('error request!');
		}
	});
}

function addMenuItem(menu, menuData) {
	for ( var i in menuData.menu_infos) {
		if (menuData.menu_infos.hasOwnProperty(i) == false) {
			continue;
		}
		var type = menuData.menu_infos[i].dish_type_name;

		for ( var j in menuData.menu_infos[i].dishes) {
			if (menuData.menu_infos[i].dishes.hasOwnProperty(j) == false) {
				continue;
			}
			if (menuData.menu_infos[i].dishes[j].is_available == false) {
				continue;
			}
			var menu_item = new MenuItem();
			menu_item.item_id = menuData.menu_infos[i].dishes[j].id;
			menu_item.name = menuData.menu_infos[i].dishes[j].name;
			menu_item.desc = menuData.menu_infos[i].dishes[j].description;
			let img_index =( menuData.menu_infos[i].dishes[j].photos.length <= 2) ? 0 : 2;
			menu_item.image_url = menuData.menu_infos[i].dishes[j].photos[img_index].value;
			if (menuData.menu_infos[i].dishes[j].discount_price) {
				menu_item.price = menuData.menu_infos[i].dishes[j].discount_price.value;
			} else {
				menu_item.price = menuData.menu_infos[i].dishes[j].price.value;
			}
			menu_item.type = type;

			// ---------- get option ---------- //
			for ( var k in menuData.menu_infos[i].dishes[j].options) {
				if (menuData.menu_infos[i].dishes[j].options.hasOwnProperty(k) == false) {
					continue;
				}
				let attribute_name = menuData.menu_infos[i].dishes[j].options[k].name;
				let min_select = menuData.menu_infos[i].dishes[j].options[k].option_items.min_select;
				let max_select = menuData.menu_infos[i].dishes[j].options[k].option_items.max_select;
				let option_type_id = menuData.menu_infos[i].dishes[j].options[k].id;

				for ( var m in menuData.menu_infos[i].dishes[j].options[k].option_items.items) {
					if (menuData.menu_infos[i].dishes[j].options[k].option_items.items.hasOwnProperty(m) == false) {
						continue;
					}
					var option = new Option();
					option.id = menuData.menu_infos[i].dishes[j].options[k].option_items.items[m].id;
					option.name = menuData.menu_infos[i].dishes[j].options[k].option_items.items[m].name;
					option.price = menuData.menu_infos[i].dishes[j].options[k].option_items.items[m].price.value;
					if (attribute_name.toLocaleLowerCase().includes('đá')) {
						menu_item.add_ice(option);
						menu_item.limit_select.ice_min = min_select;
						menu_item.limit_select.ice_max = max_select;
						menu_item.limit_select.ice_opt_id = option_type_id;
					} else if (attribute_name.toLocaleLowerCase().includes('đường')) {
						menu_item.add_sugar(option);
						menu_item.limit_select.sugar_min = min_select;
						menu_item.limit_select.sugar_max = max_select;
						menu_item.limit_select.sugar_opt_id = option_type_id;
					} else if (attribute_name.toLocaleLowerCase().includes('topping') || attribute_name.toLocaleLowerCase().includes('toping')) {
						menu_item.add_topping(option);
						if (menu_item.limit_select.topping_min) {
							menu_item.limit_select.topping_min = Math.max(menu_item.limit_select.topping_min,min_select);
						} else {
							menu_item.limit_select.topping_min = min_select;
						} 
						if (menu_item.limit_select.topping_max) {
							menu_item.limit_select.topping_max = Math.max(menu_item.limit_select.topping_max,max_select);
						} else {
							menu_item.limit_select.topping_max = max_select;
						}
						menu_item.limit_select.topping_opt_id = option_type_id;
					} else if (attribute_name.toLocaleLowerCase().includes('size')) {
						menu_item.add_size(option);
						menu_item.limit_select.size_min = min_select;
						menu_item.limit_select.size_max = max_select;
						menu_item.limit_select.size_opt_id = option_type_id;
					} else if (attribute_name.toLocaleLowerCase().includes('thêm') || attribute_name.toLocaleLowerCase().includes('khác') || 
							attribute_name.toLocaleLowerCase().includes('option') || attribute_name.toLocaleLowerCase().includes('chọn')) {
						menu_item.add_addition(option);
						menu_item.limit_select.addition_min = min_select;
						menu_item.limit_select.addition_max = max_select;
						menu_item.limit_select.addition_opt_id = option_type_id;
					} else {
						console.log("undefined attribute name: " + attribute_name);
					}
				}
			}
			// ------------------------------- //
			menu.list_item.push(menu_item);
		}
	}
	if (menu.list_item.length == 0) {
		console.log("No menu item found!");
		feedbackMenu('No menu item found!');
		return;
	}
	//adjust menu image
	var pre_image_url = menu.pre_image_url;
	if (pre_image_url.length > 0) {
		if (pre_image_data.hasOwnProperty(pre_image_url)) {
			menu.pre_image_url = pre_image_data[pre_image_url];
		} else {
			menu.pre_image_url = "";
		}
	}
	// send menu
	registSendMenu(menu);
//	Log.info(JSON.stringify(menu));
}
//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_//
function connectSocket(api_key) {
	if (isSocketOpened()) {
		invalidSocket();
	}
	return initWebSocket(api_key);
}
//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_//
//page update
chrome.tabs.onUpdated.addListener(function(tabId,changeInfo,tab){
	if(tab.status === 'complete'){
		if (CommonMethod.isValidData(milktea_order)) {
			if (tab.url == milktea_order.url && milktea_order.is_placing) {
				sendOrderItem();
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
			if (isSocketOpened()) {
				looking_for_menu();
			}
			break;
		case 'connect_socket':
			const _ret = connectSocket(request.api_key);
			sendResponse({ret: _ret});
			break;
		case 'socket_info':
			let _stt = isSocketOpened() ? "Open" : "Closed";
			sendResponse({
				key: API_KEY,
				stt: _stt
			});
			break;
		default:
			break;
		}
	}
});
//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_//
chrome.webRequest.onBeforeSendHeaders.addListener(
	function(details) {
		for (var i = 0; i < details.requestHeaders.length; ++i) {
			if (details.requestHeaders[i].name === 'x-foody-access-token') {
				if (details.requestHeaders[i].value.length > 0 && details.requestHeaders[i].value != access_token) {
					access_token = details.requestHeaders[i].value;
				}
			}
		}
		return 
		{
			requestHeaders : details.requestHeaders
		};
	},
	// filters
	{
		urls : [ 'https://gappapi.deliverynow.vn/*' ]
	},
	["requestHeaders"]
);
//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_//
chrome.webRequest.onCompleted.addListener(
	function(details) {
		if (details.url.includes('get_detail')) {
			if (CommonMethod.isValidData(milktea_menu)) {
				if (milktea_menu.status == 1) {
					milktea_menu.status = 2;
					setTimeout(looking_for_menu, 1000);
				}
			}
		}
		return 
		{
			responseHeaders : details.responseHeaders
		};
	},
	// filters
	{
		urls : [ 'https://gappapi.deliverynow.vn/*' ]
	},
	["responseHeaders"]
);