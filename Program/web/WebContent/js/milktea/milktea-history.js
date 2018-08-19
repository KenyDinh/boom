
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
});