var $j = jQuery.noConflict();
$j(document).ready(function(e) {
	$j('div[class*="option-select-"]').click(function() {
		let checkedElem = $j(this).find('div.option-checked');
		if (checkedElem.length) {
			let id = $j(this).attr('data').replace('option-','');
			if (checkedElem.is(':visible')) {
				let rsElem = $j('div.option-result-' + id);
				if (rsElem.length) {
					rsElem.addClass('result-empty');
					rsElem.html('');
				}
				checkedElem.hide();
				$j(this).find('img.survey-opt-image').removeClass('check');
			} else {
				let limit = parseInt($j('#max_choice').text());
				let cur = getSelectedOption();
				if (cur.length >= limit) {
					alert("exceed limit!");
					return;
				}
				let rsEmptyList = $j('div.result-empty');
				if (rsEmptyList.length) {
					let rsElem = rsEmptyList.eq(0);
					rsElem.removeClass('result-empty');
					let obj = $j(this).clone();
					obj.removeClass('option-select-' + id).addClass('option-result-' + id);
					rsElem.html(obj);
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
	console.log(JSON.stringify(ret));
	return ret;
}
function sendVote() {
	let limit = parseInt($j('#max_choice').text());
	let cur = getSelectedOption();
	if (cur.length <= 0) {
		//$j('#error').text('Please choose at least 1 option!');
		alert("Please choose at least 1 option!");
		return;
	}
	if (cur >= limit) {
		alert("exceed limit!");
		return;
	}
	$j('#options').val(cur.join(','));
	$j('#vote_form').submit();
}
