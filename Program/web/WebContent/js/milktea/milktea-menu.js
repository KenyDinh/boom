const MAX_TOPPING = 3;

$j(document).ready(function() {
	initEvent();
	let elem = $j('div#on-scroll-dish-type');
	let mainNav = $j('#main-nav-bar');
	if (elem.length) {
		if ($j(window).outerWidth() >= 992) {
			scrollDishType(elem,mainNav,$j(window));
		} else if ($j(window).outerWidth() <= 576) {
			$j('div#milktea-intro').css('height','');
		}
		$j(window).on('scroll resize', function() {
			scrollDishType(elem,mainNav,$j(this));
		});
	}
	if ($j('#milktea-intro > div#menu-detail').length || $j('#milktea-intro > div#shop-detail').length) {
		if ($j(window).outerWidth() <= 1700) {
			$j('div#milktea-intro').css('height','');
		}
		$j(window).on('scroll resize', function() {
			if ($j(window).outerWidth() <= 1700) {
				$j('div#milktea-intro').css('height','');
			}
		});
	}
	if ($j('img.menu-pre-image').length) {
		resizePreimageMenu();
		$j(window).on('resize', function() {
			resizePreimageMenu();
		});
	}
	$j('img.dish-image').hover(function() {
		if($j('div#dish-image-overlay').length == 0) {
			let size = $j(this).outerHeight();
			let img_size = 360;
			let top = $j(this).offset().top - $j(window).scrollTop();
			let realTop = top;
			if (mainNav.length) {
				realTop =  Math.max(0, top - mainNav.outerHeight());
			}
			let bottom = Math.max(0,$j(window).outerHeight() - top - size);
			let overlayOffTop = top;
			if (bottom < realTop && realTop > img_size) {
				overlayOffTop = top - img_size + size;
			}
			let overlayOffLeft = $j(this).offset().left + size;
			$j('body').append('<div id="dish-image-overlay" style="display:none;position:fixed;z-index:99;top:' + overlayOffTop + 'px;left:' + overlayOffLeft + 'px;"><img src="' + $j(this).attr('src') + '" height="' + img_size + '" /></div>');
		} 
		$j('div#dish-image-overlay').show();
	}, function() {
		if($j('div#dish-image-overlay').length) {
			$j('div#dish-image-overlay').remove();
		}
	});
	$j('div.menu-item').hover(function(){
		if ($j(this).hasClass('bg-light')) {
			$j(this).removeClass('bg-light');
		}
		if ($j(this).hasClass('bg-secondary') == false) {
			$j(this).addClass('bg-secondary');
		}
	},function(){
		if ($j(this).hasClass('bg-secondary')) {
			$j(this).removeClass('bg-secondary');
		}
		if ($j(this).hasClass('bg-light') == false) {
			$j(this).addClass('bg-light');
		}
	});
	if ($j('span#milktea-token').length) {
		var milkteaSocket = new BoomSocket($j('span#milktea-token').text());
		$j('span#milktea-token').remove();
		milkteaSocket.init({onmessage:onMessage});
	}
	
});
function onMessage(event) {
	if (event.data) {
		let obj = JSON.parse(event.data);
		switch(obj.message_type) {
		case 'update':
			sendMilkTeaUpdateRequest(obj.data);
			break;
		default:
			break;
		}
	}
}
function sendMilkTeaUpdateRequest(data) {
	if (data === undefined || data === null) {
		return;
	}
	$j.ajax({
		url: CONTEXT + "/milktea/milk_tea_detail_update.htm",
		type:"POST",
		data:data,
		dataType:"html",
		success:function(result) {
			if (result) {
				let retHtml = $j(result);
				if (retHtml.length) {
					let change = false;
					for (let i = 0; i < retHtml.length; i++) {
						let elem = retHtml.eq(i);
						if ($j('#' + elem.attr('id')).length) {
							$j('#' + elem.attr('id')).html(elem.html());
							change = true;
						}
					}
					if (change) {
						initToolTip();
						initEvent();
					}
				}
			}
		},
		error:function() {
			console.log("error!");
		}
	});
}

function initEvent() {
	$j('div[id^="delete-order-"]').unbind('mouseenter mouseleave');
	$j('div[id^="delete-order-"]').hover(function() {
		if ($j(this).hasClass('text-secondary')) {
			$j(this).removeClass('text-secondary');
			$j(this).addClass('text-danger');
			$j(this).tooltip('show');
		}
	}, function() {
		if ($j(this).hasClass('text-danger')) {
			$j(this).removeClass('text-danger');
			$j(this).addClass('text-secondary');
			$j(this).tooltip('hide');
		}
	});
	$j('span.follow-item').click(function() {
		viewItemByName($j(this).text());
	});
}
function scrollDishType(elem, mainNav, scrollWrap) {
	let scroll = scrollWrap.scrollTop();
	if (scrollWrap.outerWidth() >= 992) {
		let marginTop = parseInt(elem.css('margin-top'),10);
		let eTop = elem.offset().top - mainNav.outerHeight() - marginTop;
		if (scroll >= eTop) {
			elem.css('margin-top',((scroll - eTop) / 16) + 'rem');
		} else {
			elem.css('margin-top','');
		}
	} else {
		elem.css('margin-top','');
	}
	
}

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
					placeOrderModal.modal('hide');
//					$j('div#order-list').html(result);
//					initEvent();
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
			deleteOrderModal.modal('hide');
			if (result) {
//				deleteOrderModal.on('hide.bs.modal', function(e) {
//					$j('div#order-list').html(result);
//					initEvent();
//				});
			} else {
				window.location.reload();
			}
		},
		error:function() {
			window.location.reload();
		}
	});
}

function viewItemByName(name) {
	if (name == undefined || name == null || name.length == 0) {
		return;
	}
	let item = $j('span.item-findable:contains("' + name + '")');
	if (item.length <= 0) {
		return;
	}
	let top = item.offset().top - $j('#menu-item-group-1').offset().top + $j('#milktea-intro').outerHeight();
	if ($j(window).outerWidth() <= 576) {
		top += $j('#on-scroll-dish-type').outerHeight();
	}
	$j('html').animate({
		scrollTop:top
	}, function() {
		item.fadeOut(100,function(){item.addClass('text-danger')}).fadeIn(100).fadeOut(100,function(){item.removeClass('text-danger')}).fadeIn(100);
	});
	//$j(window).scrollTop(top);
}

function viewMenuItemGroup(groupId) {
	if (groupId == undefined || groupId == null || groupId == 0) {
		groupId = 1;
	}
	let item_group = $j('#menu-item-group-' + groupId);
	if (item_group.length <= 0) {
		return;
	}
	let top = item_group.offset().top - $j('#menu-item-group-1').offset().top + $j('#milktea-intro').outerHeight();
	if ($j(window).outerWidth() <= 576) {
		top += $j('#on-scroll-dish-type').outerHeight();
	}
	$j(window).scrollTop(top);
}

function resizePreimageMenu() {
	$j('img.menu-pre-image').each(function() {
		let width = parseInt($j(this).css('width'));
		$j(this).css('height',(width * 458 / 800) + 'px');
	});
}
