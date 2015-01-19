<#import "/resource/common_html_meat.ftl" as html>
<@html.htmlBase>
<style>
	.td_right{text-align: right;}
</style>
<script type="text/javascript">
	$(function() {
		 $("#username").focus();
	});
</script>
</head>

<body>
<#if e.id??>
    <#assign formAction="111">
<#assign insertAction=false />
<#else >
<#assign formAction="insert">
    <#assign insertAction=true />
</#if>

<#--<%-- formAction=<s:property value="#formAction"/><br> --%>-->
<#--<%-- formAction2=<s:property value="#request.formAction"/><br> --%>-->
<form action="${basepath}/manage/user.action" id="form">
	<#--<s:form action="user!" namespace="/" theme="simple" id="form">-->
		<table class="table table-bordered">
			<tr>
				<td colspan="2" style="background-color: #dff0d8;text-align: center;">
					<strong>帐号编辑</strong>
				</td>
			</tr>
			<tr style="display:none;">
				<th>id</th>
				<td><input type="hidden" name="e.id" value="${e.id!""}"></td>
			</tr>
			<tr>
				<th class="td_right">帐号</th>
				<td style="text-align: left;">
                    <#if insertAction>
                        <input type="text" name="e.username" id="username"  data-rule="帐号:required;username;length[4~20];remote[user!unique.action]">
                    <#else >
                        ${e.username}
                    </#if>
				</td>
			</tr>
			<tr>
				<th class="td_right">昵称</th>
				<td style="text-align: left;">
				<input name="e.nickname" value="${e.nickname!""}" id="nickname"  data-rule="昵称:required;nickname;length[2~20];remote[user!unique.action]"/>
					</td>
			</tr>

                <tr>
                    <th class="td_right">密码</th>
                    <td style="text-align: left;"><input type="password" name="e.password" data-rule="密码:password;length[6~20];"
                                                              id="password" />

            <#if !insertAction> <br>(不输入表示不修改密码)</#if>
                    </td>
                </tr>
                <tr>
                    <th class="td_right">确认密码</th>
                    <td style="text-align: left;"><input type="password" name="e.newpassword2" data-rule="确认密码:match(e.password)"
                                                              id="newpassword2" />
                    </td>
                </tr>
			<tr>
				<th class="td_right">选择角色</th>
				<td style="text-align: left;">
                    <select name="rid">
                        <#list roleList as item>
                            <#--${item.id}-->
                            <option value="${item.id}" <#if e.rid?? && item.id?string == e.rid?string>selected="selected"</#if>> ${item.role_name}</option>
                        </#list>
                    </select>
				</td>
			</tr>
            <#if !e.username?exists || e.username?exists && e.username != "admin">
				<tr>
					<th class="td_right">状态</th>
					<td style="text-align: left;" >
                        <select class="input-small" id="status" name="e.status" >
                            <option value="y" <#if e.status?? && e.status == "y">selected="selected" </#if>>启用</option>
                            <option value="n"<#if e.status?? && e.status == "n">selected="selected" </#if>>禁用</option>
                        </select>
					</td>
				</tr>
            </#if>
			<tr >
				<td colspan="2" style="text-align: center;">
					<#if insertAction>
						<button method="user!insert.action" class="btn btn-success">
							<i class="icon-ok icon-white"></i> 新增
						</button>
                        <#else >
						<button method="user!update.action" class="btn btn-success">
							<i class="icon-ok icon-white"></i> 保存
						</button>
                    </#if>
				</td>
			</tr>
		</table>
</form>
</@html.htmlBase>