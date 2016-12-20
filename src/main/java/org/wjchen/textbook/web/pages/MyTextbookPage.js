/**
 * 
 */
$(document).ready(function() {
	$('a[data-toggle="collapse"]').click(function () {
		$(this).find('span.toggle-icon').toggleClass('glyphicon-collapse-up glyphicon-collapse-down');
	})
});

