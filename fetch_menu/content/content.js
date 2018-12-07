function looking_for_menu(request, sender, sendResponse) {
	getOriginalMenuData();
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
						menu_item.limit_select.topping_min = min_select;
						menu_item.limit_select.topping_max = max_select;
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
			menu.add_item(menu_item);
		}
	}

	if (menu.list_item.length == 0) {
		console.log("No menu item found!");
		return null;
	}
	return menu;
}

function placeOrder(request) {
	if (request.menu_order && request.token) {
		let menu_order = request.menu_order;
		let access_token = request.token;
		let pathname = window.location.pathname.substr(1);
		$.ajax({
			url: 'https://gappapi.deliverynow.vn/api/delivery/get_from_url',
			data: 'url=' + pathname,
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
			headers: get_headers(''),
			success: function(ret) {
				if (ret.result == 'success') {
					$.ajax({
						url: 'https://gappapi.deliverynow.vn/api/cart/draft',
						type: 'post',
						data: '{"delivery_id":' + ret.reply.delivery_id + '}',
						dataType: 'json',
						contentType: 'application/json; charset=utf-8',
						headers: get_headers(access_token),
						success: function(ret) {
							if (ret.result == 'success') {
								menu_order.cart_id = ret.reply.id;
								console.log(JSON.stringify(menu_order));
								///order
								$.ajax({
									url: 'https://gappapi.deliverynow.vn/api/cart/set_items',
									type: 'post',
									data: JSON.stringify(menu_order),
									dataType: 'json',
									contentType: 'application/json; charset=utf-8',
									headers: get_headers(access_token),
									success: function(ret) {
										let _result;
										if (ret.result == 'success') {
											_result = 'success';
										} else {
											_result = 'error';
											console.log(JSON.stringify(ret));
										}
										chrome.extension.sendMessage({type:'place_order_result', result:_result});
										window.location.reload();
									},
									error: function() {
										console.log('error request!');
									}
								});
							} else {
								console.log(JSON.stringify(ret));
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
	} else {
		console.log("No menu order found!");
	}
}


function getOriginalMenuData() {
	let pathname = window.location.pathname.substr(1);
	$.ajax({
		url: 'https://gappapi.deliverynow.vn/api/delivery/get_from_url',
		data: 'url=' + pathname,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		headers: get_headers(''),
		success: function(ret) {
			if (ret.result == 'success') {
				$.ajax({
					url: 'https://gappapi.deliverynow.vn/api/dish/get_delivery_dishes',
					data: 'id_type=2&request_id=' + ret.reply.delivery_id,
					dataType: 'json',
					contentType: 'application/json; charset=utf-8',
					headers: get_headers(''),
					success: function(ret) {
						if (ret.result == 'success') {
							let menu = retrieveMenuData(ret.reply);
							if (menu != null) {
								chrome.extension.sendMessage({type:'retrieve_menu', menu_data:menu});
							}
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

function sendAjaxRequest(url, data, token, callback) {
	$.ajax({
		url: url,
		data: data,
		dataType: 'json',
		contentType: 'application/json; charset=utf-8',
		headers: get_headers(token),
		success: callback,
		error: function() {
			console.log('error request!');
		}
	});
}

function get_headers(token) {
	return {
		'x-foody-access-token': token,
		'x-foody-api-version': 1,
		'x-foody-app-type': 1004,
		'x-foody-client-id': '',
		'x-foody-client-language': 'vi',
		'x-foody-client-type': 1,
		'x-foody-client-version': '1.8.3'
	};
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
			placeOrder(request);
			break;
		default:

			break;
		}
	}
});