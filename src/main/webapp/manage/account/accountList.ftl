<#import "/resource/common_html_meat.ftl" as html>
<@html.htmlBase>
	<form action="${basepath}/manage/account!selectList.action" method="post" theme="simple">
		<table class="table table-bordered">
			<tr>
				<td style="text-align: right;" nowrap="nowrap">账号</td>
				<td style="text-align: left;"><input type="text" value="${e.account!""}" name="e.account"  class="search-query input-small"
						id="account" /></td>
				<td style="text-align: right;" nowrap="nowrap">昵称</td>
				<td style="text-align: left;"><input type="text" value="${e.nickname!""}" name="e.nickname" class="input-small"
						id="nickname" /></td>
				<td style="text-align: right;" nowrap="nowrap">会员等级</td>
				<td style="text-align: left;">
					<#assign map = {'':'','R1':'普通会员','R2':'铜牌会员','R3':'银牌会员','R4':'金牌会员','R5':'钻石会员'}>
                    <select id="rank" name="e.rank" class="input-medium">
						<#list map?keys as key>
                            <option value="${key}" <#if e.rank?? && e.rank==key>selected="selected" </#if>>${map[key]}</option>
						</#list>
                    </select>
				<td style="text-align: right;" nowrap="nowrap">状态</td>
				<td style="text-align: left;">
					<#assign map = {'':'','y':'已冻结','n':'未冻结'}>
                    <select id="freeze" name="e.freeze" class="input-medium">
						<#list map?keys as key>
                            <option value="${key}" <#if e.freeze?? && e.freeze==key>selected="selected" </#if>>${map[key]}</option>
						</#list>
                    </select>
				</td>
				<td style="text-align: right;" nowrap="nowrap">注册日期</td>
				<td style="text-align: left;" colspan="3" nowrap="nowrap">
					<input id="d4311" class="Wdate search-query input-small" type="text" name="e.startDate"
					value="${e.startDate!""}"
					onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'d4312\')||\'2020-10-01\'}'})"/>
					~ 
					<input id="d4312" class="Wdate search-query input-small" type="text" name="e.endDate" 
					value="${e.endDate!""}"
					onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d4311\')}',maxDate:'2020-10-01'})"/>
						</td>
			</tr>
			<tr>
				<td colspan="28">
<#--<%-- 					<s:submit method="selectList" value="查询" cssClass="btn btn-primary"/> --%>-->
<#--<%-- 					<s:a method="selectList" cssClass="btn btn-primary"> --%>-->
<!-- 						<i class="icon-search icon-white"></i> 查询 -->
<#--<%-- 					</s:a> --%>-->
					<button method="account!selectList.action" class="btn btn-primary" onclick="selectList(this)">
						<i class="icon-search icon-white"></i> 查询
					</button>
					
					<div style="float: right;vertical-align: middle;bottom: 0px;top: 10px;">
						<#include "/manage/system/pager.ftl"/>
					</div>
				</td>
			</tr>
		</table>
		
		<table class="table table-bordered table-hover">
			<tr style="background-color: #dff0d8">
				<th width="20"><input type="checkbox" id="firstCheckbox" /></th>
				<th nowrap="nowrap">登陆方式</th>
				<th nowrap="nowrap">帐号</th>
				<th nowrap="nowrap">昵称</th>
				<th nowrap="nowrap">会员等级</th>
				<th nowrap="nowrap">邮箱</th>
				<th nowrap="nowrap">注册日期</th>
				<th nowrap="nowrap">最后登录时间</th>
				<th nowrap="nowrap">最后登录IP</th>
				<th nowrap="nowrap">是否冻结</th>
<!-- 				<th width="150px">冻结时间</th> -->
				<th nowrap="nowrap">操作</th>
			</tr>
			<#list pager.list as item>
				<tr>
					<td><input type="checkbox" name="ids"
						value="${item.id!""}</td>
					<td nowrap="nowrap" align="center">
						<#if item.accountType??&&item.accountType=="qq">
							<img alt="" src="${basepath}/resource/images/mini_qqLogin.png">
						<#elseif  item.accountType??&&item.accountType=="sinawb">
							<img alt="" src="${basepath}/resource/images/mini_sinaWeibo.png">
						<#elseif item.accountType??&&item.accountType=="alipay">
							<span class="badge badge-warning">alipay</span>
						<#else>
							<span class="badge badge-warning">jeeshop</span>
						</#if>
					</td>
					<td nowrap="nowrap">
						&nbsp;${item.account!""}
					</td>
					<td nowrap="nowrap">&nbsp;${item.nickname!""}</td>
					<td nowrap="nowrap">&nbsp;${item.rankName!""}</td>
					<td nowrap="nowrap">&nbsp;${item.email!""}</td>
					<td nowrap="nowrap">&nbsp;${item.regeistDate!""}</td>
					<td nowrap="nowrap">&nbsp;${item.lastLoginTime!""}</td>
					<td nowrap="nowrap">&nbsp;${item.lastLoginIp!""}</td>
					<td nowrap="nowrap">&nbsp;
						<#if item.freeze?? && item.freeze=="y">
							<img alt="" src="${basepath}/resource/images/login.gif">
						<#elseif item.freeze?? && item.freeze=="n">
							
						<#else>
							异常
						</#if>
					</td>
<!-- 					<td nowrap="nowrap">&nbsp; -->
<#--<%-- 						<s:if test="freeze.equals(\"y\")"> --%>-->
<#--<%-- 							<s:property value="freezeStartdate" />~<s:property value="freezeEnddate" /> --%>-->
<#--<%-- 						</s:if>								 --%>-->
<!-- 					</td> -->
					<td nowrap="nowrap">
<#--<%-- 						<s:a href="account!toEdit.action?e.id=%{id}">编辑</s:a> --%>-->
						<a target="_blank" href="account!show.action?account=${item.account!""}">查看</a>
<#--<%-- 						<s:a href="account!czmm.action?e.id=%{id}">重置密码</s:a> --%>-->
						<a href="account!toFreeze.action?e.id=${item.id!""}">冻结</a>
					</td>
				</tr>
			</#list>
			<tr>
				<td colspan="16" style="text-align: center;">
					<#include "/manage/system/pager.ftl"/></td>
			</tr>
		</table>
	</form>
<script type="text/javascript">
	$(function() {
		function c1(f) {
			$(":checkbox").each(function() {
				$(this).attr("checked", f);
			});
		}
		$("#firstCheckbox").click(function() {
			if ($(this).attr("checked")) {
				c1(true);
			} else {
				c1(false);
			}
		});

	});
	function deleteSelect() {
		if ($("input:checked").size() == 0) {
			return false;
		}
		return confirm("确定删除选择的记录?");
	}
</script>

</@html.htmlBase>