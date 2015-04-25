$(function(){
    $("#username").focus();
    if (top.location != self.location) {
        top.location = self.location;
    }
    $("#btnLogin").click(function(){
        $("#formLogin").submit();
    });
});