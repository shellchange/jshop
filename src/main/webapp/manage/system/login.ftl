<#import "/manage/tpl/htmlBase.ftl" as html>
<@html.htmlBase checkLogin=false jsFiles=["system/login.js"]>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="login-panel panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><b>${systemSetting().name}</b>-后台</h3>
                </div>
                <div class="panel-body">
                    <form role="form" id="formLogin" action="${basepath}/manage/user/login" method="post">
                        <fieldset>
                            <div class="form-group">
                                <input type="text" value="${e.username!""}"placeholder="账号" name="username" class="len form-control" id="username" autofocus/>
                            </div>
                            <div class="form-group">
                                <input type="password" name="password" placeholder="密码" class="len form-control" value="123456" label="密码" />
                            </div>
                            <#--<div class="checkbox">-->
                                <#--<label>-->
                                    <#--<input name="remember" type="checkbox" value="Remember Me">Remember Me-->
                                <#--</label>-->
                            <#--</div>-->
                            <!-- Change this to a button or input when using this as a form -->
                            <a href="javascript:void(0);" id="btnLogin" class="btn btn-lg btn-success btn-block">登录</a>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
					<#--<tr>-->
						<#--<td colspan="2" style="text-align: center;">测试账号：test1/123456-->
						<#--</td>-->
					<#--</tr>-->
					<#--<tr>-->
						<#--<td colspan="2" style="text-align: center;">${systemSetting().version}-->
						<#--</td>-->
					<#--</tr>-->
</body>
</@html.htmlBase>