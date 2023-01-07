$j(document).ready(function() {
	if ($j('img.game-menu-image').length) {
		resizeGameImageMenu();
		$j(window).on('resize', function() {
			resizeGameImageMenu();
		});
	}
});
function resizeGameImageMenu() {
	$j('img.game-menu-image').each(function() {
		let width = parseInt($j(this).css('width'));
		$j(this).css('height',(width * 512 / 512) + 'px');
	});
}