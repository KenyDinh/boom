
$j(document).ready(function() {
	if ($j('table#milktea-ranking-table').length && $j('td#no-data').length <= 0) {
		if ($j('span#king_of_chasua').length) {
			$j('table#milktea-ranking-table').DataTable( {
				responsive: true,
				"ordering": false
			});
		} else {
			$j('table#milktea-ranking-table').DataTable( {
				responsive: true,
				"columnDefs": [
					{ "type": "num-fmt", "targets": [2,3,4] },
					{ "orderable": false, "targets": 0 }
					],
					"order": [[ 2, "desc" ]]
			});
		}
		//new $j.fn.dataTable.FixedHeader( $j('table#milktea-ranking-table') );
	}
});