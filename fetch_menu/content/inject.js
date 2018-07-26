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
		var real_order_list = [];
		for (var k = 0; k < order_list.length; k++) {
			var item = null;
			for (var m = 0; m < origin_item_list.length; m++) {
				if (order_list[k].name != origin_item_list[m].Name) {
					continue;
				}
				if (order_list[k].price != origin_item_list[m].Price) {
					continue;
				}
				item = jQuery.extend(true, {}, origin_item_list[m]);
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
			item.back_id = order_list[k].id;
			real_order_list.push(item);
		}
		var loop_order = function(list, index) {
			if (index >= list.length) {
				document.getElementById('success-order-count').classList.add('order-done');
				return;
			}
			controller.actionDish = list[index];
			controller.insertShoppingCartItemInCludeAttribute();
			let ids = document.getElementById('success-order-count').innerHTML;
			if (ids.length > 0) {
				ids += ",";
			}
			ids += list[index].back_id;
			document.getElementById('success-order-count').innerHTML = ids;
			window.setTimeout(function() {
				loop_order(list, index + 1);
			}, 200);
		}
		loop_order(real_order_list, 0);
	}
}

function clone(obj) {
    var copy;

    // Handle the 3 simple types, and null or undefined
    if (null == obj || "object" != typeof obj) return obj;

    // Handle Date
    if (obj instanceof Date) {
        copy = new Date();
        copy.setTime(obj.getTime());
        return copy;
    }

    // Handle Array
    if (obj instanceof Array) {
        copy = [];
        for (var i = 0, len = obj.length; i < len; i++) {
            copy[i] = clone(obj[i]);
        }
        return copy;
    }

    // Handle Object
    if (obj instanceof Object) {
        copy = {};
        for (var attr in obj) {
            if (obj.hasOwnProperty(attr)) copy[attr] = clone(obj[attr]);
        }
        return copy;
    }

    throw new Error("Unable to copy obj! Its type isn't supported.");
}