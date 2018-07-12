$(document).ready(function() {
	$('a#logout').click(function() {
		if ($('form#logout-form').length) {
			$('form#logout-form').trigger('submit');
		}
	});
	$('[data-toggle="tooltip"]').tooltip();
});
function onclickLogin() {
	if ($('div#login-form-modal').length) {
		var username = document.getElementById('form-login-username');
		var password = document.getElementById('form-login-password');
		if (username && username.checkValidity() && password && password.checkValidity()) {
			$.ajax({
				type:"POST",
				url:CONTEXT + "/account/login.htm",
				data:"username=" + username.value + "&password=" + password.value,
				success:function(result) {
					if (result) {
						var ret = JSON.parse(result);
						if (ret.success == 1) {
							$('div#login-form-modal').modal('hide');
							window.location.reload();
						} else if (ret.error) {
							$('div#login-message').html(ret.error);
						}
					}
				},
				error:function() {
					console.log("login error!");
				}
			});
		}
	}
}
function onclickRegist() {
	if ($('div#regist-form-modal').length) {
		var username = document.getElementById('form-regist-username');
		var password = document.getElementById('form-regist-password');
		var re_password = document.getElementById('form-regist-re-password');
		if (username && username.checkValidity() && password && password.checkValidity() && re_password && re_password.checkValidity()) {
			$.ajax({
				type:"POST",
				url:CONTEXT + "/account/register.htm",
				data:"username=" + username.value + "&password=" + password.value + "&re_password=" + re_password.value,
				success:function(result) {
					if (result) {
						var ret = JSON.parse(result);
						if (ret.success == 1) {
							$('div#regist-form-modal').modal('hide');
							window.location.reload();
						} else if (ret.error) {
							$('div#regist-message').html(ret.error);
						}
					}
				},
				error:function() {
					console.log("regist failed!");
				}
			});
		}
	}
}