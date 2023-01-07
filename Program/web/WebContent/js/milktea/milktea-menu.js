const MAX_TOPPING = 3;
$j(document).ready(function() {
	initEvent();
	scrollDishType();
	adjustIntroBannerHeight();
	let scrollHeight = Math.max(
			document.body.scrollHeight, document.documentElement.scrollHeight,
			document.body.offsetHeight, document.documentElement.offsetHeight,
			document.body.clientHeight, document.documentElement.clientHeight
	);
	showArrowIconScrollUp(scrollHeight);
	$j(window).on('scroll resize', function() {
		scrollDishType();
		adjustIntroBannerHeight();
		showArrowIconScrollUp(scrollHeight);
	});
	if ($j('img.menu-pre-image').length) {
		resizePreimageMenu();
		$j(window).on('resize', function() {
			resizePreimageMenu();
		});
	}
	$j('#btn-scroll-up > img').click(function() {
		$j('body,html').animate({
			scrollTop:0
		},"slow");
//		if (navigator.userAgent.match(/(iPod|iPhone|iPad|Android)/)) {
//		} else {
//		}
	});
	$j('img.dish-image').hover(function() {
		if($j('div#dish-image-overlay').length == 0) {
			let size = $j(this).outerHeight();
			let img_size = 360;
			let top = $j(this).offset().top - $j(window).scrollTop();
			let realTop = top;
			if ($j('#main-nav-bar').length) {
				realTop =  Math.max(0, top - $j('#main-nav-bar').outerHeight());
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
		if ($j(this).hasClass('bg-secondary') == false) {
			$j(this).addClass('bg-secondary');
		}
	},function(){
		if ($j(this).hasClass('bg-secondary')) {
			$j(this).removeClass('bg-secondary');
		}
	});
	if ($j('span#milktea-token').length) {
		var milkteaSocket = new BoomSocket($j('span#milktea-token').text());
		$j('span#milktea-token').remove();
		milkteaSocket.init({onmessage:onMessage});
	}
	$j('#order-list').on('click', '#export-price-table', function() {
		const orderData = getOrderData();
		if (orderData == null || orderData.size <= 0) {
			return;
		}
		const username = $j('#navbarColor02 > .form-inline > div').eq(0).text() || "";
		let table = "|#name|#drink|#price|#paid|";
		table += "\n";
		table += "|:---|:---|:---:|:---:|";
		for (let [key, value] of orderData) {
			table += "\n";
			let dData = value['_dish'];
			let dInfo = "";
			for (let [d_name, d_count] of dData) {
				if (dInfo.length > 0) {
					dInfo += " + ";
				}
				dInfo += d_name;
				if (d_count > 1) {
					dInfo += "(x" + d_count + ")";
				}
			}
			table += "|" + key + "|" + dInfo + "|" + value['_price'] + "đ|";
			if (name == username) {
				table += ":white_check_mark:|";
			} else {
				table += "|";
			}
		}
		table += "\n";
		table += "\n";
		table += "### Leave ur comment here : " + window.location.href;
		const textarea = $j('<textarea></textarea>');
		textarea.val(table);
		$j('body').append(textarea);
		textarea.select();
		document.execCommand('copy');
		textarea.remove();
		//console.log(table);
		const title = $j(this).attr('title');
		$j(this).attr('title','Copied!');
		setTimeout(() => {
			$j(this).attr('title',title);
		}, 3000);
	});
	$j('#menu-list,#shop-list').on('click', '.page-item', function() {
		if ($j(this).hasClass('disabled')) {
			return;
		}
		const page = $j(this).data('page');
		if (typeof page === "undefined" || page === null || page <= 0) {
			return;
		}
		if ($j('#page-msg-id').length <= 0) {
			return;
		}
		const msg_id = $j('#page-msg-id').text();
		data = {
			"msg_id" : msg_id,
			"page" : page
		}
		let offTop = $j('#main-nav-bar').outerHeight();
		if ($j('#menu-list').length) {
			offTop = $j('#menu-list > div').eq(2).offset().top - offTop;
		} else if ($j('#shop-list').length) {
			offTop = $j('#shop-list').offset().top - offTop;
		} else {
			offTop = 0;
		}
		offTop = Math.max(offTop, 0);
		sendMilkTeaUpdateRequest(data);
		$j('body,html').animate({
			scrollTop:offTop
		},"slow");
	});
	$j('.item-comment-all').click(function() {
		const name = ($j(this).data('name') || "Comments");
		const code = ($j(this).data('code') || 0);
		const shop = ($j(this).data('shop') || 0);
		if (shop <= 0) {
			return;
		}
		const _claz = "item-code-" + code;
		const item_comment = $j('#comment-pool').find('.item-comment.' + _claz);
		$j('#item-comment-list').find('#comment-item-name').html(name);
		if (item_comment.length) {
			item_comment.detach().appendTo($j('#item-comment-list div.modal-body'));
			$j('#item-comment-list').modal('show');
			return;
		}
		$j.ajax({
			url: CONTEXT + "/milktea/milk_tea_comment_loader.htm",
			type:"get",
			data:{
				"shop_id" : shop,
				"dish_code" : code
			},
			dataType:"html",
			success:function(result) {
				$j('#item-comment-list div.modal-body').html(result);
				$j('#item-comment-list').modal('show');
			},
			error:function() {
				console.log("error!");
			}
		});
	});
	$j('#item-comment-list').on('hidden.bs.modal', function() {
		$j('#item-comment-list').find('.modal-body > div.item-comment').detach().appendTo($j('#comment-pool'));
	});
});
function onMessage(event) {
	if (event.data) {
		let obj = JSON.parse(event.data);
		switch(obj.message_type) {
		case 'update':
			let data = obj.data;
			if ($j('.page-item.active').length) {
				data += "&page=" + $j('.page-item.active').data('page');
			}
			sendMilkTeaUpdateRequest(data);
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
						initEvent();
						resizePreimageMenu();
					}
				}
			}
		},
		error:function() {
			console.log("error!");
		}
	});
}

function adjustIntroBannerHeight() {
	if ($j('#milktea-intro > div#menu-detail').length || $j('#milktea-intro > div#shop-detail').length) {
		if ($j(window).outerWidth() <= 1700) {
			$j('div#milktea-intro').css('height','');
		}
	}
}

function showArrowIconScrollUp(scrollHeight) {
	if ($j('#btn-scroll-up').length > 0 && $j(window).outerWidth() <= 576 ) {
		if ($j(window).scrollTop() > scrollHeight/2) {
			$j('#btn-scroll-up').css('opacity','0.6');
		} else {
			$j('#btn-scroll-up').css('opacity','0');
		}
	}
}

function initEvent() {
	$j('div[id^="delete-order-"]').unbind('mouseenter mouseleave');
	$j('div[id^="delete-order-"]').hover(function() {
		if ($j(this).hasClass('text-secondary')) {
			$j(this).removeClass('text-secondary');
			$j(this).addClass('text-danger');
		}
	}, function() {
		if ($j(this).hasClass('text-danger')) {
			$j(this).removeClass('text-danger');
			$j(this).addClass('text-secondary');
		}
	});
	$j('span.follow-item').unbind('click');
	$j('span.follow-item').click(function() {
		viewItemByName($j(this).text());
	});
}

function scrollDishType() {
	let elem = $j('div#on-scroll-dish-type');
	if (elem.length <= 0) {
		return;
	}
	let mainNav = $j('#main-nav-bar');
	let scroll = $j(window).scrollTop();
	if ($j(window).outerWidth() >= 992) {
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

function viewItemByName(name) {
	if (name === undefined || name === null || name.length === 0) {
		return;
	}
	let item = null;
	let item_list = $j('span.item-findable:contains("' + name + '")');
	if (item_list.length <= 0) {
		return;
	}
	for (let i = 0; i < item_list.length; i++) {
		if (item_list.eq(i).text() == name) {
			item = item_list.eq(i);
			break;
		}
	}
	if (item === null) {
		return;
	}
	let top = item.closest('div.menu-item').offset().top - $j('#main-nav-bar').outerHeight();
	$j('body,html').animate({
		scrollTop:top
	}, function() {
		item.fadeOut(100,function(){item.addClass('text-danger')}).fadeIn(100).fadeOut(100,function(){item.removeClass('text-danger')}).fadeIn(100);
	});
}

function viewMenuItemGroup(groupId) {
	if (groupId == undefined || groupId == null || groupId == 0) {
		groupId = 1;
	}
	let item_group = $j('#menu-item-group-' + groupId);
	if (item_group.length <= 0) {
		return;
	}
	let top = item_group.parent().offset().top - $j('#main-nav-bar').outerHeight();
	$j(window).scrollTop(top);
}

function resizePreimageMenu() {
	$j('img.menu-pre-image').each(function() {
		let width = parseInt($j(this).css('width'));
		$j(this).css('height',(width * 458 / 800) + 'px');
	});
}

function getOrderData() {
	const p_list = jQuery('#order-list > table tbody tr');
	if (p_list.length <= 1) {
		console.error("No order found!")
		return null;
	}
	let data = new Map();
	for (let i = 0; i < p_list.length - 1; i++) {
		let p_data = p_list.eq(i).find("td");
		let name = p_data.eq(1).text();
		if (name.length <= 0) {
			continue;
		}
		let dish = p_data.eq(0).text().trim();
		let price = parseInt(p_data.eq(6).text().replace(/\D/g,'')) || 0;
		if (data.has(name)) {
			data.get(name)['_price'] += price;
			let cnt = 1;
			if (data.get(name)['_dish'].has(dish)) {
				cnt += data.get(name)['_dish'].get(dish);
			}
			data.get(name)['_dish'].set(dish, cnt);
		} else {
			let dMap = new Map();
			dMap.set(dish, 1);
			data.set(name, {
				_dish : dMap,
				_price : price
			})
		}
	}
	return data;
}
