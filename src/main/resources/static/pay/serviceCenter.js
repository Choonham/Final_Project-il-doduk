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
});