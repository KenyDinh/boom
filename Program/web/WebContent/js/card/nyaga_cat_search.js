var table;

$j(document).ready(function() {
	if ($j('table#cat-seacrh-table').length && $j('td#no-data').length <= 0) {
		table = $j('table#cat-seacrh-table').DataTable({
			"responsive" : true,
			"paging": false,
			"searching": false,
			"info": false,
			"columnDefs": [
				{ "orderable": false, "targets": [2,5,6,7,10] }
			]
		});
	}
	
	$j('div#cat-filter-form button.cat-filter').click(function() {
		if ($j(this).hasClass('btn-info')) {
			$j(this).removeClass('btn-info').addClass('btn-secondary');
		} else {
			$j(this).removeClass('btn-secondary').addClass('btn-info');
		}
	});
	
	$j('button#filter-button').click(function() {
		let filterModel = $j('div#cat-filter-modal');
		if (filterModel.length) {
			reinitFilter();
			filterModel.modal('show');
		}
	});
	$j('button#clear-button').click(function() {
		clearFilter();
	});
	$j('button#filter-card').click(function() {
		applyFilter();
		$j('form#search-form').submit();
	});
//	$j('button#filter-close').click(function() {
//		applyFilter();
//	});
});

function applyFilter() {
	let elem = 0, rank = 0, star = 0, cost = 0, job = 0, miltype = 0;
	$j('div#cat-filter-form button.cat-filter.btn-info').each(function() {
		let data = $j(this).attr('data');
		if (typeof data !== "undefined" && data.length > 0) {
			if (data.startsWith('elem')) {
				elem |= parseInt(data.replace('elem-',''));
			} else if (data.startsWith('rank')) {
				rank |= parseInt(data.replace('rank-',''));
			} else if (data.startsWith('star')) {
				star |= parseInt(data.replace('star-',''));
			} else if (data.startsWith('cost') ) {
				cost |= parseInt(data.replace('cost-',''));
			} else if (data.startsWith('job')) {
				job |= parseInt(data.replace('job-',''));
			} else if (data.startsWith('miltype')) {
				miltype |= parseInt(data.replace('miltype-',''));
			}
		}
	});
	$j('form#search-form > input[name="element"]').val(elem);
	$j('form#search-form > input[name="rank"]').val(rank);
	$j('form#search-form > input[name="star"]').val(star);
	$j('form#search-form > input[name="cost"]').val(cost);
	$j('form#search-form > input[name="job"]').val(job);
	$j('form#search-form > input[name="miltype"]').val(miltype);
}

function reinitFilter() {
	let elem = $j('form#search-form > input[name="element"]').attr('value');
	let cost = $j('form#search-form > input[name="cost"]').attr('value');
	let rank = $j('form#search-form > input[name="rank"]').attr('value');
	let star = $j('form#search-form > input[name="star"]').attr('value');
	let job = $j('form#search-form > input[name="job"]').attr('value'); 
	let miltype = $j('form#search-form > input[name="miltype"]').attr('value');
	
	$j('div#cat-filter-form button.cat-filter').each(function() {
		if ($j(this).hasClass('btn-info')) {
			$j(this).removeClass('btn-info').addClass('btn-secondary');
		}
		let data = $j(this).attr('data');
		if (typeof data !== "undefined" && data.length > 0) {
			let on = false;
			if (data.startsWith('elem')) {
				if ((elem & parseInt(data.replace('elem-',''))) != 0) {
					on = true;
				}
			} else if (data.startsWith('rank')) {
				if ((rank & parseInt(data.replace('rank-',''))) != 0) {
					on = true;
				}
			} else if (data.startsWith('star')) {
				if ((star & parseInt(data.replace('star-',''))) != 0) {
					on = true;
				}
			} else if (data.startsWith('cost')) {
				if ((cost & parseInt(data.replace('cost-',''))) != 0) {
					on = true;
				}
			} else if (data.startsWith('job')) {
				if ((job & parseInt(data.replace('job-',''))) != 0) {
					on = true;
				}
			} else if (data.startsWith('miltype')) {
				if ((miltype & parseInt(data.replace('miltype-',''))) != 0) {
					on = true;
				}
			}
			if (on) {
				$j(this).removeClass('btn-secondary').addClass('btn-info');
			}
		}
	});
}

function clearFilter() {
	$j('form#search-form input[name="id"]').val('');
	$j('form#search-form input[name="disp"]').val('');
	$j('form#search-form input[name="fname"]').val('');
	$j('form#search-form > input[name="element"]').val(0);
	$j('form#search-form > input[name="rank"]').val(0);
	$j('form#search-form > input[name="star"]').val(0);
	$j('form#search-form > input[name="cost"]').val(0);
	$j('form#search-form > input[name="job"]').val(0);
	$j('form#search-form > input[name="miltype"]').val(0);
	
	$j('div#cat-filter-form button.cat-filter.btn-info').each(function() {
		$j(this).removeClass('btn-info').addClass('btn-secondary');
	});
}
