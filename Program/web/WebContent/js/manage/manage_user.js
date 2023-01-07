$j(document).ready(function() {
	if ($j('table#user-table').length && $j('td#no-data').length <= 0) {
		$j('table#user-table').DataTable( {
			responsive: false,
			"ordering": false
		});
	}
});