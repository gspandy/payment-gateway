<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>支付结果</title>
    <link rel="icon" href="/public/resources/images/favicon.ico" type="image/x-icon">
</head>
<body>
<div>
[#if paymentOrder??]
    <form id="form" method="post" action="${paymentOrder.returnUrl!}">
        <input name="id" value="${paymentOrder.id!}" type="hidden"/>
        <input name="amount" value="${paymentOrder.amount!}" type="hidden"/>
        <input name="title" value="${paymentOrder.title!}" type="hidden"/>
        <input name="status" value="${paymentOrder.status!}" type="hidden"/>
        <input name="outTradeNo" value="${paymentOrder.outTradeNo!}" type="hidden"/>
        <input name="tradeNo" value="${paymentOrder.tradeNo!}" type="hidden"/>
        <input name="custom" value="${paymentOrder.custom!}" type="hidden"/>
        <input name="result" value="FAILED" type="hidden"/>
        <input name="message" value="${message!}" type="hidden"/>
    </form>
    <script>
        var form=document.getElementById("form");
        form.submit();
    </script>
[#else]
${message!}
[/#if]
</div>

</body>
</html>