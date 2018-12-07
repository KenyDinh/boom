var $j = jQuery.noConflict();
$j(document).ready(function(e) {
	if ($j('img.survey-opt-image').length) {
		resizeOptionImage();
	}
	$j(window).on('resize', function() {
		resizeOptionImage();
		//resizeResultImage();
	});
	$j('div[class*="option-select-"]').click(function() {
		let checkedElem = $j(this).find('div.option-checked');
		if (checkedElem.length) {
			let id = $j(this).attr('data').replace('option-','');
			let limit = parseInt($j('#max_choice').text());
			if (checkedElem.is(':visible')) {
				let rsElem = $j('div.option-result-' + id);
				if (rsElem.length) {
					rsElem.parent().addClass('result-empty');
					if ($j('.result-empty').length >= limit) {
						resizeResultImage();
					}
					rsElem.remove();
				}
				checkedElem.hide();
				$j(this).find('img.survey-opt-image').removeClass('check');
			} else {
				let cur = getSelectedOption();
				if (cur.length >= limit) {
					alert("Choose maximum " + limit + "!");
					return;
				}
				let rsEmptyList = $j('div.result-empty');
				if (rsEmptyList.length) {
					let rsElem = rsEmptyList.eq(0);
					rsElem.removeClass('result-empty');
					let obj = $j(this).clone();
					obj.removeClass('option-select-' + id).addClass('option-result-' + id);
					rsElem.html(obj);
					resizeOptionImage();
					$j('div#result-frame').attr('style','')
				}
				checkedElem.show();
				$j(this).find('img.survey-opt-image').addClass('check');
			}
		}
	});
	$j('[data-toggle="tooltip"]').tooltip();
});
function getSelectedOption() {
	let ret = [];
	let option_list = $j('div.survey-option');
	if (option_list.length) {
		for (let i = 0; i < option_list.length; i++) {
			let checkedElem = option_list.eq(i).find('div.option-checked');
			if (checkedElem.length) {
				if (checkedElem.is(':visible')) {
					ret.push(option_list.eq(i).attr('data').replace('option-',''));
				}
			}
		}
	}
	return ret;
}
function sendVote() {
	let limit = parseInt($j('#max_choice').text());
	let minimum = parseInt($j('#min_choice').text());
	let cur = getSelectedOption();
	if (cur.length < minimum) {
		//$j('#error').text('Please choose at least 1 option!');
		alert("Please choose at least " + minimum + " option!");
		return;
	}
	if (cur.length > limit) {
		alert("Exceed limit! maximum " + limit);
		return;
	}
	$j('#options').val(cur.join(','));
	$j('#vote_form').submit();
}

function resizeResultImage() {
	if ($j('div#result-frame').length) {
		let height = $j('div#result-frame').outerHeight();
		$j('div#result-frame').css('height',height + 'px')
	}
}

function resizeOptionImage() {
	if ($j('img.survey-opt-image').length) {
		$j('img.survey-opt-image').each(function() {
			let width = parseInt($j(this).css('width'));
			$j(this).css('height',(width * 100 / 160) + 'px');
		});
	}
}
