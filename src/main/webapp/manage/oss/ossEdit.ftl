<#import "/resource/common_html_meat.ftl" as html>
<@html.htmlBase>
	<form action="${basepath}/manage/oss.action" method="post" theme="simple" id="form" name="form">
		<table class="table table-bordered">
			<tr style="background-color: #dff0d8">
				<td colspan="2" style="background-color: #dff0d8;text-align: center;">
					<strong>存储编辑</strong>
				</td>
			</tr>
			<tr style="display: none;">
				<td>id</td>
				<td><intput type="hidden" value="${e.id!""}" name="e.id" label="id" /></td>
			</tr>
			<tr>
				<td style="text-align: right;width: 200px;">存储类型</td>
				<td style="text-align: left;">
					<#assign map = {'aliyun':'阿里云存储'}>
                    <select id="code" name="e.code" class="input-medium">
						<#list map?keys as key>
                            <option value="${key}" <#if e.code?? && e.code==key>selected="selected" </#if>>${map[key]}</option>
						</#list>
                    </select>
			</tr>
<!-- 			<tr> -->
<!-- 				<td style="text-align: right;">配置信息</td>    -->
<!-- 				<td style="text-align: left;" > -->
<#--<%-- 					<s:textarea name="e.ossJsonInfo" rows="3" cols="600" cssStyle="width:800px;" id="ossJsonInfo"  --%>-->
<#--<%-- 					data-rule="配置信息;required;ossJsonInfo;length[1~500];"/> --%>-->
<!-- 				</td> -->
<!-- 			</tr> -->
			<tr>
				<td style="text-align: right;">ACCESS_ID</td>   
				<td style="text-align: left;">
					<input type="text"  value="${e.aliyunOSS.ACCESS_ID!""}" name="e.aliyunOSS.ACCESS_ID"  id="ACCESS_ID" data-rule="ACCESS_ID:required;ACCESS_ID;length[1~50];"/>
				</td>
			</tr>
			<tr>
				<td style="text-align: right;">ACCESS_KEY</td>   
				<td style="text-align: left;" >
					<input type="text"  value="${e.aliyunOSS.ACCESS_KEY!""}" name="e.aliyunOSS.ACCESS_KEY"  id="ACCESS_KEY" data-rule="ACCESS_KEY:required;ACCESS_KEY;length[1~50];"/>
				</td>
			</tr>
			<tr>
				<td style="text-align: right;">OSS_ENDPOINT</td>   
				<td style="text-align: left;" >
					<input type="text"  value="${e.aliyunOSS.OSS_ENDPOINT!""}" name="e.aliyunOSS.OSS_ENDPOINT"  id="OSS_ENDPOINT" data-rule="OSS_ENDPOINT:required;OSS_ENDPOINT;length[1~50];"/>
				</td>
			</tr>
			<tr>
				<td style="text-align: right;">bucketName</td>   
				<td style="text-align: left;" >
					<input type="text"  value="${e.aliyunOSS.bucketName!""}" name="e.aliyunOSS.bucketName"  id="bucketName" data-rule="bucketName:required;bucketName;length[1~50];"/>
				</td>
			</tr>
			<tr>
				<td style="text-align: right;">状态</td>
				<td style="text-align: left;">
					<#assign map = {'y':'启用'}>
                    <select id="status" name="e.status" class="input-medium">
						<#list map?keys as key>
                            <option value="${key}" <#if e.status?? && e.status==key>selected="selected" </#if>>${map[key]}</option>
						</#list>
                    </select>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;">
					<#if e.id??>
                        <button method="oss!update.action" class="btn btn-success">
                            <i class="icon-ok icon-white"></i> 保存
                        </button>
					<#else>
                        <button method="oss!insert.action" class="btn btn-success">
                            <i class="icon-ok icon-white"></i> 新增
                        </button>
					</#if>
				</td>
			</tr>
		</table>
	</form>
</@html.htmlBase>