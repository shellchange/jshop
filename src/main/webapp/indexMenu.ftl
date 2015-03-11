<#if systemSetting().isopen=="false">
    ${systemSetting().closeMsg!"系统关闭，请联系管理员"}
</#if>

<style type="text/css">
.centerImageCss{
	width: 289px;
	height: 190px;
}
.title {
	display: block;
	width: 280px;
	overflow: hidden; /*注意不要写在最后了*/
	white-space: nowrap;
	-o-text-overflow: ellipsis;
	text-overflow: ellipsis;
}
body{
	padding-top: 0px;
  	padding-bottom: 0px;
	font-size: 12px;
/*    	font-family: 微软雅黑, Verdana, sans-serif, 宋体; */
}
</style>

<style>
.hotSearch{
	cursor: pointer;
}
</style>
<a name="toTop" id="toTop"></a>
<!-- 顶部导航条 -->
<!-- <div class="navbar navbar-default navbar-fixed-top" -->
<div class="navbar navbar-default"
	style="background-color: white;border-color: #ccc;min-height: 10px;margin-top: 0px;margin-bottom: 0px;border: 0px;">
<!-- 			style="position: fixed;top: 0px;text-align: right;border: 2px solid red;z-index: 10000;"> -->
	<div class="container" style="min-height: 10px;margin-top:5px;margin-bottom:5px;border: 0px solid red;">
		<div class="row">
			<div class="col-xs-3">
				<a href="${systemSetting().www}"><img style="max-height: 50px;" alt="myshop-logo" src="${systemSetting().log}"/></a>
			</div>
			<div class="col-xs-6" style="border: 0px solid blue;padding-left:5px;">
				<!-- search查询输入框 -->
<!-- 				style="padding: 0px;margin-left: 0px;" -->
				<form class="form-inline" role="form" name="searchForm" id="searchForm" 
					action="${basepath}/search.html">
						<#--<%-->
							<#--String key = "";-->
										<#--if(request.getAttribute("key")!=null && !StringUtils.isBlank(request.getAttribute("key").toString())){-->
											<#--key = request.getAttribute("key").toString();-->
										<#--}-->
						<#--%>-->
					${key!""}
					<div class="form-group btn-group">
						<div class="input-group">
							<input type="text" name="key" id="key" class="form-control input-sm" style="border: 2px solid red;border-right: 0px;" 
			      		placeholder="请输入商品关键字" size="40" value="${key!""}" maxlength="20"/>
							<span class="input-group-btn">
								<button value="搜索" class="btn btn-primary btn-sm" onclick="search();">
									<span class="glyphicon glyphicon-search"></span>&nbsp;搜索
								</button>
								<a class="btn btn-success btn-sm" href="${basepath}/cart/cart.html">
									<span class="glyphicon glyphicon-shopping-cart"></span>&nbsp;购物车
									<#if myCart?? && myCart.productList?? && myCart.productList.size() gt 0>
                                        <span class="badge badge-success">${session.myCart.productList.size()}</span>
									</#if>
								</a>
							</span>
						</div>
					</div>
				</form>
				<div style="text-align: left;margin-top: 5px;">热门搜索：
					<#list systemManager().hotqueryList as item>
                        <a class="hotSearch" href="${item.url}" target="_blank">
							${item.key1!""}
                        </a>
					</#list>
				</div>
			</div>
			<div class="col-xs-3" style="height: 100%;">
		    	<div class="row" style="height: 100%;">
					<#if currentUser()??>

                        <span id="myshopMenuPPP" style="display: inline-block;z-index: 9999;position: relative;;">
		          			<!-- 会员中心的菜单 -->
		          			<span style="margin-top: 0px;">
							  <a data-toggle="dropdown" style="display: block;margin-top: 0px;">
                                  <span class="glyphicon glyphicon-user"></span>&nbsp;用户中心
                                  (${currentUser().nickname})
                                  <span class="caret" style="display: inline-block;"></span>
                              </a>
							  <ul class="dropdown-menu" id="myshopMenu" role="menu" style="display: none;margin-top: 0px;">
                                  <li><a href="${basepath}/user/user.html"><span class="glyphicon glyphicon-user"></span>&nbsp;个人资料</a></li>
                                  <li><a href="${basepath}/user/topwd.html"><span class="glyphicon glyphicon-screenshot"></span>&nbsp;修改密码</a></li>
                                  <li class="divider"></li>
                                  <li><a href="${basepath}/user/orders.html"><span class="glyphicon glyphicon-th"></span>&nbsp;我的订单</a></li>
                                  <li><a href="${basepath}/user/address.html"><span class="glyphicon glyphicon-send"></span>&nbsp;配送地址</a></li>
                                  <li><a href="${basepath}/user/favorite.html"><span class="glyphicon glyphicon-tags"></span>&nbsp;收藏夹</a></li>
                                  <li class="divider"></li>
                                  <li><a href="${basepath}/user/exit.html"><span class="glyphicon glyphicon-pause"></span>&nbsp;退出系统</a></li>
                              </ul>
							</span>
		          		</span>
		          		<span style="display: none;">
							${currentUser().nickname!""}
		          			(${currentUser().loginType!""})
		          		</span>
					<#else >
                        <span class="col-xs-12" id="loginOrRegSpan" style="font-size: 14px;">
		          			<a href="${basepath}/user/login.html">登陆</a>|<a href="${basepath}/user/register.html">注册</a>
		          		</span>
					</#if>

		          	<div style="vertical-align: middle;margin-top: 10px;font-size: 18px;z-index: 0;">
		          		<span class="glyphicon glyphicon-earphone"></span>&nbsp;客服热线:${systemSetting().tel}
		          	</div>
		    	</div>
			</div>
		</div>
		
	</div>
</div>
		
<!-- <div class="navbar navbar-default navbar-fixed-top" style="top: 50px;"> -->
<div class="navbar navbar-default" style="margin-bottom: 15px;z-index: 111">
      <div class="container">
        <div class="navbar-header col-xs-3" style="text-align: center;" id="navbar-header">
				<#if selectMenu??>
                    <a class="navbar-brand" href="${systemSetting().www}" style="font-weight: bold;margin-left: 5px;">全部商品分类</a>
				<#else>
                    <a class="navbar-brand" href="${systemSetting().www}" style="font-weight: bold;margin-left: 5px;">全部商品分类</a>
				</#if>
	          <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#navbar-main">
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	          </button>
        </div>
        <div class="navbar-collapse collapse" id="navbar-main">
	          <ul class="nav navbar-nav" >
	          		<#--<%-->
	          			<#--//out.println("request.getServletPath()="+request.getServletPath());-->
	          				          		<#--if(request.getServletPath().endsWith("/index.jsp")){-->
	          				          			<#--request.getSession().setAttribute(FrontContainer.selectMenu,"0");-->
	          				          		<#--}else if(request.getServletPath().endsWith("/specialProductList.jsp")){-->
	          				          			<#--request.getSession().setAttribute(FrontContainer.selectMenu,"special");-->
	          				          		<#--}-->
	          				          		<#--if(request.getSession().getAttribute(FrontContainer.selectMenu)==null){-->
	          			          				<#--request.getSession().setAttribute(FrontContainer.selectMenu,"0");-->
	          				          		<#--}-->
	          				          		<#---->
	          				          		<#--List<Catalog> catalogs = SystemManager.catalogs;-->
	          			          			<#--application.setAttribute("catalogs", catalogs);-->
	          		<#--%>-->
	          		<!-- 首页 -->
						<#if selectMenu??&&selectMenu=="0">
                            <li class="active"><a href="${systemSetting().www}"><b>首页</b></a></li>
						<#else>
                            <li><a href="${systemSetting().www}"><b>首页</b></a></li>
						</#if>
					<!-- 类别作为菜单显示 -->
						<#list systemManager().catalogs as item>
						    <#if item.showInNav == "y">
								<#if selectMenu?? && item.id == selectMenu>
									<li class="active"><a href="${basepath}/catalog/${item.code}.html"><b>${item.name}</b></a></li>
								<#else>
									<li><a href="${basepath}/catalog/${item.code}.html"><b>${item.name}</b></a></li>
								</#if>
							</#if>
						</#list>
				</ul>

		          <ul class="nav navbar-nav navbar-right" style="display: block;">
		          	<!-- 促销活动 -->
		          	<#if selectMenu?? && selectMenu=="activity">
						<li class="active"><a href="${basepath}/activity/activity.html" >
						<span class="glyphicon glyphicon-time"></span>
							<b>促销活动</b></a>
						</li>
					<#else>
						<li>
							<a href="${basepath}/activity/activity.html" >
							<span class="glyphicon glyphicon-time"></span>
							<b>促销活动</b></a>
						</li>
					</#if>
					
					<!-- 积分商城 -->
		          	<#if selectMenu?? && selectMenu=="score">
						<li class="active"><a href="${basepath}/activity/score.html" >
							<b>积分商城</b></a>
						</li>
					<#else>
						<li>
							<a href="${basepath}/activity/score.html" >
							<b>积分商城</b></a>
						</li>
					</#if>
					
					<!-- 团购活动 -->
					<#if false>
						<#if selectMenu?? && selectMenu=="tuan">
							<li class="active"><a href="${basepath}/activity/tuan.html" >
								<b>团购活动</b></a>
							</li>
						<#else>
							<li>
								<a href="${basepath}/activity/tuan.html" >
								<b>团购活动</b></a>
							</li>
						</#if>
					</#if>
		          </ul>
        </div>
      </div>
    </div>




