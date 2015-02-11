<#import "/resource/common_html_meat.ftl" as html>
<@html.htmlBase checkLogin=false>
<script type="text/javascript">
	$(function() {
		$("#username").focus();

		if (top.location != self.location) {
			top.location = self.location;
		}
	});
</script>
	<div class="container">
		<div class="row">
		
			<div class="span5">
				<div style="margin-top: 100px;text-align: right;margin-right: 10px;">
					<img data-src="holder.js/240x240" class="img-circle" alt="240x240" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAIwAAACMCAYAAACuwEE+AAADHklEQVR4Xu3XQU7qYABGUR2xMZbNmpgz0mDSiKTQXi2VkvOG8kl99z9p4f14PH68+afAzALvwMwsZfZVABgQUgFgUi5jYBhIBYBJuYyBYSAVACblMgaGgVQAmJTLGBgGUgFgUi5jYBhIBYBJuYyBYSAVACblMgaGgVQAmJTLGBgGUgFgUi5jYBhIBYBJuYyBYSAVACblMgaGgVQAmJTLGBgGUgFgUi5jYBhIBYBJuYyBYSAVACblMgaGgVQAmJTLGBgGUgFgUi5jYBhIBYBJuYyBYSAVACblMgaGgVQAmJTLGBgGUgFgUi5jYBhIBYBJuYyBYSAVACblMgaGgVQAmJTLGBgGUgFgUi5jYBhIBYBJuYyBYSAVACblMgaGgVQAmJTLGBgGUgFgUi5jYBhIBYBJuYyBYSAVACblMgaGgVQAmJTLePNgDofD1ynu9/vR0xx7ffjZ8Au3fnfsDde+3rMR3TSYy4MfO/Sx168PfArA5YGtfb1nw3L+ezYN5vwfuHXg55/vdru30+n04w70FzD/cb1nQwPMxSNtwHQJ7fpxtzZQYBYucO8zyvkxNXVHGXt97M40/NlLX2/hHA9/u5e+w1zXmwPo3mNn6pH02+s9/JQXvMDLgrl1R5hzx6l3mLEPxsOH8KnrLXiWq7zVpsHM+Xp875vS9dfqqc8wS19vlRNe+CKbBrNwC283owAwMyKZfBcAhoZUAJiUyxgYBlIBYFIuY2AYSAWASbmMgWEgFQAm5TIGhoFUAJiUyxgYBlIBYFIuY2AYSAWASbmMgWEgFQAm5TIGhoFUAJiUyxgYBlIBYFIuY2AYSAWASbmMgWEgFQAm5TIGhoFUAJiUyxgYBlIBYFIuY2AYSAWASbmMgWEgFQAm5TIGhoFUAJiUyxgYBlIBYFIuY2AYSAWASbmMgWEgFQAm5TIGhoFUAJiUyxgYBlIBYFIuY2AYSAWASbmMgWEgFQAm5TIGhoFUAJiUyxgYBlIBYFIuY2AYSAWASbmMgWEgFQAm5TIGhoFUAJiUyxgYBlIBYFIuY2AYSAWASbmMgWEgFQAm5TL+BPn285fsB0YAAAAAAElFTkSuQmCC" style="width: 240px; height: 240px;">
				</div>
			</div>
			
			<div class="span7" style="border-left: 1px solid #ccc;margin-left: -2px;">
<!-- 				<hr width="1px" size="100" style="height: 400px;float: left;"> -->
<!-- <HR align=center width=12 height="5"  color=#ccc style="height: 500px;float: left;display:block;"> -->
				<table class="table table-bordered" style="width: 95%;margin-top: 100px;float: right;">
					<caption>

                        <#if Session["loginErrorObj"]??>
						<div class="alert alert-warning alert-dismissable" style="margin-bottom:0px;">
						  <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
							${Session["loginErrorObj"]}
						</div>
                        </#if>
					</caption>
					<tr>
						<td colspan="2"
							style="background-color: #dff0d8; text-align: center;"><strong>&nbsp;${systemSetting().name}-后台</strong>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<form action="${basepath}/manage/user/login" method="post"
								class="form-horizontal">
								<div class="control-group">
									<label class="control-label" for="inputEmail">帐号</label>
									<div class="controls">
										<div class="input-prepend">
											<span class="add-on"><i class="icon-user"></i></span>
											<input type="text" value="${e.username!""}" name="username" class="len" id="username" />
										</div>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="inputPassword">密码</label>
									<div class="controls">
										<div class="input-prepend">
											<span class="add-on"><i class="icon-lock"></i></span>
											<input type="password" name="password" class="len" value="123456" label="密码" />
										</div>
									</div>
								</div>
								<div class="control-group">
									<div class="controls">
										<div class="input-prepend">
											<span class="add-on"><i class="icon-hand-right"></i></span>
											<button class="btn">
												登陆
											</button>
										</div>
									</div>
								</div>
							</form>
						</td>
					</tr>
					<tr>
						<td colspan="2" style="text-align: center;">测试账号：test1/123456
						</td>
					</tr>
					<tr>
						<td colspan="2" style="text-align: center;">${systemSetting().version}
						</td>
					</tr>
				</table>
				
			</div>
		</div>
	</div>
</@html.htmlBase>