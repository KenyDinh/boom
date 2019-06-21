function looking_for_menu(sendResponse) {
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
	let discount_code = $('div.promotions-order').find('.icon-discount').eq(0).parent();
	if (discount_code.length > 0) {
		let sale = parseInt(discount_code.find('strong').eq(0).text().replace('%', '').trim());
		menu.set_sale(sale);
		let code = discount_code.find('strong').eq(1).text().trim();
		menu.set_code(code);
		let max_discount = parseInt(discount_code.find('strong').eq(3).text().replace(',',''));
		if (max_discount > 0) {
			menu.set_max_discount(max_discount);
		}
	}
	//chrome.extension.sendMessage({type:'retrieve_menu', menu_data:menu});
	let pathname = window.location.pathname.substr(1);
	sendResponse({menu_data:menu, path:pathname});
}

//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_//
function copyObject(src) {
	return Object.assign({}, src);
}
//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_//
function sendMessToBackground(data, callback) {
	if (typeof callback == 'function') {
		chrome.extension.sendMessage(data, callback);
	} else {
		chrome.extension.sendMessage(data);
	}
}
//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_//
chrome.runtime.onMessage.addListener(function(request, sender, sendResponse) {
	if (request) {
		switch (request.type) {
		case 'looking_for_menu':
			looking_for_menu(sendResponse);
			break;
		default:

			break;
		}
	}
});
//_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_//