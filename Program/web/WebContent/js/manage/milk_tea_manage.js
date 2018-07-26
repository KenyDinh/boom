var mmtSocket = null;
$j(document).ready(function() {
	$j('input#check-all-order').change(function() {
		if ($j(this).is(':checked')) {
			$j('input.checkbox-order').prop('checked',true);
		} else {
			$j('input.checkbox-order').prop('checked',false);
		}
	});
	$j('input.checkbox-order').change(function() {
		let isAllchecked = true;
		$j('input.checkbox-order').each(function() {
			if ($j(this).is(':checked') == false) {
				isAllchecked = false;
			}
		});
		$j('input#check-all-order').prop('checked',isAllchecked);
	});
//	$j('input#socket_test').click(function() {
//		if (mmtSocket != null && mmtSocket.isOpen()) {
//			mmtSocket.sendMessage("PREPARING_ORDER:3,4,5,6,7");
//			$j('#waiting-order-modal').modal({backdrop: "static"});
//		}
//	});
	$j('button#place_order').click(function() {
		if (mmtSocket != null && mmtSocket.isOpen()) {
			let order_list = "";
			$j('input.checkbox-order').each(function() {
				if ($j(this).is(':checked')) {
					if (order_list.length > 0) {
						order_list += ",";
					}
					order_list += $j(this).attr('value');
				}
			});
			if (order_list.length == 0) {
				return;
			}
			mmtSocket.sendMessage("PREPARING_ORDER:" + order_list);
			$j('#waiting-order-modal').modal({backdrop: "static"});
		}
	});
	if ($j('span#socket_url').length) {
		mmtSocket = new BoomSocket($j('span#socket_url').text());
		$j('span#socket_url').remove();
		mmtSocket.init({
			onmessage:onMessage
		});
	}
});

function onMessage(event) {
	let message = event.data;
	switch (message) {
	case "order_done":
		window.location.reload(); // ? ajax
//		$j('#waiting-order-modal').on('hide.bs.modal' ,function(e) {
//		});
//		$j('#notify').text("OK already!, click [x] to close this popup!");
//		$j('#close-modal').show();
		break;
	default:
		break;
	}
}

function forceResetState() {
	if (mmtSocket) {
		mmtSocket.sendMessage("FORCE_RESET_STATE");
	}
}