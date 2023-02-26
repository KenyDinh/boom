$j(document).ready(function() {
	$j('.delete-button').click(function() {
		const mode = $j(this).data('mode') ?? 0;
		if (mode <= 0) {
			return;
		}
		if ($j(this).closest('form').length <= 0) {
			return;
		}
		if ($j(this).closest('form').find('input[name=mode]').length <= 0) {
			return;
		}
		const ret = confirm("Are you sure to delete this record?");
		if (!ret) {
			return;
		}
		$j(this).closest('form').find('input[name=mode]').val(mode);
		$j(this).closest('form').trigger('submit');
	});
	if ($j('#username-field').length) {
		$j('#username-field').autoComplete({
			"minChars" : 2,
			source: function(term, suggest){
				$j.ajax({
					url : CONTEXT + "/sub/search_user.json",
					type : "get",
					data : {"username" : term},
					dataType : "json",
					success : function(ret) {
						if (ret.user_list) {
							suggest(ret.user_list);
						}
					},
					error : function() {
						console.log('error!');
					}
				});
			},
			renderItem : function(item) {
				return '<div class="autocomplete-suggestion" data-id="' + item.id + '" data-username="' + item.username + '">' + item.username + '</div>';
			},
			onSelect : function(event, term, item) {
				$j('#username-field').val(item.data('username'));
			}
		});
	}
});
