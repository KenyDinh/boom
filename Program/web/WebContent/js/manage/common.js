var $j = jQuery.noConflict();
$j(document).ready(function() {
	initToolTip();
});

function initToolTip() {
	$j('[data-toggle="tooltip"]').tooltip();
}