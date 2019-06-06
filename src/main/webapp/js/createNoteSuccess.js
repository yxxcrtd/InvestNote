	FastClick.attach(document.body);
	//分享提示
	$("#share").click(function() {
		$(".share_tip").show();
	});
	$(".share_tip").click(function() {
		$(this).hide();
	});