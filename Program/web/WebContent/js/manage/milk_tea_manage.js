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
	$j('input#socket_test').click(function() {
		if (mmtSocket != null && mmtSocket.isOpen()) {
			mmtSocket.sendMessage("PREPARING_ORDER:3,4,5,6,7");
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
	
}