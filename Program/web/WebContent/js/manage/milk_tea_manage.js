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
});

function confirmDeleteOrder(obj) {
	if (typeof obj == "undefined") {
		return;
	}
	let answ = confirm("Confirm delete order?");
	if (answ == false) {
		return;
	}
	let _form = $j(obj).closest('form');
	if (_form.length) {
		_form.append('<input type="hidden" name="mode" value="Delete" />');
		_form.submit();
		return;
	}
	alert("Form not found!");
}