<#import "/resource/common_html_meat.ftl" as html>
<@html.htmlBase>
<form action="${basepath}/manage/user" method="post">
	<table class="table table-bordered table-condensed">
		<tr>
			<td style="text-align: right;">状态</td>
			<td style="text-align: left;" >
                <select name="e.status" id="status" class="input-small">
                    <option value="">全部</option>
                    <option value="y">启用</option>
                    <option value="n">禁用</option>
                </select>
			</td>
		</tr>
		<tr>
			<td colspan="11">
            <#if checkPrivilege("user!insert.action") >
					<button method="user!selectList.action" class="btn btn-primary" onclick="selectList(this)">
						<i class="icon-search icon-white"></i> 查询
					</button>
             </#if>
				<#if checkPrivilege("user!insert.action") >
                <a href="${basepath}/manage/user!toAdd.action" class="btn btn-success"><i class="icon-plus-sign icon-white"></i> 添加</a>
				</#if>

				<div style="float: right;vertical-align: middle;bottom: 0px;top: 10px;">
                    <#include "/manage/system/pager.ftl"/>
				</div>

			</td>
		</tr>
	</table>
	
	<table class="table table-bordered table-hover">
		<thead>
		<tr style="background-color: #dff0d8">
			<th width="20"><input type="checkbox" id="firstCheckbox"/></th>
			<th style="display: none;">id</th>
			<th>帐号</th>
			<th>昵称</th>
			<th>创建时间</th>
			<th>角色</th>
			<th>状态</th>
			<th nowrap="nowrap">操作</th>
		</tr></thead>
        <#list pager.list as item>
			<tr >
				<td><#if item.id!=1><input type="checkbox" name="ids" value="${item.id}"/></#if></td>
				<td  style="display: none;">&nbsp;${item.id!""}</td>
				<td>&nbsp;${item.username!""}</td>
				<td>&nbsp;${item.nickName!""}</td>
				<td>&nbsp;${item.createtime!""}</td>
				<td>&nbsp;${item.role_name}</td>
				<td>
                    <#if item.status == "y">
                        <img src="${basepath}/resource/images/action_check.gif">
                    <#else >
                        <img src="${basepath}/resource/images/action_delete.gif">
                    </#if>
				</td>
				<td>
                    <#if checkPrivilege("user!edit.action")>
                        <a href="${basepath}/manage/user!toEdit.action?e.id=${item.id}">编辑</a>
                    </#if>
				</td>
			</tr>
        </#list>
		<tr><td colspan="16" style="text-align:center;">
        <#include "/manage/system/pager.ftl"/>
        </td>
		</tr>
	</table>
</form>
</@html.htmlBase>