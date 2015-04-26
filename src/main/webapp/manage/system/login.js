$(function(){
    if (top.location != self.location) {
        top.location = self.location;
    }
    $("#username").focus();
    $("#password").onkeydown(function(e){

    });
    $("#btnLogin").click(function(){
        $("#formLogin").submit();
    });
});