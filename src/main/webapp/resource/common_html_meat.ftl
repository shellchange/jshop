<#macro htmlBase title="JEESHOP" jsFiles=[] cssFiels=[] nobody=false>
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
