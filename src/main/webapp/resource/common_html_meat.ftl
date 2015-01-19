<#macro htmlBase title="JEESHOP" jsFiles=[] cssFiels=[] nobody=false checkLogin=true>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <#assign non_responsive2>y</#assign>
    <#assign responsive>${session.getAttribute("responsive")!""}</#assign>
    <#if responsive == "y">
        <#assign non_responsive2>n</#assign>
    <#elseif systemSetting().openResponsive == "n">
        <#assign non_responsive2>y</#assign>
    <#else >
        <#assign non_responsive2>n</#assign>
    </#if>
    <script>
        var basepath = "${basepath}";
        var staticpath = "${staticpath}";
        var non_responsive2 = "${non_responsive2}";
        <#if currentUser()??>
            var login = true;
        var currentUser = "${currentUser().username}";
        <#else >
        var login = false;
        var currentUser = "";
            <#if checkLogin>
                top.location = "${basepath}/manage/system/user!loginOut.action";
            </#if>
        </#if>
    </script>
    <#if non_responsive2 != "y">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </#if>
    <meta name="description" content="${systemSetting().description}"/>
    <meta name="keywords" content="${systemSetting().keywords}"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>${title!"JEESHOP"}</title>
    <link rel="shortcut icon" type="image/x-icon" href="${systemSetting().shortcuticon}">

    <link rel="stylesheet" href="${basepath}/resource/zTree3.5/css/zTreeStyle/zTreeStyle.css" type="text/css">
    <link rel="stylesheet" href="${basepath}/resource/bootstrap/css/bootstrap.min.css"  type="text/css">
    <link rel="stylesheet" href="${basepath}/resource/css/base.css"  type="text/css">
    <link rel="stylesheet" href="${basepath}/resource/bootstrap/css/docs.css"  type="text/css">
    <link rel="stylesheet" href="${basepath}/resource/jquery-jquery-ui/themes/base/jquery.ui.all.css">
    <link rel="stylesheet" href="${basepath}/resource/validator-0.7.0/jquery.validator.css" />

<#--<script type="text/javascript" src="${basepath}/resource/js/jquery-1.4.2.min.js"></script>-->
    <script type="text/javascript" src="${basepath}/resource/js/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${basepath}/resource/zTree3.5/js/jquery.ztree.all-3.5.min.js"></script>

    <script type="text/javascript" src="${basepath}/resource/js/jquery.blockUI.js"></script>
    <script type="text/javascript" src="${basepath}/resource/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${basepath}/resource/js/manage.js"></script>

    <#--<script src="${basepath}/resource/jquery-jquery-ui/jquery-1.5.1.js"></script>-->
    <script src="${basepath}/resource/jquery-jquery-ui/ui/jquery.ui.core.js"></script>
    <script src="${basepath}/resource/jquery-jquery-ui/ui/jquery.ui.widget.js"></script>
    <script src="${basepath}/resource/jquery-jquery-ui/ui/jquery.ui.tabs.js"></script>
    <!-- jquery validator -->

    <script type="text/javascript" src="${basepath}/resource/validator-0.7.0/jquery.validator.js"></script>
    <script type="text/javascript" src="${basepath}/resource/validator-0.7.0/local/zh_CN.js"></script>


</head>
    <#if nobody>
        <#nested />
    <#else >
    <body>
        <#nested />
    </body>
    </#if>
</html>
</#macro>
