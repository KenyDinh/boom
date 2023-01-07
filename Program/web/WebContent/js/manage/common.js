var $j = jQuery.noConflict();
var mmtSocket = null;
$j(document).ready(function() {
	if ($j('span#socket_url').length) {
		mmtSocket = new BoomSocket($j('span#socket_url').text());
		$j('span#socket_url').remove();
		mmtSocket.init({
			onmessage:onMessage
		});
	}
	$j('form#form-open-menu').on('submit', function(e) {
		e.preventDefault();
		const menu_url = $j(this).find('input#menu-url').val();
		if (mmtSocket != null && mmtSocket.isOpen()) {
			mmtSocket.sendMessage("OPEN_NEW_MENU:" + menu_url);
			$j('#open-menu-modal').modal('hide');
			$j('div#menu-loading').removeClass('d-none');
		}
	});
});

function onMessage(event) {
	let obj = JSON.parse(event.data);
	switch (obj.message) {
	case "menu_open":
		if (!obj.error) {
			window.location.href = window.location.href;
		} else {
			alert(obj.error);
			$j('div#menu-loading').addClass('d-none');
		}
		break;
	case "order_done":
		window.location.reload();
		break;
	default:
		if (obj.message.startsWith("ERROR")) {
			alert(obj.message);
			window.location.reload();
		}
		break;
	}
}

function forceResetState() {
	if (mmtSocket) {
		mmtSocket.sendMessage("FORCE_RESET_STATE");
	}
}
