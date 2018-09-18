var $j = jQuery.noConflict();
$j(document).ready(function(e){
	$j(".img-check").click(function(){
		$j(this).toggleClass("check");
	});
});
function sendVote() {
	let listOption = $j('input[name="option"]:checked');
	let count = 0;
	if (listOption.length) {
		let options = "";
		for (let i = 0; i < listOption.length; i++) {
			if (options.length > 0) {
				options += ",";
			}
			options += listOption.eq(i).attr('value');
			++count;
		}
		let maxChoice = parseInt($j('span#maxChoice').text());
		if (count <= 0) {
			$j('#error').text('Please choose at least 1 option!');
			return;
		}
		if (count > maxChoice) {
			$j('#error').text('You can only choose max 3 options!');
			return;
		}
		$j('#options').val(options);
		$j('#vote_form').submit();
		return;
	}
}