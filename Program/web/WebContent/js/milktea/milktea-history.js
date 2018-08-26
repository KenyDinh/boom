
$j(document).ready(function() {
	if ($j('table#order-history-table').length) {
		$j('table#order-history-table').DataTable( {
	        responsive: true,
	        "columnDefs": [
	            { "type": "num-fmt", "targets": [1,2,5] },
	            { "orderable": false, "targets": 3 }
	        ],
	        "order": [[ 6, "desc" ]]
	    });
	}
	$j('span.order-star').click(function() {
		let order_id = parseInt($j(this).attr('id').replace('order-star-','').split('-')[0]);
		let index = parseInt($j(this).attr('id').replace('order-star-','').split('-')[1]);
		let check = $j(this).hasClass('text-success');
		for (let i = 0; i < index; i++) {
			let subElem = $j('span#order-star-' + order_id + '-' + (i + 1));
			let subcheck = subElem.hasClass('text-success');
			if (subcheck == false) {
				subElem.addClass('text-success');
			}
		}
		for (let i = index; ; i++) {
			let subElem = $j('span#order-star-' + order_id + '-' + (i + 1));
			if (subElem.length) {
				subElem.removeClass('text-success');
			} else {
				break;
			}
		}
	});
});

function upvoteTheOrder(orderId) {
	if (orderId == undefined || orderId == null) {
		return;
	}
	var votingOrderModal = $j('div#voting-up-order-' + orderId);
	if (votingOrderModal.length <= 0) {
		return;
	}
	let stars = 0;
	$j('span[id^="order-star-' + orderId + '-"]').each(function() {
		if ($j(this).hasClass('text-success')) {
			stars = Math.max( stars, parseInt( $j(this).attr('id').replace('order-star-' + orderId + '-','') ) );
		}
	});
	if (stars <= 0) {
		return;
	}
	$j.ajax({
		url: CONTEXT + "/milktea/milk_tea_manage_order.htm",
		type:"POST",
		dataType:"json",
		data:"mode=3&order_id=" + orderId + "&star=" + stars,
		success:function(result) {
			if (result.data) {
				let data = $j(result.data);
				let elem = $j('#' + data.attr('id'));
				if (elem.length) {
					votingOrderModal.on('hide.bs.modal', function(e) {
						elem.html(data);
						votingOrderModal.remove();
					});
				}
				votingOrderModal.modal('hide');
			} else {
				votingOrderModal.modal('hide');
			}
		},
		error:function() {
			window.location.reload();
		}
	});
}