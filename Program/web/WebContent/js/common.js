var $j = jQuery.noConflict();
var countdown = null;
$j(document).ready(function() {
	$j('a#logout').click(function() {
		if ($j('form#logout-form').length) {
			$j('form#logout-form').trigger('submit');
		}
	});
	initToolTip();
	initCountdown();
});

function initToolTip() {
	$j('[data-toggle="tooltip"]').tooltip();
}

function isValidData(data) {
	if (data == undefined || data === null) {
		return false;
	}
	return true;
}
function onclickLogin() {
	if ($j('div#login-form-modal').length) {
		var username = document.getElementById('form-login-username');
		var password = document.getElementById('form-login-password');
		if (!isValidData(username.value) || username.value.length == 0 || !isValidData(password.value) || password.value.length == 0) {
			$j('div#login-message').html("Please input the valid datas!");
		}
		if (username.checkValidity() && password.checkValidity()) {
			$j.ajax({
				type:"POST",
				url:CONTEXT + "/account/login.htm",
				dataType:"json",
				data:"username=" + encodeURIComponent(username.value) + "&password=" + encodeURIComponent(password.value),
				success:function(result) {
					if (result) {
						if (result.success == 1) {
							$j('div#login-form-modal').modal('hide');
							window.location.reload();
						} else if (result.error) {
							$j('div#login-message').html(result.error);
						}
					} else {
						window.location.reload();
					}
				},
				error:function() {
					window.location.reload();
				}
			});
		} else {
			$j('div#login-message').html("Please input the valid datas!");
		}
	}
}
function onclickRegist() {
	if ($j('div#regist-form-modal').length) {
		var username = document.getElementById('form-regist-username');
		var password = document.getElementById('form-regist-password');
		var re_password = document.getElementById('form-regist-re-password');
		if (!isValidData(username.value) || username.value.length == 0 || !isValidData(password.value) || password.value.length == 0 || 
				!isValidData(re_password.value) || re_password.value.length == 0) {
			$j('div#regist-message').html("Please input the valid datas!");
		}
		if (username.checkValidity() && password.checkValidity() && re_password.checkValidity()) {
			$j.ajax({
				type:"POST",
				url:CONTEXT + "/account/register.htm",
				dataType:"json",
				data:"username=" + encodeURIComponent(username.value) + "&password=" + encodeURIComponent(password.value) + "&re_password=" + encodeURIComponent(re_password.value),
				success:function(result) {
					if (result) {
						if (result.success == 1) {
							$j('div#regist-form-modal').on('hide.bs.modal', function(e) {
								$j('div#login-form-modal').modal('show');
							});
							$j('div#regist-form-modal').modal('hide');
						} else if (result.error) {
							$j('div#regist-message').html(result.error);
						}
					} else {
						window.location.reload();
					}
				},
				error:function() {
					window.location.reload();
				}
			});
		} else {
			$j('div#regist-message').html("Please input the valid datas!");
		}
	}
}

function onclickChangePassword() {
	if ($j('div#change-password-modal').length) {
		var cur_pwd = document.getElementById('form-cp-current-password');
		var new_pwd = document.getElementById('form-cp-new-password');
		var re_new_pwd = document.getElementById('form-cp-re-new-password');
		if (!isValidData(cur_pwd.value) || cur_pwd.value.length == 0 || !isValidData(new_pwd.value) || new_pwd.value.length == 0 || 
				!isValidData(re_new_pwd.value) || re_new_pwd.value.length == 0) {
			$j('div#change-pwd-message').html("Please input the valid datas!");
		}
		if (cur_pwd.checkValidity() && new_pwd.checkValidity() && re_new_pwd.checkValidity()) {
			$j.ajax({
				type:"POST",
				url:CONTEXT + "/account/change_password.htm",
				dataType:"json",
				data:"cur_password=" + encodeURIComponent(cur_pwd.value) + "&new_password=" + encodeURIComponent(new_pwd.value) + "&re_new_password=" + encodeURIComponent(re_new_pwd.value),
				success:function(result) {
					if (result) {
						if (result.success == 1) {
							$j('div#change-password-modal').modal('hide');
						} else if (result.error) {
							$j('div#change-pwd-message').html(result.error);
						}
					} else {
						window.location.reload();
					}
				},
				error:function() {
					window.location.reload();
				}
			});
		} else {
			$j('div#change-pwd-message').html("Please input the valid datas!");
		}
	}
}

function initCountdown() {
	let cdList = $j('.countdown-timer');
	if (cdList.length) {
		for (let i = 0; i < cdList.length; i++) {
			let elem = cdList.eq(i);
			let id = elem.attr('id');
			if (id) {
				let time = parseFullTime(elem.text());
				let parent_id = elem.attr('data-parent');
				if (time <= 0) {
					if (parent_id && $j(parent_id).length) {
						$j(parent_id).hide();
					}
					continue;
				}
				if (countdown == null) {
					countdown = [];
				}
				id = "#" + id;
				let obj = {
					key:id,
					time:time
				}
				if (parent_id && $j(parent_id).length) {
					obj.parent = parent_id;
				}
				if (elem.attr('data-reload')) {
					obj.reload = true;
				} else {
					obj.reload = false;
				}
				countdown.push(obj);
			}
		}
		window.setTimeout(startCountdown, 1000);
	}
}

function startCountdown() {
	if (countdown == null || countdown.length == 0) {
		return;
	}
	for (let i = 0; i < countdown.length; i++) {
		let obj = countdown[i];
		obj.time--;
		if (obj.time <= 0) {
			if (obj.reload) {
				window.location.reload();
				return;
			}
			$j(obj.parent).hide();
		} else {
			let str_time = formatFullTime(obj.time);
			$j(obj.key).text(str_time);
		}
	}
	window.setTimeout(startCountdown, 1000);
}

function parseFullTime(str_time) {
	if (str_time === null || str_time === undefined) {
		return 0;
	}
	if (/^\d{2}:\d{2}:\d{2}$/.test(str_time) == false) {
		return false;
	}
	let arr = str_time.split(':');
	let time = 0;
	time += parseInt(arr[0]) * 3600;
	time += parseInt(arr[1]) * 60;
	time += parseInt(arr[2]);
	return time;
}

function formatFullTime(time) {
	let h = parseInt(time/3600);
	time = time - 3600 * h;
	if (h < 10) {
		h = "0" + h;
	}
	let m = parseInt(time/60);
	time = time - 60 * m;
	if (m < 10) {
		m = "0" + m;
	}
	let s = time;
	if (s < 10) {
		s = "0" + s;
	}
	return h + ":" + m + ":" + s;
}