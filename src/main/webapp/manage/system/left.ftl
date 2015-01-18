<#import "/resource/common_html_meat.ftl" as html>
<@html.htmlBase>
    <script type="text/javascript">
    	//注销
    	function a(){
    		window.parent.location.href = "${basepath}/manage/system/user!loginOut.action";
    	}
    </script>
<style>
    body{font-size: 12px;background-color: #E6EAE9;margin: 0;padding: 0;}
</style>
<div id="userDiv">
	&nbsp;&nbsp;欢迎你! | <a href="#" onclick="a()">注销</a> | <a target="_blank" href="${systemSetting().www}">门户</a>
</div>
<hr style="margin: 0;padding: 0">
<#--<jsp:include page="../system/menu/leftMenu.jsp"></jsp:include>-->
<#include "/manage/system/menu/leftMenu.ftl">
</@html.htmlBase>