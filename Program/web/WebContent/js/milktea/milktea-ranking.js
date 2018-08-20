
$j(document).ready(function() {
	if ($j('table#milktea-ranking-table').length) {
		$j('table#milktea-ranking-table').DataTable( {
	        responsive: true,
	        "columnDefs": [
	            { "type": "num-fmt", "targets": [2,3,4] },
	            { "orderable": false, "targets": 0 }
	        ],
	        "order": [[ 2, "desc" ]]
	    });
		//new $j.fn.dataTable.FixedHeader( $j('table#milktea-ranking-table') );
	}
});