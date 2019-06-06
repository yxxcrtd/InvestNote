<#include "config.ftl" />
<meta charset="utf-8">
<meta name="format-detection" content="telephone=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link rel="stylesheet" href="${staticUrl}${request.contextPath}/weixin/note/css/public.css?version=${version}" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0"/>
<script type="text/javascript" src="${staticUrl}${request.contextPath}/weixin/note/js/jquery-1.11.1.min.js?version=${version}"></script>
<script type="text/javascript" src="${staticUrl}${request.contextPath}/weixin/note/js/flexible.js?version=${version}"></script>
<script type="text/javascript" src="${staticUrl}${request.contextPath}/weixin/note/js/zepto.min.js?version=${version}"></script>
<script type="text/javascript" src="${staticUrl}${request.contextPath}/weixin/note/js/fastclick.js?version=${version}"></script>
<script type="text/javascript" src="${staticUrl}${request.contextPath}/weixin/note/js/public.js?version=${version}"></script>
<script src="//res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<title>投资笔记</title>


<script>
    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: '${jsApiTicket.appId}', // 必填，公众号的唯一标识
        timestamp: ${jsApiTicket.timestamp?default(0)}, // 必填，生成签名的时间戳
        nonceStr: '${jsApiTicket.nonceStr}', // 必填，生成签名的随机串
        signature: '${jsApiTicket.signature}',// 必填，签名，见附录1
        jsApiList: [
            'onMenuShareTimeline',  // 分享到朋友圈
            'onMenuShareAppMessage',    // 分享给朋友
            'onMenuShareWeibo',    // 分享到微博
            'onMenuShareQQ',    // 分享到QQ
            'onMenuShareQZone',    // 分享到QQ空间
            'hideMenuItems',    // 批量隐藏功能按钮
            'showMenuItems',    // 批量显示功能按钮接口
            'closeWindow',       // 关闭当前网页
            'showAllNonBaseMenuItem',   // 显示所有功能按钮接口
            'hideAllNonBaseMenuItem'    // 隐藏所有非基础按钮接口
        ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });

    wx.ready(function(){
        // 隐藏不必要的菜单按钮
//        wx.hideMenuItems({
//            menuList : [
//                'menuItem:share:qq', 'menuItem:share:weiboApp', 'menuItem:share:facebook', 'menuItem:share:QZone',
//                'menuItem:openWithSafari', 'menuItem:openWithQQBrowser', 'menuItem:share:email'
//            ]
//        });
//        // 要显示的菜单项
//        wx.showMenuItems({
//            menuList: ['menuItem:share:appMessage', 'menuItem:share:timeline', 'menuItem:favorite']
//        });

        // 分享到朋友圈的东东
        wx.onMenuShareTimeline({
            title: "${shareInfo.friend?default('投资笔记')}", // 分享标题
            link: '${shareInfo.link?default('http://gupiao.caimao.com/weixin/note/index')}', // 分享链接
            <#if shareInfo.imgUrl??>
            	imgUrl: '${shareInfo.imgUrl?substring(0, shareInfo.imgUrl?last_index_of("/0")) + "/64"?default('http://gupiao.caimao/weixin/note/img/logo.png')}', // 分享图标
            <#else>
            	imgUrl: '${shareInfo.imgUrl?default('http://gupiao.caimao/weixin/note/img/logo.png')}', // 分享图标
            </#if>
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });

        // 发送给好友
        wx.onMenuShareAppMessage({
            title: "${shareInfo.title?default("投资笔记")}", // 分享标题
            desc: "${shareInfo.desc?default("投资笔记")}", // 分享描述
            link: '${shareInfo.link?default("http://gupiao.caimao.com/weixin/note/index")}', // 分享链接
            <#if shareInfo.imgUrl??>
            	imgUrl: '${shareInfo.imgUrl?substring(0, shareInfo.imgUrl?last_index_of("/0")) + "/64"?default('http://gupiao.caimao/weixin/note/img/logo.png')}', // 分享图标
            <#else>
            	imgUrl: '${shareInfo.imgUrl?default('http://gupiao.caimao/weixin/note/img/logo.png')}', // 分享图标
            </#if>
            type: '${shareInfo.type?default("link")}', // 分享类型,music、video或link，不填默认为link
            dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });

        // 分享到微博
        wx.onMenuShareWeibo({
            title: "${shareInfo.friend?default('投资笔记')}", // 分享标题
            link: '${shareInfo.qqlink?default('http://gupiao.caimao.com/weixin/note/index')}', // 分享链接
            <#if shareInfo.imgUrl??>
            	imgUrl: '${shareInfo.imgUrl?substring(0, shareInfo.imgUrl?last_index_of("/0")) + "/64"?default('http://gupiao.caimao/weixin/note/img/logo.png')}', // 分享图标
            <#else>
            	imgUrl: '${shareInfo.imgUrl?default('http://gupiao.caimao/weixin/note/img/logo.png')}', // 分享图标
            </#if>
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });
        // 分享到QQ
        wx.onMenuShareQQ({
            title: "${shareInfo.friend?default('投资笔记')}", // 分享标题
            link: '${shareInfo.qqlink?default('http://gupiao.caimao.com/weixin/note/index')}', // 分享链接
            <#if shareInfo.imgUrl??>
            	imgUrl: '${shareInfo.imgUrl?substring(0, shareInfo.imgUrl?last_index_of("/0")) + "/64"?default('http://gupiao.caimao/weixin/note/img/logo.png')}', // 分享图标
            <#else>
            	imgUrl: '${shareInfo.imgUrl?default('http://gupiao.caimao/weixin/note/img/logo.png')}', // 分享图标
            </#if>
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });

        // 分享到QQ空间
        wx.onMenuShareQZone({
          title: "${shareInfo.friend?default('投资笔记')}", // 分享标题
            link: '${shareInfo.qqlink?default('http://gupiao.caimao.com/weixin/note/index')}', // 分享链接
            <#if shareInfo.imgUrl??>
            	imgUrl: '${shareInfo.imgUrl?substring(0, shareInfo.imgUrl?last_index_of("/0")) + "/64"?default('http://gupiao.caimao/weixin/note/img/logo.png')}', // 分享图标
            <#else>
            	imgUrl: '${shareInfo.imgUrl?default('http://gupiao.caimao/weixin/note/img/logo.png')}', // 分享图标
            </#if>
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });
    });

</script>
