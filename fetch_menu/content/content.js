function looking_for_menu(request, sender, sendResponse) {
	if (typeof sendResponse == 'function') {
		getOriginalMenuData(sendResponse);
	}
}

function retrieveMenuData(menuData) {
	if (menuData == undefined || menuData == null) {
		return null;
	}
	var menu = new Menu();
	menu.set_menu_name($('.name-restaurant').eq(0).text().trim());
	menu.setAddress($('.address-restaurant').eq(0).text().trim());
	menu.set_url(window.location.href);
	menu.set_pre_image_url(window.location.pathname);
	if ($('div.detail-restaurant-img > img').length > 0) {
		menu.set_image_url($('div.detail-restaurant-img > img').eq(0).attr('src'));
	} else if ($('div.detail-restaurant-img > iframe').length > 0) {
		menu.set_image_url($('div.detail-restaurant-img > iframe').eq(0).attr('src'));
	}
	//console.log(JSON.stringify(menu));
	let discount_code = $('div.promotions-order').find('.icon-discount-code').eq(0).parent();
	if (discount_code.length > 0) {
		let sale = parseInt(discount_code.find('strong').eq(0).text().replace('%', '').trim());
		menu.set_sale(sale);
		let code = discount_code.find('strong').eq(1).text().trim();
		menu.set_code(code);
		let max_discount = parseInt(discount_code.find('strong').eq(4).text().replace(',',''));
		if (max_discount > 0) {
			menu.set_max_discount(max_discount);
		}
	}
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
			menu_item.name = menuData.menu_infos[i].dishes[j].name;
			menu_item.desc = menuData.menu_infos[i].dishes[j].description;
			let img_index =( menuData.menu_infos[i].dishes[j].photos.length <= 2) ? 0 : 2;
			menu_item.image_url = menuData.menu_infos[i].dishes[j].photos[img_index];
			menu_item.price = menuData.menu_infos[i].dishes[j].price.value;
			menu_item.type = type;

			// ---------- get option ---------- //
			for ( var k in menuData.menu_infos[i].dishes[j].options) {
				if (menuData.menu_infos[i].dishes[j].options.hasOwnProperty(k) == false) {
					continue;
				}
				var attribute_name = menuData.menu_infos[i].dishes[j].options[k].name;
				var min_select = menuData.menu_infos[i].dishes[j].options[k].option_items.min_select;
				var max_select = menuData.menu_infos[i].dishes[j].options[k].option_items.max_select;

				for ( var m in menuData.menu_infos[i].dishes[j].options[k].option_items.items) {
					if (menuData.menu_infos[i].dishes[j].options[k].option_items.items.hasOwnProperty(m) == false) {
						continue;
					}
					var option = new Option();
					option.name = menuData.menu_infos[i].dishes[j].options[k].option_items.items[m].name;
					option.price = menuData.menu_infos[i].dishes[j].options[k].option_items.items[m].price.value;
					if (attribute_name.toLocaleLowerCase().includes('đá')) {
						menu_item.add_ice(option);
						menu_item.limit_select.ice_min = min_select;
						menu_item.limit_select.ice_max = max_select;
					} else if (attribute_name.toLocaleLowerCase().includes('đường')) {
						menu_item.add_sugar(option);
						menu_item.limit_select.sugar_min = min_select;
						menu_item.limit_select.sugar_max = max_select;
					} else if (attribute_name.toLocaleLowerCase().includes('topping') || attribute_name.toLocaleLowerCase().includes('toping')) {
						menu_item.add_topping(option);
						menu_item.limit_select.topping_min = min_select;
						menu_item.limit_select.topping_max = max_select;
					} else if (attribute_name.toLocaleLowerCase().includes('size')) {
						menu_item.add_size(option);
						menu_item.limit_select.size_min = min_select;
						menu_item.limit_select.size_max = max_select;
					} else if (attribute_name.toLocaleLowerCase().includes('thêm') || attribute_name.toLocaleLowerCase().includes('khác') || 
							attribute_name.toLocaleLowerCase().includes('option') || attribute_name.toLocaleLowerCase().includes('chọn')) {
						menu_item.add_addition(option);
						menu_item.limit_select.addition_min = min_select;
						menu_item.limit_select.addition_max = max_select;
					} else {
						console.log("undefined attribute name: " + attribute_name);
					}
				}
			}
			// ------------------------------- //
			menu.add_item(menu_item);
		}
	}

	if (menu.list_item.length == 0) {
		console.log("No menu item found!");
		return null;
	}
	return menu;
}

function placeOrder(request, sender, sendResponse) {
	if (request.order_list) {
		var order_list = request.order_list;
		if (order_list.length == 0) {
			console.log("Order list is empty!");
			sendMessToBackground({type:"order_feed_back",ids:""});
			return;
		}
		if (document.cookie.includes('DELIVERY.AUTH.UDID') == false) {
			console.log("Please login first!");
			sendMessToBackground({type:"order_feed_back",ids:""});
			return;
		}
//		if ($('div#order-list-inject-content').length > 0) {
//			$('div#order-list-inject-content').remove();
//		}
//		if ($('#order-list-inject-script').length > 0) {
//			$('#order-list-inject-script').remove();
//		}
		//$('body').append("<div id='order-list-inject-content' style='display:none;'></div>");
		//$('body').append("<script id='order-list-inject-script' >document.getElementById('order-list-inject-content').innerHTML = '" + JSON.stringify(order_list) + "';</script>");
		// inject javascript
		$('body').append("<span id='success-order-count' style='display:none;'></span>");
		$('body').append("<script>" + getPlaceOrderInjectCode(order_list) + "</script>");
		var countdown = 100;
		var t = window.setInterval(function() {
			if ($('#success-order-count').hasClass('order-done') || countdown < 0) {
				let ids = $('#success-order-count').text();
				if (t != null) {
					clearInterval(t);
					sendMessToBackground({type:"order_feed_back",ids:ids});
				}
				console.log(ids);
			}
			countdown--;
		}, 200);
	} else {
		console.log("No order found!");
	}
}


function getPlaceOrderInjectCode(order_list) {
	var code = "";
	code += "var order_list = " + JSON.stringify(order_list) + ";";
	code += "var controller = angular.element(document.querySelector('#detail-page')).scope().detailCtrl;";
	code += "var origin_item_list = [];";
	code += "if (controller.cart.length > 0) {console.log('Please reset cart first!');document.getElementById('success-order-count').classList.add('order-done');} else {";
	code += "for (var i in menuData.DishType) {";
	code += "if (menuData.DishType.hasOwnProperty(i) == false) {continue;}";
	code += "for (var j in menuData.DishType[i].Dishes) {";
	code += "if (menuData.DishType[i].Dishes.hasOwnProperty(j) == false) {continue;}";
	code += "if (menuData.DishType[i].Dishes[j].IsDayOff || menuData.DishType[i].Dishes[j].OutOfStock) {console.log('item is now not available!');continue;}";
	code += "origin_item_list.push(menuData.DishType[i].Dishes[j]);}";
	code += "}";
	code += "var real_order_list = [];";
	code += "for (var k = 0; k < order_list.length; k++) {";
	code += "let item = null;";
	code += "for (var m = 0; m < origin_item_list.length; m++) {";
	code += "if (order_list[k].name != origin_item_list[m].Name) {continue;}";
//	code += "if (order_list[k].price != origin_item_list[m].Price) {continue;}";
	code += "item = jQuery.extend(true, {}, origin_item_list[m]);break;}";
	code += "if (item === null) {console.log('Invalid order: ' + JSON.stringify(order_list[k]));continue;}";
	//code += "console.log('Do add item here!');";
	code += "let extraPrice = 0;";
	code += "for (var n = 0; n < item.Attributes.length; n++) {";
	code += "for (var p = 0; p < item.Attributes[n].Values.length; p++) {";
	code += "for (var q = 0; q < order_list[k].options.length; q++) {";
	code += "if (item.Attributes[n].Values[p].ValueName == order_list[k].options[q].name) {";
	code += "item.Attributes[n].Values[p].checked = true;";
	code += "item.Attributes[n].Values[p].NumberChoosed = 1;";
	code += "extraPrice += item.Attributes[n].Values[p].Price;";
	code += "break;}";
	code += "}}}";
	code += "item.back_id = order_list[k].id;";
	code += "item.extra_price = extraPrice;";
	code += "real_order_list.push(item);}";
	code += "var loop_order = function(list, index) {\n";
	code += "if (index >= list.length) {";
	code += "document.getElementById('success-order-count').classList.add('order-done');return;}\n";
	code += "controller.actionDish = list[index];\n";
	code += "controller.totalAttributePrice = list[index].extra_price;\n";
	code += "controller.insertShoppingCartItemInCludeAttribute();\n";
	code += "let ids = document.getElementById('success-order-count').innerHTML;\n";
	code += "if (ids.length > 0) {ids += ',';}\n";
	code += "ids += list[index].back_id;\n";
	code += "document.getElementById('success-order-count').innerHTML = ids;\n";
	code += "window.setTimeout(function() {loop_order(list, index + 1);},200);\n";
	code += "}\n";
	code += "loop_order(real_order_list, 0);\n";
	code += "}\n";
	return code;
}

function getOriginalMenuData(sendResponse) {
	let pathname = window.location.pathname.substr(1);
	sendAjaxRequest(
			'https://gappapi.deliverynow.vn/api/delivery/get_from_url',
			'url=' + pathname,
			function(ret) {
				if (ret.result == 'success') {
					sendAjaxRequest(
							'https://gappapi.deliverynow.vn/api/dish/get_delivery_dishes',
							'id_type=2&request_id=' + ret.reply.delivery_id,
							function(ret) {
								if (ret.result == 'success') {
									let menu = retrieveMenuData(ret.reply);
									if (menu != null) {
										chrome.extension.sendMessage({type:'retrieve_menu', menu_data:menu});
									}
								}
							}
					);
				}
			}
	);
}

function sendAjaxRequest(url, data, callback) {
	$.ajax({
		url: url,
		data: data,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		headers: {
			'x-foody-access-token': '',
			'x-foody-api-version': 1,
			'x-foody-app-type': 1004,
			'x-foody-client-id': '',
			'x-foody-client-language': 'vi',
			'x-foody-client-type': 1,
			'x-foody-client-version': '1.8.3'
		},
		success: callback,
		error: function() {
			console.log('error request!');
		}
	});
}

function copyObject(src) {
	return Object.assign({}, src);
}

function sendMessToBackground(data, callback) {
	if (typeof callback == 'function') {
		chrome.extension.sendMessage(data, callback);
	} else {
		chrome.extension.sendMessage(data);
	}
}

chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
	if (request) {
		switch (request.type) {
		case 'looking_for_menu':
			looking_for_menu(request, sender, sendResponse);
			break;
		case 'place_order':
			placeOrder(request, sender, sendResponse);
			break;
		default:

			break;
		}
	}
});