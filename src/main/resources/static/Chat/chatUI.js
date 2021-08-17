var ws;


window.onload = function () {


    ws = new WebSocket("wss://localhost:9991/chating");
    wsEvt();
}
//to ==recive from == id
//||(list[i].recive==me&&list[i].send==you)
for (var i = 0; i < list.length; i++) {
    //메세지 받을 떄
    if (list[i].recive == me) {


        var newMessage = "<article class=\"msg-container msg-remote\" id=\"msg-0\"><div class=\"msg-box\"><img class=\"user-img\" id=\"user-0\" src="+recivePhoto+" /><div class=\"flr\"><div class=\"messages\"><p class=\"msg\" id=\"msg-0\">"+ list[i].message +"</p></div><span class=\"timestamp\"><span class=\"username\">"+other +"</span>&bull;<span class=\"posttime\">3 minutes ago</span></span></div></div></article>";
        $('#chatwindow').append(newMessage);
    }//보낼 때
    else{

        var newMessage =" <article class=\"msg-container msg-self\" id=\"msg-0\"><div class=\"msg-box\"><div class=\"flr\"><div class=\"messages\"><p class=\"msg\" id=\"msg-1\">" + list[i].message + "</p> </div><span class=\"timestamp\"><span class=\"username\">"+me +"</span>&bull;<span class=\"posttime\">2 minutes ago</span></span></div><img class=\"user-img\" id=\"user-0\" src="+myPhoto+" /></div></article>";
        $('#chatwindow').append(newMessage);
    }
}


var msgreal;


function wsEvt() {
    ws.onopen = function (data) {
        //소켓이 열리면 초기화 세팅하기
        console.log(data);
    }

    ws.onmessage = function (data) {
        var msg = JSON.parse(data.data);
        var time = msg.date;
        if (msg.send == other && msg.recive == me&&msg.auc==auc_seq) {
            var newMessage = "<article class=\"msg-container msg-remote\" id=\"msg-0\"><div class=\"msg-box\"><img class=\"user-img\" id=\"user-0\" src="+recivePhoto+" /><div class=\"flr\"><div class=\"messages\"><p class=\"msg\" id=\"msg-0\">"+ msg.text +"</p></div><span class=\"timestamp\"><span class=\"username\">"+other +"</span>&bull;<span class=\"posttime\">3 minutes ago</span></span></div></div></article>";
            $('#chatwindow').append(newMessage);
            $('#chatwindow').scrollTop=  $('#chatwindow').scrollHeight;

        }

    }

    document.addEventListener("keypress", function (e) {
        if (e.keyCode == 13) { //enter press
            send(id, other,auc_seq);
            $('#chatwindow').scrollTop=  $('#chatwindow').scrollHeight;
        }
    });
}


function send(id,recive,auc_seq) {
    makeText(id, recive,auc_seq);

    var msg = msgreal;
    var time = msg.date;
    ws.send(JSON.stringify(msg));
    var newMessage =" <article class=\"msg-container msg-self\" id=\"msg-0\"><div class=\"msg-box\"><div class=\"flr\"><div class=\"messages\"><p class=\"msg\" id=\"msg-1\">" +$('#message').val() + "</p> </div><span class=\"timestamp\"><span class=\"username\">"+me +"</span>&bull;<span class=\"posttime\">2 minutes ago</span></span></div><img class=\"user-img\" id=\"user-0\" src="+myPhoto+" /></div></article>";
    $('#chatwindow').append(newMessage);
    $('#message').val("");
}


/*날짜 형식 지정*/
function getCurrentDate() {
    var date = new Date();
    var year = date.getFullYear().toString();

    var month = date.getMonth() + 1;
    month = month < 10 ? '0' + month.toString() : month.toString();

    var day = date.getDate();
    day = day < 10 ? '0' + day.toString() : day.toString();

    var hour = date.getHours();
    hour = hour < 10 ? '0' + hour.toString() : hour.toString();

    var minites = date.getMinutes();
    minites = minites < 10 ? '0' + minites.toString() : minites.toString();

    var seconds = date.getSeconds();
    seconds = seconds < 10 ? '0' + seconds.toString() : seconds.toString();

    return year + "/" + month + "/" + day + "/" + hour + "/" + minites + "/" + seconds;
}

/*메세지 형태 json으로 만들기*/
function makeText(id, recive,auc_seq) {
    // Construct a msg object containing the data the server needs to process the message from the chat client.

    msgreal = {
        type: "message",
        text: document.getElementById("message").value,
        send: id,
        date: getCurrentDate().toString(),
        recive: recive,
        auc: auc_seq
    };
}

/*$('#send').click(function (){



});*/

$('#send').click(function () {

    send(me, other,auc_seq);
    $('#chatwindow').scrollTop=  $('#chatwindow').scrollHeight;
});






