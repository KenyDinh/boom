var $j = jQuery.noConflict();
$j(document).ready(function(e) {
	if ($j('#login-form-modal').length) {
		$j('#login-form-modal').modal('show');
	}
	if ($j('.survey-opt-image').length) {
		resizeOptionImage();
	}
	$j(window).on('resize', function() {
		resizeOptionImage();
		//resizeResultImage();
	});
	$j('.date-picker').datepicker({dateFormat:"yy/mm/dd"});
	$j('div[class*="option-select-"]').click(function(e) {
		if ($j(e.target).hasClass('hover-image')) {
			return;
		}
		let checkedElem = $j(this).find('div.option-checked');
		if (checkedElem.length) {
			let id = $j(this).attr('data').replace('option-','');
			let limit = ($j('#btn-submit-vote').data('max') || 0);
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
				$j(this).find('.survey-opt-image').removeClass('check');
			} else {
				let cur = getSelectedOption();
				if (cur.length >= limit) {
					alert("Cannot select more than " + limit + " option!");
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
				$j(this).find('.survey-opt-image').addClass('check');
			}
		}
	});
	$j('.text-answer').on('change keyup paste', function() {
		const textCount = $j(this).parent('div').find('.char-count');
		if (textCount.length <= 0) {
			return;
		}
		const limit = ($j('#btn-submit-vote').data('max') || 0);
		const minimum = ($j('#btn-submit-vote').data('min') || 0);
		if (limit <= 0) {
			return;
		}
		const length = ($j(this).val() || "").length;
		if (length <= 0) {
			textCount.text('');
			return;
		} else if (length <= limit && length >= minimum) {
			textCount.text(length + " / " + limit);
		} else {
			textCount.html('<span class="text-danger font-weight-bold">' + length + '</span>&nbsp;/&nbsp;' + limit);
		}
	});
	$j('#btn-submit-vote').click(function() {
		const form = $j('#vote_form');
		if (form.length <= 0) {
			return;
		}
		form.trigger('reset');
		const textAnswer = $j('textarea.text-answer');
		const yesNoQuestion = $j('input[name=yes_no]');
		const selectAnswer = $j('select.select-answer');
		const datePicker = $j('input.date-picker');
		const limit = $j(this).data('max');
		const minimum = $j(this).data('min');
		let options = '';
		if (textAnswer.length) {
			options = (textAnswer.val() || "");
			const t_length = options.length;
			if (t_length > limit || t_length < minimum) {
				alert("Your answer must contain " + minimum + " to " + limit + " character(s)!");
				return;
			}
		} else if (yesNoQuestion.length) {
			options = ($j('input[name=yes_no]:checked').val() || "");
		} else if (selectAnswer.length) {
			options = (selectAnswer.val() || "");
		} else if (datePicker.length) {
			options = (datePicker.val() || "");
		} else {
			let cur = getSelectedOption();
			if (cur.length < minimum) {
				//$j('#error').text('Please choose at least 1 option!');
				alert("Please choose at least " + minimum + " option(s)!");
				return;
			}
			if (cur.length > limit) {
				alert("You cannot select more than " + limit + " option(s)!");
				return;
			}
			options = cur.join(',');
		}
		form.find('#options').val(options);
		form.submit();
	});
//	$j('div.survey-option > .survey-opt-image').hover(function(e) {
//		if($j('div#opt-image-overlay').length) {
//			return;
//		}
//		const hoverImage = new Image();
//		const windowWidth = $j(window).outerWidth();
//		hoverImage.src = $j(this).attr('src');
//		hoverImage.onload = () => {
//			const imgWidth = hoverImage.width;
//			const imgHeight = hoverImage.height;
//			let drawWidth = windowWidth / 4;
//			let drawHeight = (drawWidth * imgHeight) / imgWidth;
//			let top = e.clientY;
//			let left = e.clientX;
//			if (top > drawHeight) {
//				top -= drawHeight;
//				top += 10;
//			} else {
//				top = Math.max(0, top - 10);
//			}
//			left = Math.max(0, left - 10);
//			$j(this).parent().append('<div id="opt-image-overlay" style="display:none;position:fixed;z-index:99;top:' + top + 'px;left:' + left + 'px;max-width:' + drawWidth + 'px"><img class="hover-image" src="' + hoverImage.src + '" style="cursor:pointer;width:100%;" /></div>');
//			$j('div#opt-image-overlay').show();
//		}
//	});
	$j('div.survey-option').on('mouseleave', function() {
		if ($j('div#opt-image-overlay').length) {
			$j('div#opt-image-overlay').remove();
		}
	});
	$j('body').on('click', 'div#opt-image-overlay', function() {
		if ($j('#imagemodal').length) {
			const previewImage = new Image();
			previewImage.src = $j(this).find('img').first().attr('src');
			previewImage.onload = () => {
				const imgWidth = previewImage.width;
				const imgHeight = previewImage.height;
				$j('#imagemodal').find('.modal-dialog').css('max-width',(imgWidth + 32) + 'px');
				$j('#imagemodal').find('.imagepreview').attr('src',previewImage.src);
				$j('#imagemodal').modal('show');
			}
		}
	})
	$j('div.mystery-box').on('click', function() {
		const _this = $j(this);
		if (_this.hasClass('load')) {
			return;
		}
		$j('div.mystery-box').addClass('load');
		const path = $j('#vote_form input[name="p"]').val();
		const id = _this.data('id');
		if (!path || !id) {
			$j('div.mystery-box').removeClass('load');
			return;
		}
		$j.ajax({
			url: CONTEXT + "/vote/vote_confirm.json",
			type:"post",
			data:{
				"p" : path,
				"ans" : id
			},
			dataType:"json",
			success:function(ret) {
				const list = ret.reward_id;
				if (!list || !list.length || list.length < id) {
					return;
					$j('div.mystery-box').removeClass('load');
				}
				_this.find('img').eq(0).animate({
					opacity: 0
				}, 1000, "swing", function() {
					_this.empty();
					_this.append('<span>' + list[id - 1] + '</span>').addClass('active');
					$j('div.mystery-box img').animate({
						opacity: 0
					}, 1000, "swing", function() {
						const _id = $j(this).parent().data('id') || 0;
						if (_id != 0) {
							$j(this).parent().empty().append('<span>' + list[_id - 1] + '</span>');
						}
					});
				});
			},
			error:function() {
				console.log("error!");
				$j('div.mystery-box').removeClass('load');
			}
		});
		
	});
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
	if ($j('.survey-opt-image').length) {
		$j('.survey-opt-image').each(function() {
			let width = parseInt($j(this).css('width'));
			$j(this).css('height',(width * 100 / 160) + 'px');
		});
	}
}
