function getMenuData() {
	try {
		var menuData = JSON.parse($('div#menu-inject-content').text());
		if (menuData === undefined || menuData === null) {
			console.log("Menu Data not found!");
			return null;
		}
		return menuData;
	} catch (e) {
		console.log(JSON.stringify(e, null, 4));
	}
}

function getOrderList() {
	try {
		var order_list = JSON.parse($('div#order-list-inject-content').text());
		if (order_list === undefined || order_list === null) {
			console.log("Order list not found!");
			return null;
		}
		return order_list;
	} catch (e) {
		console.log(JSON.stringify(e, null, 4));
	}
}

function placeOrder() {
	var menuData = getMenuData();
	if (menuData === null) {
		return;
	}
	var controller;
	try {
		JSON.stringify(angular);
		controller = angular.element(document.querySelector('#detail-page')).scope().detailCtrl;
	} catch(e) {
		console.log("Controller not found!");
		return;
	}
	var order_list = getOrderList();
	var origin_item_list = [];
	if (controller.cart.length > 0) {
		console.log('Please reset cart first!');
		return;
	} else {
		for (var i in menuData.DishType) {
			if (menuData.DishType.hasOwnProperty(i) == false) {
				continue;
			}
			for (var j in menuData.DishType[i].Dishes) {
				if (menuData.DishType[i].Dishes.hasOwnProperty(j) == false) {
					continue;
				}
				if (menuData.DishType[i].Dishes[j].IsDayOff || menuData.DishType[i].Dishes[j].OutOfStock) {
					continue;
				}
				origin_item_list.push(menuData.DishType[i].Dishes[j]);
			}
		}
		for (var k = 0; k < order_list.length; k++) {
			var item = null;
			for (var m = 0; m < origin_item_list.length; m++) {
				if (order_list[k].name != origin_item_list[m].Name) {
					continue;
				}
				if (order_list[k].price != origin_item_list[m].Price) {
					continue;
				}
				item = Object.assign({},origin_item_list[m]);
				break;
			}
			if (item === null) {
				console.log("Invalid order: " + JSON.stringify(order_list[k]));
				continue;
			}
			console.log("Do add item here!");
			for (var n = 0; n < item.Attributes.length; n++) {
				for (var p = 0; p < item.Attributes[n].Values.length; p++) {
					for (var q = 0; q < order_list[k].options.length; q++) {
						if (item.Attributes[n].Values[p].ValueName == order_list[k].options[q].name) {
							item.Attributes[n].Values[p].checked = true;
							break;
						}
					}
				}
			}
			controller.actionDish = item;
			controller.insertShoppingCartItemInCludeAttribute();
		}
	}
}
