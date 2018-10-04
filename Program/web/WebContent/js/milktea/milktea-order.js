
function placeTheOrder(menuId,itemId) {
	if (menuId == undefined || menuId == null || menuId == 0) {
		return;
	}
	if (itemId == undefined || itemId == null || itemId == 0) {
		return;
	}
	let placeOrderModal = $j('div#place-order-modal-' + itemId);
	if (placeOrderModal.length == 0) {
		return;
	}
	let form = $j('form#place-order-form-' + itemId);
	if (form.length) {
		let params = "menu_id=" + menuId + "&menu_item_id=" + itemId;
		let quantity = 1;
		if (form.find('input#quantity-item-' + itemId).length) {
			params += "&quantity=" + form.find('input#quantity-item-' + itemId).val();
		}
		params += "&ticket=" + form.find('input#free-ticket-' + itemId).is(':checked');
		params += "&mode=1";
		let listCheckedOption = form.find('input:checked');
		let listSize = form.find('input[name="item-option-size"]');
		let listIce = form.find('input[name="item-option-size"]');
		let listSugar = form.find('input[name="item-option-sugar"]');
		let listTopping = form.find('input[name="item-option-topping"]');
		let listAddition = form.find('input[name="item-option-addition"]');
		if (listCheckedOption.length > 0) {
			let countSize = 0, countIce = 0, countSugar = 0, countTopping = 0, countAddition = 0;
			for (let i = 0; i < listCheckedOption.length; i++) {
				let name = listCheckedOption.eq(i).attr('name');
				let value = listCheckedOption.eq(i).attr('value');
				if (name == 'item-option-size') {
					params += "&menu_item_option_2=" + encodeURIComponent(value);
					countSize++;
				} else if (name == 'item-option-ice') {
					params += "&menu_item_option_1=" + encodeURIComponent(value);
					countIce++;
				} else if (name == 'item-option-sugar') {
					params += "&menu_item_option_3=" + encodeURIComponent(value);
					countSugar++;
				} else if (name == 'item-option-topping') {
					params += "&menu_item_option_4=" + encodeURIComponent(value);
					countTopping++;
				} else if (name == 'item-option-addition') {
					params += "&menu_item_option_5=" + encodeURIComponent(value);
					countAddition++;
				}
			} 
//			if (countSize == 0 && listSize.length > 0) {
//				console.log("count size: " + countSize);
//				return;
//			}
//			if (countIce == 0 && listIce.length > 0) {
//				console.log("count ice: " + countIce);
//				return;
//			}
//			if (countSugar == 0 && listSugar > 0) {
//				console.log("count sugar: " + countSugar);
//				return;
//			}
			if (countTopping > listTopping.length || countTopping > MAX_TOPPING) {
				console.log("count topping: " + countTopping);
				return;
			}
			if (countAddition > listAddition.length || countAddition > MAX_TOPPING) {
				console.log("count addition: " + countAddition);
				return;
			}
		}
		$j.ajax({
			url:CONTEXT + "/milktea/milk_tea_manage_order.htm",
			type:"POST",
			data:params,
			dataType:"json",
			success:function(result) {
				if (result) {
					placeOrderModal.on('hide.bs.modal', function(e) {
						$j('div#menu-item-place-modal').html(result.model_list);
						$j('div#order-list').html($j(result.order_list).html());
						initEvent();
					});
					placeOrderModal.modal('hide');
				} else {
					window.location.reload();
				}
			},
			error:function() {
				window.location.reload();
			}
		});
	}
}

function deleteTheOrder(menuId,orderId) {
	if (menuId == undefined || menuId == null) {
		return;
	}
	if (orderId == undefined || orderId == null) {
		return;
	}
	let deleteOrderModal = $j('div#confirm-delete-order-' + orderId);
	if (deleteOrderModal.length <= 0) {
		return;
	}
	deleteOrderModal.detach();
	$j.ajax({
		url: CONTEXT + "/milktea/milk_tea_manage_order.htm",
		type:"POST",
		dataType:"json",
		data:"mode=2&menu_id=" + menuId + "&order_id=" + orderId,
		success:function(result) {
			if (result) {
				deleteOrderModal.on('hide.bs.modal', function(e) {
					$j('div#menu-item-place-modal').html(result.model_list);
					$j('div#order-list').html($j(result.order_list).html());
					initEvent();
				});
				deleteOrderModal.modal('hide');
			} else {
				window.location.reload();
			}
		},
		error:function() {
			window.location.reload();
		}
	});
}