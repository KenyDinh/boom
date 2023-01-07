
$j(document).ready(function() {
	if ($j('table#order-history-table').length) {
		$j('table#order-history-table').DataTable( {
			responsive: true,
			"columnDefs": [
				//{ "type": "num-fmt", "targets": [2,3,6] },
				{ "orderable": false, "targets": 4 }
				],
				"order": [[ 7, "desc" ]]
		});
	}
	$j('#order-history-table,#order-list').on('click', 'span.vote-up-order', function() {
		const id = $j(this).data('id');
		const name = $j(this).data('name');
		const star = $j(this).data('star');
		let comment = "";
		if ($j('span#order-comment-' + id).length) {
			comment = $j('span#order-comment-' + id).text();
		}
		const list = $j('#voting-up-order-form span.order-star');
		for (let i = 0; i < list.length; i++) {
			if (star > 0) {
				list.eq(i).addClass('lock');
			} else {
				list.eq(i).removeClass('lock');
			}
			if (list.eq(i).data('index') <= star) {
				list.eq(i).addClass('text-success');
			} else {
				list.eq(i).removeClass('text-success');
			}
		}
		$j('#voting-up-order-form').trigger('reset');
		$j('#voting-up-order-form #vote-order-id').val(id);
		$j('#voting-up-order-form #vote-order-name').html(name);
		if ($j(this).hasClass('commented')) {
			$j('#voting-up-order-form #vote-comment').attr('readonly','readonly');
			$j('#voting-up-order-form #btn-up-vote').hide();
		} else {
			$j('#voting-up-order-form #vote-comment').removeAttr('readonly');
			$j('#voting-up-order-form #btn-up-vote').show();
		}
		$j('#voting-up-order-form #vote-comment').val(comment);
		$j('#voting-up-order').modal('show');
	});
	$j('#voting-up-order-form span.order-star').click(function() {
		if ($j(this).hasClass('lock')) {
			return;
		}
		const index = $j(this).data('index');
		const check = $j(this).hasClass('text-success');
		const list = $j('#voting-up-order-form span.order-star');
		for (let i = 0; i < list.length; i++) {
			if (list.eq(i).data('index') <= index) {
				list.eq(i).addClass('text-success');
			} else {
				list.eq(i).removeClass('text-success');
			}
		}
	});
	$j('#btn-up-vote').click(function() {
		const order_id = $j('#voting-up-order-form #vote-order-id').val() || 0;
		if (order_id <= 0) {
			return;
		}
		let stars = 0;
		const list = $j('#voting-up-order-form span.order-star');
		let lock = false;
		for (let i = 0; i < list.length; i++) {
			if (list.eq(i).hasClass('text-success')) {
				stars++;
			}
			if (list.eq(i).hasClass('lock')) {
				lock = true;
			}
		}
		if (stars <= 0) {
			return;
		}
		const comment = $j('#voting-up-order-form #vote-comment').val() || "";
		if (lock && comment.length <= 0) {
			return;
		}
		const votingOrderModal = $j('#voting-up-order');
		if (votingOrderModal.length <= 0) {
			return;
		}
		$j.ajax({
			url: CONTEXT + "/milktea/milk_tea_manage_order.htm",
			type:"POST",
			dataType:"json",
			data:{
				"mode" : 3,
				"order_id" : order_id,
				"star" : stars,
				"comment" : comment
			},
			success:function(result) {
				if (result.data) {
					let data = $j(result.data);
					let elem = $j('#' + data.attr('id'));
					if (elem.length) {
						votingOrderModal.on('hide.bs.modal', function(e) {
							elem.html(data);
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
	});
});