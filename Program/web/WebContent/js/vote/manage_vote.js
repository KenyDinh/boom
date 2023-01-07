var color_map = null;
var ctx = null;
var vote_chart = null;
var isPhone = false;
var timeout = null;
var survey_id = 0;
$j(document).ready(function() {
	if ($j('table#survey_table').length && $j('td#no-survey-record').length <= 0) {
		$j('table#survey_table').DataTable( {
	        responsive: true,
	        paging:false,
	        searching:false,
	        ordering:false,
	        info:false
	    });
	}
	$j('a.list-question-option').click(function() {
		const id = ($j(this).data('id') || 0)
		const table_option = $j('#question-option-list-' + id);
		$j('div.question-option-list').each(function() {
			if ($j(this).data('id') != id || $j(this).is(':visible')) {
				$j(this).collapse('hide');
			} else {
				$j(this).collapse('show');
			}
		});
	});
	$j('div.question-option-list').on('shown.bs.collapse', function() {
		if ($j(this).hasClass('initialized')) {
			return;
		}
		$j(this).find('table.option-table').first().DataTable( {
	        responsive: true,
	        paging:false,
	        searching:false,
	        ordering:false,
	        info:false
	    });
		$j(this).addClass('initialized');
	});
	if ($j('table#question_table').length && $j('td#no-question-record').length <= 0) {
		$j('table#question_table').DataTable( {
	        responsive: true,
	        paging:false,
	        searching:false,
	        ordering:false,
	        info:false
	    });
	}
	if ($j('table#result_table').length && $j('td#no-result-record').length <= 0) {
		$j('table#result_table').DataTable( {
			responsive: true,
			paging:false,
			searching:false,
			ordering:false,
			info:false
		});
	}
	if ($j('table#rate_table').length && $j('td#no-rate-record').length <= 0) {
		$j('table#rate_table').DataTable( {
			responsive: true,
			paging:false,
			searching:false,
			info:false,
			"columnDefs": [
				{ "type": "num-fmt", "targets": 3 },
				{ "orderable": false, "targets": [0, 1] }
				],
				"order": [[ 2, "desc" ]]
		});
	}
	$j('#delete-button').click(function() {
		let answ = confirm("Are you sure to process this action?");
		if (answ == false) {
			return;
		}
		$j(this).closest('form').append('<input type="hidden" name="delete" value="1" />');
		$j(this).closest('form').submit();
	});
	$j('.delete-record').click(function() {
		let answ = confirm("Are you sure to delete this record?");
		if (answ == false) {
			return;
		}
		$j(this).closest('form').append('<input type="hidden" name="delete" value="1" />');
		$j(this).closest('form').submit();
	});
	$j('select#type').change(function() {
		const val = $j('select#type').val() || 0;
		if ($j('label.label-dynamic').length) {
			$j('label.label-dynamic').each(function() {
				const _labels = $j(this).data('label') || [];
				if (_labels.length > val) {
					$j(this).text(_labels[val]);
				}
			});
		} else if ($j('option.image-included').length) {
			if (val == $j('option.image-included').eq(0).val()) {
				$j('input#content').attr('type','file');
			} else {
				$j('input#content').attr('type','text');
			}
		}
	});
	$j('#show-completed-survey').change(function() {
		if ($j(this).prop('checked')) {
			window.location.href = CONTEXT + "/vote/manage_vote.htm?completed=1";
		} else {
			window.location.href = CONTEXT + "/vote/manage_vote.htm";
		}
	});
	$j('select#type').trigger('change');
});
var isClearResult = false;
function clearResult(user) {
	if (typeof survey_id == "undefined" || survey_id == null || survey_id <= 0) {
		return;
	}
	if (typeof user == "undefined" || user == null || user.length == 0) {
		return;
	}
	let answ = confirm("Confirm clear up result?");
	if (answ == false) {
		return;
	}
	if (isClearResult) {
		return;
	}
	isClearResult = true;
	$j.ajax({
		type:"POST",
		url:CONTEXT + "/vote/vote_data_loader.json?survey_id=" + survey_id + "&mode=clear&user=" + user,
		dataType:"json",
		success: function(result) {
			if (result.success) {
				loadResultDetail();
				loadVoteResult();
				isClearResult = false;
			}
		},
		error:function() {
			console.log("error!");
			isClearResult = false;
		}
	});
}