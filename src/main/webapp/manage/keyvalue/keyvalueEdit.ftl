<#import "/resource/common_html_meat.ftl" as html>
<@html.htmlBase>
<form action="${basepath}/manage/keyvalue.action" id="form" name="form">
			<table class="table table-bordered">
				<tr style="background-color: #dff0d8">
					<td colspan="2" style="background-color: #dff0d8;text-align: center;">
						<strong>键值对编辑</strong>
					</td>
				</tr>
				<tr style="display: none;">
					<td>id</td>
					<td><input type="hidden" name="e.id" value="${e.id!""}" id="id"></td>
				</tr>
				<tr>
					<td style="text-align: right;">键</td>
					<td style="text-align: left;"><input type="text" name="e.key1" value="${e.key1!""}" id="key1" data-rule="键:required;key1;length[1~45];"/></td>
				</tr>
				<tr>
					<td style="text-align: right;">值</td>
					<td style="text-align: left;"><input type="text" name="e.value" value="${e.value!""}" id="value" data-rule="值:required;value;length[1~45];"/></td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center;">
							<#if e.id??>
                                <button method="keyvalue!update.action" class="btn btn-success" >
                                    <i class="icon-ok icon-white"></i> 保存
                                </button>
								<#else>
                                    <button method="keyvalue!insert.action" class="btn btn-success" >
                                        <i class="icon-ok icon-white"></i> 新增
                                    </button>
							</#if>
					</td>
				</tr>
			</table>
	</form>

	</@html.htmlBase>