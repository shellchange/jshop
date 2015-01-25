<#import "/resource/common_html_meat.ftl" as html/>
<@html.htmlBase>
<div style="text-align: center; border: 0px solid #999;margin: auto;">
	<div style="text-align: center; border: 0px solid #999;
		margin: auto;margin-top: 150px;">
		<form action="${basepath}/manage/menu.action" theme="simple">
				<table>
					<tr style="display: none;">
						<th>id</th>
						<td><input type="hidden" value="${menu.rid!""}" name="menu.rid"/></td>
					</tr>
					<tr style="display: none;">
						<th>pid</th>
						<td><input type="hidden" value="${menu.pid!""}"  name="menu.pid"/></td>
					</tr>
					<tr>
						<th>url</th>
						<td>
							<input type="text" value="${menu.url!""}" name="menu.url" readonly="false"/>
						</td>
					</tr>
					<tr>
						<th>name</th>
						<td>
                            <input type="text" value="${menu.name!""}" name="menu.name" readonly="false"/>
						</td>
					</tr>
					<tr><td></td>
						<td>
							<button value="save"></button>
							<button value="back"></button>
						    <#--<s:submit method="save" value="save"/>-->
						    <#--<s:submit method="back" value="back"/>-->
						</td>
					</tr>
				</table>

		</form>
	</div>
</div>
</@html.htmlBase>