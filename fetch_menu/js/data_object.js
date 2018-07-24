var Menu = function() {
	this.menu_name = "";
	this.address = "";
	this.url = "";
	this.pre_image_url = "";
	this.image_url = "";
	this.sale = 0;
	this.code = "";
	this.shipping_fee = 15000; // default
	this.list_item = [];

	this.set_menu_name = function(menu_name) {
		this.menu_name = menu_name;
	}
	this.setAddress = function(address) {
		this.address = address;
	}

	this.set_sale = function(sale) {
		this.sale = sale;
	}
	
	this.set_code = function(code) {
		this.code = code;
	}

	this.set_shipping_fee = function(shipping_fee) {
		this.shipping_fee = shipping_fee;
	}
	
	this.set_url = function(url) {
		this.url = url;
	}
	
	this.set_image_url = function(image_url) {
		this.image_url = image_url;
	}
	
	this.set_pre_image_url = function(pre_image_url) {
		this.pre_image_url = pre_image_url;
	}

	this.add_item = function(item) {
		this.list_item.push(item);
	}
}

var MenuItem = function() {
	this.name = "";
	this.desc = "";
	this.image_url = "";
	this.price = 0;
	this.type = "";
	this.list_size = [];
	this.list_topping = [];
	this.list_sugar = [];
	this.list_ice = [];
	this.list_addition = [];
	this.limit_select = {};

	this.add_size = function(size) {
		this.list_size.push(size);
	}
	
	this.add_topping = function(topping) {
		this.list_topping.push(topping);
	}
	this.add_sugar = function(sugar) {
		this.list_sugar.push(sugar);
	}
	this.add_ice = function(ice) {
		this.list_ice.push(ice);
	}
	this.add_addition = function(addition) {
		this.list_addition.push(addition);
	}
}

var Option = function() {
	this.name = "";
	this.price = 0;
}

function getBaseMenuString(menu) {
	var obj = {};
	obj.menu_name = menu.menu_name;
	obj.address = menu.address;
	obj.url = menu.url;
	obj.pre_image_url = menu.pre_image_url;
	obj.image_url = menu.image_url;
	obj.sale = menu.sale;
	obj.code = menu.code;
	obj.shipping_fee = menu.shipping_fee;
	return "MENU_OBJECT" + JSON.stringify(obj);
}

function getBaseMenuItemString(item) {
	return "MENU_ITEM" + JSON.stringify(item);
}