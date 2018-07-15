const MAX_TOPPING = 3;

$(document).ready(function() {
	initEvent();
	var elem = $('div#on-scroll-dish-type');
	var mainNav = $('#main-nav-bar');
	if (elem.length) {
		if ($(window).outerWidth() >= 992) {
			scrollDishType(elem,mainNav,$(window));
		} else if ($(window).outerWidth() <= 576) {
			$('div#milktea-intro').css('max-height','');
		}
		$(window).on('scroll resize', function() {
			scrollDishType(elem,mainNav,$(this));
		});
	}
	if ($('img.menu-pre-image').length) {
		resizePreimageMenu();
		$(window).on('resize', function() {
			resizePreimageMenu();
		});
	}
	$('img.dish-image').hover(function() {
		if($('div#dish-image-overlay').length == 0) {
			var size = $(this).outerHeight();
			var img_size = 360;
			var top = $(this).offset().top - $(window).scrollTop();
			var realTop = top;
			if (mainNav.length) {
				realTop =  Math.max(0, top - mainNav.outerHeight());
			}
			var bottom = Math.max(0,$(window).outerHeight() - top - size);
			var overlayOffTop = top;
			if (bottom < realTop && realTop > img_size) {
				overlayOffTop = top - img_size + size;
			}
			var overlayOffLeft = $(this).offset().left + size;
			$('body').append('<div id="dish-image-overlay" style="display:none;position:fixed;z-index:99;top:' + overlayOffTop + 'px;left:' + overlayOffLeft + 'px;"><img src="' + $(this).attr('src') + '" height="' + img_size + '" /></div>');
		} 
		$('div#dish-image-overlay').show();
	}, function() {
		if($('div#dish-image-overlay').length) {
			$('div#dish-image-overlay').remove();
		}
	});
	$('div.menu-item').hover(function(){
		if ($(this).hasClass('bg-light')) {
			$(this).removeClass('bg-light');
		}
		if ($(this).hasClass('bg-secondary') == false) {
			$(this).addClass('bg-secondary');
		}
	},function(){
		if ($(this).hasClass('bg-secondary')) {
			$(this).removeClass('bg-secondary');
		}
		if ($(this).hasClass('bg-light') == false) {
			$(this).addClass('bg-light');
		}
	});
	if ($('span#milktea-token').length) {
		var milkteaSocket = new BoomSocket($('span#milktea-token').text());
		$('span#milktea-token').remove();
		milkteaSocket.init({onmessage:onMessage});
	}
	
});
function onMessage(event) {
	if (event.data) {
		var obj = JSON.parse(event.data);
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
	$.ajax({
		url: CONTEXT + "/milktea/milk_tea_detail_update.htm",
		type:"POST",
		data:data,
		success:function(result) {
			if (result) {
				var retHtml = $(result);
				if ($('#' + retHtml.attr('id')).length) {
					$('#' + retHtml.attr('id')).html(retHtml.html());
					initToolTip();
					initEvent();
				}
			}
		},
		error:function() {
			console.log("error!");
		}
	});
}

function initEvent() {
	$('div[id^="delete-order-"]').unbind('mouseenter mouseleave');
	$('div[id^="delete-order-"]').hover(function() {
		if ($(this).hasClass('text-secondary')) {
			$(this).removeClass('text-secondary');
			$(this).addClass('text-danger');
		}
	}, function() {
		if ($(this).hasClass('text-danger')) {
			$(this).removeClass('text-danger');
			$(this).addClass('text-secondary');
		}
	});
}
function scrollDishType(elem, mainNav, scrollWrap) {
	var scroll = scrollWrap.scrollTop();
	if (scrollWrap.outerWidth() >= 992) {
		var marginTop = parseInt(elem.css('margin-top'),10);
		var eTop = elem.offset().top - mainNav.outerHeight() - marginTop;
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
	var placeOrderModal = $('div#place-order-modal-' + itemId);
	if (placeOrderModal.length == 0) {
		return;
	}
	var form = $('form#place-order-form-' + itemId);
	if (form.length) {
		var params = "menu_id=" + menuId + "&menu_item_id=" + itemId;
		var quantity = 1;
		if (form.find('input#quantity-item-' + itemId).length) {
			params += "&quantity=" + form.find('input#quantity-item-' + itemId).val();
		}
		params += "&mode=1";
		var listCheckedOption = form.find('input:checked');
		var listSize = form.find('input[name="item-option-size"]');
		var listIce = form.find('input[name="item-option-size"]');
		var listSugar = form.find('input[name="item-option-sugar"]');
		var listTopping = form.find('input[name="item-option-topping"]');
		if (listCheckedOption.length > 0) {
			var countSize = 0, countIce = 0, countSugar = 0, countTopping = 0;
			for (var i = 0; i < listCheckedOption.length; i++) {
				var name = listCheckedOption.eq(i).attr('name');
				var value = listCheckedOption.eq(i).attr('id').replace("item-option-" + itemId + "-", "");
				if (name == 'item-option-size') {
					countSize++;
				} else if (name == 'item-option-ice') {
					countIce++;
				} else if (name == 'item-option-sugar') {
					countSugar++;
				} else if (name == 'item-option-topping') {
					countTopping++;
				}
				params += "&menu_item_option=" + value;
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
		}
		$.ajax({
			url:CONTEXT + "/milktea/milk_tea_manage_order.htm",
			type:"POST",
			data:params,
			success:function(result) {
				if (result) {
					placeOrderModal.modal('hide');
//					$('div#order-list').html(result);
//					initEvent();
				}
			},
			error:function() {
				console.log("error!");
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
	var deleteOrderModal = $('div#confirm-delete-order-' + orderId);
	if (deleteOrderModal.length <= 0) {
		return;
	}
	deleteOrderModal.detach();
	$.ajax({
		url: CONTEXT + "/milktea/milk_tea_manage_order.htm",
		type:"POST",
		data:"mode=2&menu_id=" + menuId + "&order_id=" + orderId,
		success:function(result) {
			if (result) {
//				deleteOrderModal.on('hide.bs.modal', function(e) {
//					$('div#order-list').html(result);
//					initEvent();
//				});
				deleteOrderModal.modal('hide');
			}
		},
		error:function() {
			console.log("error!");
		}
	});
}

function viewMenuItemGroup(groupId) {
	if (groupId == undefined || groupId == null || groupId == 0) {
		groupId = 1;
	}
	var item_group = $('#menu-item-group-' + groupId);
	if (item_group.length <= 0) {
		return;
	}
	var top = item_group.offset().top - $('#menu-item-group-1').offset().top + $('#milktea-intro').outerHeight();
	$(window).scrollTop(top);
}

function resizePreimageMenu() {
	$('img.menu-pre-image').each(function() {
		var width = parseInt($(this).css('width'));
		$(this).css('height',(width * 458 / 800) + 'px');
	});
}

