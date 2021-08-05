$(document).ready(function() {
    //여기 아래 부분
    $('#summernote').summernote({
        height: 300,                 // 에디터 높이
        minHeight: null,             // 최소 높이
        maxHeight: null,             // 최대 높이
        focus: true,                  // 에디터 로딩후 포커스를 맞출지 여부
        lang: "ko-KR",					// 한글 설정
        placeholder: '최대 2048자까지 쓸 수 있습니다'	//placeholder 설정

    });
});


var setting = {
    height : 300,
    minHeight : null,
    maxHeight : null,
    focus : true,
    lang : 'ko-KR',
    placeholder: '최대 2048자까지 쓸 수 있습니다',

    //콜백 함수
    callbacks : {
        onImageUpload : function(files, editor, welEditable) {
            // 파일 업로드(다중업로드를 위해 반복문 사용)
            for (var i = files.length - 1; i >= 0; i--) {
                uploadSummernoteImageFile(files[i],
                    this);
            }
        }
    }
};
$('#summernote').summernote(setting);

function uploadSummernoteImageFile(file, el) {
    data = new FormData();
    data.append("file", file);
    $.ajax({
        data : data,
        type : "POST",
        url : "uploadSummernoteImageFile",
        contentType : false,
        enctype : 'multipart/form-data',
        processData : false,
        success : function(data) {
            $(el).summernote('editor.insertImage', data.url);
            $('#editordata').append("<img src="+'+data.url+'+">")
            console.log(data.url);
        }
    });
}

