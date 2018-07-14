function looking_for_menu(request, sender, sendResponse) {
	if (typeof sendResponse == 'function') {
		var menu = fetchData();
		sendResponse(menu);
	}
}

function fetchData() {
	if ($('div#menu-inject-script').length > 0) {
		$('div#menu-inject-script').remove();
	}
	if ($('#friday-inject-script').length > 0) {
		$('#menu-inject-script').remove();
	}
	$('body').append("<div id='menu-inject-script' style='display:none;'></div>");
	$('body').append("<script id='friday-inject-script' >document.getElementById('menu-inject-script').innerHTML = JSON.stringify(menuData);</script>");
	try {
		var menuData = JSON.parse($('div#menu-inject-script').text());
		if (menuData === undefined || menuData === null) {
			console.log("Menu Data not found!");
			return null;
		}
		var menu = new Menu();
		menu.set_menu_name($('.name-hot-restaurant').eq(0).text().trim());
		menu.setAddress($('.name-hot-restaurant + p').eq(0).text().trim());
		menu.set_url(window.location.href);
		menu.set_pre_image_url(window.location.pathname);
		if ($('div.img-hot-restaurant > img').length > 0) {
			menu.set_image_url($('div.img-hot-restaurant > img').eq(0).attr('src'));
		} else if ($('div.img-hot-restaurant > iframe').length > 0) {
			menu.set_image_url($('div.img-hot-restaurant > iframe').eq(0).attr('src'));
		}
		console.log(JSON.stringify(menu));
		var discount_code = $('div.news-promotion').find('.icon-discount-code').eq(0).parent().find(".txt-red");
		if (discount_code.length > 0) {
			var sale = parseInt(discount_code.eq(0).text().replace('%', '').trim());
			menu.set_sale(sale);
		}
		if (discount_code.length > 1) {
			var code = discount_code.eq(1).text().trim();
			menu.set_code(code);
		}
		var discount_air_pay = $('div.news-promotion').find('.icon-discount-airpay').eq(0).parent().find(".txt-red");
		if (discount_air_pay.length > 0) {
			var air_pay_rate = discount_air_pay.eq(0).text().trim();
			if (air_pay_rate == "100%") {
				menu.set_shipping_fee(0);
			}
		}
		for ( var i in menuData.DishType) {
			if (menuData.DishType.hasOwnProperty(i) == false) {
				continue;
			}
			var type = menuData.DishType[i].Name;

			for ( var j in menuData.DishType[i].Dishes) {
				if (menuData.DishType[i].Dishes.hasOwnProperty(j) == false) {
					continue;
				}
				var menu_item = new MenuItem();
				menu_item.name = menuData.DishType[i].Dishes[j].Name;
				menu_item.desc = menuData.DishType[i].Dishes[j].Description;
				menu_item.image_url = menuData.DishType[i].Dishes[j].ImageUrl;
				menu_item.price = menuData.DishType[i].Dishes[j].Price;
				menu_item.type = type;

				// ---------- get option ---------- //
				for ( var k in menuData.DishType[i].Dishes[j].Attributes) {
					if (menuData.DishType[i].Dishes[j].Attributes.hasOwnProperty(k) == false) {
						continue;
					}
					var attribute_name = menuData.DishType[i].Dishes[j].Attributes[k].AttributeName;

					for ( var m in menuData.DishType[i].Dishes[j].Attributes[k].Values) {
						if (menuData.DishType[i].Dishes[j].Attributes[k].Values.hasOwnProperty(m) == false) {
							continue;
						}
						var option = new Option();
						option.name = menuData.DishType[i].Dishes[j].Attributes[k].Values[m].ValueName;
						option.price = menuData.DishType[i].Dishes[j].Attributes[k].Values[m].Price;
						if (attribute_name.toLocaleLowerCase().includes('đá')) {
							menu_item.add_ice(option);
						} else if (attribute_name.toLocaleLowerCase().includes('đường')) {
							menu_item.add_sugar(option);
						} else if (attribute_name.toLocaleLowerCase().includes('topping')) {
							menu_item.add_topping(option);
						} else if (attribute_name.toLocaleLowerCase().includes('size')) {
							menu_item.add_size(option);
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
	} catch (e) {
		console.log(JSON.stringify(e, null, 4));
	}
	return null;
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
		case '?':

			break;
		default:

			break;
		}
	}
});