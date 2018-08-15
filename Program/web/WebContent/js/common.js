var $j = jQuery.noConflict();
$j(document).ready(function() {
	$j('a#logout').click(function() {
		if ($j('form#logout-form').length) {
			$j('form#logout-form').trigger('submit');
		}
	});
	initToolTip();
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