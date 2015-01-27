<#import "/resource/common_html_meat.ftl" as html>
<@html.htmlBase>
<form action="${basepath}/manage/account.action" theme="simple" id="form">

<div id="tabs">
	<ul>
		<li><a href="#tabs-1">基本信息</a></li>
	</ul>
	<div id="tabs-1">
		<div class="alert alert-info" style="margin-bottom: 2px;text-align: left;">
			<strong>后台管理人员信息：</strong>
		</div>
		<table class="table table-bordered">
					<tr style="display: none;">
						<td>id</td>
						<td><input type="hidden" value="${e.id!""}" name="e.id" label="id" id="id"/></td>
					</tr>
					<tr>
						<td style="text-align: right;width: 200px;">昵称</td>
						<td style="text-align: left;">${e.nickname!""}</td>
					</tr>
					<tr>
						<td style="text-align: right;">账号</td>   
						<td style="text-align: left;">${e.account!""}</td>
					</tr>
					<tr>
						<td style="text-align: right;">城市</td>
						<td style="text-align: left;">${e.city!""}</td>
					</tr>
					<tr>
						<td style="text-align: right;">联系地址</td>
						<td style="text-align: left;">${e.address!""}</td>
					</tr>
					<tr>
						<td style="text-align: right;">证件号码</td>
						<td style="text-align: left;">${e.postcode!""}</td>
					</tr>
					<tr>
						<td style="text-align: right;">证件类型</td>
						<td style="text-align: left;">${e.cardType!""}</td>
					</tr>
					<tr>
						<td style="text-align: right;">等级</td>
						<td style="text-align: left;">${e.grade!""}</td>
					</tr>
					<tr>
						<td style="text-align: right;">消费额</td>
						<td style="text-align: left;">${e.amount!""}</td>
					</tr>
					<tr>
						<td style="text-align: right;">电话</td>
						<td style="text-align: left;">${e.tel!""}</td>
					</tr>
					<tr>
						<td style="text-align: right;">Email地址</td>
						<td style="text-align: left;">${e.email!""}</td>
					</tr>
					<tr>
						<td style="text-align: right;">是否冻结</td>
						<td style="text-align: left;">
							<s:if test="e.freeze==1">是</s:if>
							<s:else>否</s:else>
						</td>
					</tr>
					
					<tr>
						<td style="text-align: right;">最后登陆时间</td>
						<td style="text-align: left;">${e.lastLoginTime!""}</td>
					</tr>
					<tr>
						<td style="text-align: right;">最后登陆时间</td>
						<td style="text-align: left;">${e.lastLoginIp!""}</td>
					</tr>
					<tr>
						<td style="text-align: right;">注册日期</td>
						<td style="text-align: left;">${e.regeistDate!""}</td>
					</tr>
					<tr>
						<td style="text-align: right;">会员类型</td>
						<td style="text-align: left;">${e.accountType!""}</td>
					</tr>
				</table>
	</div>
</div>
</form>

<script type="text/javascript">
$(function() {
	$( "#tabs" ).tabs({
		//event: "mouseover"
	});
	
});

</script>
</@html.htmlBase>