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
			$j(this).tooltip('show');
		}
	}, function() {
		if ($j(this).hasClass('text-danger')) {
			$j(this).removeClass('text-danger');
			$j(this).addClass('text-secondary');
			$j(this).tooltip('hide');
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
	if (name == undefined || name == null || name.length == 0) {
		return;
	}
	let item = $j('span.item-findable:contains("' + name + '")');
	if (item.length <= 0) {
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
