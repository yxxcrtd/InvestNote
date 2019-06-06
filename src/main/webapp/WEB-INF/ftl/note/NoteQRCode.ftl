<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="edge,chrome=1" />
		<link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/pcerCode.css?version=${version}" />
		<title></title>
	</head>
	<body>
		<div class="page-content">
			<div class="head">
				<div class="head_cont clearfix">
					<p class="mask"></p>
					<img src="${staticUrl}${request.contextPath}/weixin/note/img/pc/logo.png" alt="logo"/>
					<span class="head_title">
						投资笔记
					</span>
					<span class="head_Detail">
						最赚钱的投资者<br/>
						都&emsp;在&emsp;这&emsp;里
					</span>
					<!--<span class="tel">400-8888-8888</span>-->
				</div>
			</div>
			<div class="content">
				<div class="phone">
					<i></i>
					<span></span>
				</div>
				<div class="content_phone">
					<div class="content_head clearfix">
						<img src="${user.user_header_img?substring(0, user.user_header_img?last_index_of("/0")) + "/96"}" alt="头像"/>
						<dl class="historyDetail">
							<dt>${user.user_nickname}</dt>
							<dd><span>历时笔记${user.user_note_count}篇</span><span class="mgl">成功率${user.user_success}%</span><span class="mgl">收益率${user.user_yield}%</span></dd>
						</dl>	
					</div>
					<div class="detail">
						<span><em>${note.note_stock_code}</em><br/>股票代码</span><span><em>${note.note_increase}%</em><br/>目标涨幅</span><span><em>${note.note_target_day}日</em><br/>实现时间</span>
					</div>
					<!--<p class="ercode"><img src="/home/www/html/avatars/qrcode/${note.note_id}.jpg"/><br/>-->	
					<!--<p class="ercode"><img src="/upload/${note.note_id}.jpg"/><br/>-->
					<p class="ercode"><img src="data:image/jpg;base64,${imageBase64QRCode}"/><br/>
					微信扫码
					</p>			
				</div>
				<p class="phone_footer">
					<span></span>
				</p>
				<div class="pageCont_title">
					${user.user_nickname}
					<br/>
					写了一篇投资笔记
					<p>微信扫码查看投资逻辑</p>
				</div>
			</div>
			</div>
			
	</body>
</html>
   