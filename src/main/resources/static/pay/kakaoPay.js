
//결제 팝업
$(document).ready(function(){
    //결제 팝업창으로 띄우기
    $("#btn_submit").on("click",function(){
        window.open("/kakaoPay/kakao","popup_cash","width=500, height=584, scrollbars=no, resizeable=no,toolbar=no, left=420,top=90");
    });




});