FastClick.attach(document.body);
//分享提示
$("#share").click(function(){
    $(".share_tip").show();
});
$(".share_tip").click(function(){
	$(this).hide();
});
$(".dialog_footer a").click(function(){
	$(".dialog").hide();
});
$("#payBtn").click(function(){
	$(".dialog").show();
});
$("#writeCom").click(function(){
	$('html,body').addClass('ovfHiden'); 
	$(".writeCom").addClass("commontAnimShow").removeClass("commontAnimHide");
});
function cancel(){
	$(".writeCom").addClass("commontAnimHide").removeClass("commontAnimShow");
	$('html,body').removeClass('ovfHiden');
}
$("#cancel").click(cancel);
