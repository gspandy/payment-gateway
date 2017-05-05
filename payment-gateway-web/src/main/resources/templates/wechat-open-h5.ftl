<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title></title>
    <link rel="icon" href="/public/resources/images/favicon.ico" type="image/x-icon">
</head>
<body>
<div>
    <form id="form" method="post" action="${returnURL!}">
        <input name="id" value="${paymentOrder.id!}" type="hidden"/>
        <input name="result" value="success" type="hidden"/>
    </form>
</div>

<script type="text/javascript">
    function onBridgeReady() {
        WeixinJSBridge.invoke(
                'getBrandWCPayRequest', ${h5PayJson!},
                function (res) {
                    if (res.err_msg == "get_brand_wcpay_request：ok") {
                        document.getElementById("result").value = "SUCCESS"
                    }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                    else {
                        document.getElementById("result").value = "FAILED"
                    }
                    var form=document.getElementById("form");
                    form.submit();
                }
    );
    }
    if (typeof WeixinJSBridge == "undefined") {
        if (document.addEventListener) {
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
        } else if (document.attachEvent) {
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
        }
    } else {
        onBridgeReady();
    }
</script>
</body>
</html>