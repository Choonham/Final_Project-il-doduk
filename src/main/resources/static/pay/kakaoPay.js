
//결제 팝업
$(document).ready(function(){
    //결제 팝업창으로 띄우기
    $("#btn_submit").on("click",function(){
        window.open("/kakaoPay/kakao","popup_cash","width=500, height=584, scrollbars=no, resizeable=no,toolbar=no, left=420,top=90");
    });


//환불 관련
    $('#refund_checkBtn').click(function (e) {

        if($("input:checkbox[name=refund_check]").is(":checked") == true){
            e.preventDefault();
            var check = confirm("정말 환불하시겠습니까???");
            if(check){
                alert("환불 신청되었습니다..")
                $("#refundForm")[0].submit();
                self.close();
            } else {
                alert("취소되었습니다.")
                self.close();
                return false;
            }
        } else {
            alert("약관에 동의 하지 않으셨습니다. 다시 한번 확인해주세요.");
            return false;
        }

    });

});