$(document).ready(function () {

//------- 문의 게시판 작성 폼에서 비밀번호 창
    $('#check1').click(function () {
        $("#inputPass").hide();
    });

    $('#check2').click(function () {
        $("#inputPass").show();
    });


//-------- 문의 게시판 수정 및 삭제 버튼 ---------------
    var form = $("form");

    $("#update").click(function () {

        if (!confirm("수정하시겠습니까?")) {
            return;
        }

        form
            .attr("action", "/serviceCenter/update")
            .attr("method", "post")
            .submit();
    });

    $("#delete").click(function () {

        if (!confirm("삭제하시겠습니까?")) {
            return;
        }

        form
            .attr("action", "/serviceCenter/delete")
            .attr("method", "post")
            .submit();
    });


    // 조회 게시판 버튼 클릭시 해당 내역 출력
    $("#none").click(function(){
        $("#notSelected").show();
        $("#payHistory").hide();
        $("#tradeHistory").hide();
    });

    $("#pay_btn").click(function(){
        $("#payHistory").show();
        $("#tradeHistory").hide();
        $("#notSelected").hide();
    });

    $("#trade_btn").click(function(){
        $("#payHistory").hide();
        $("#tradeHistory").show();
        $("#notSelected").hide();
    });

//-------  신고 게시판 ---------

    //해당 유저 눌렀을때 신고 대상으로 나오게
    $("#badUser tr").click(function(){
        var str = ""
        var tdArr = new Array();

        var tr = $(this);
        var td = tr.children();

        td.each(function(i){
            tdArr.push(td.eq(i).text());
        });

        var user = td.eq(0).text();
        str += user;
        $("#fname").val(str);
    });

    //버튼 클릭시 신고글 게시 및 삭제 확인 창
    $("#report").on("click",function(){
       var insertCheck = confirm("신고 하시겠습니까???");
       if(insertCheck){
           $("form").submit();
       } else {
           return false;
       }
    });

    $("#reportDelete").on("click",function(){
        var deleteCheck = confirm("삭제 하시겠습니까???");
        if(deleteCheck){
            $("form").submit();
        } else {
            return false;
        }

    });


});