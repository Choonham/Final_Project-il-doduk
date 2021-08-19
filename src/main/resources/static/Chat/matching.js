var ws;


window.onload = function () {


    ws = new WebSocket("wss://localhost:9991/chating");
    wsEvt();
}
//to ==recive from == id

for(var i=0; i<list.length; i++){

    if((list[i].to==other&&list[i].from==me)||(list[i].to==me&&list[i].from==other)) {
        $("#chating").append("<p>" + list[i].from + ": " + list[i].message + "_" + list[i].time + "</p>");
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
            if(msg.id==other&&msg.recive==me){
            $("#chating").append("<p>" + msg.id + ": " + msg.text + "_" + time + "</p>");
            }

    }

    document.addEventListener("keypress", function (e) {
        if (e.keyCode == 13) { //enter press
            send(me,you);
        }
    });
}


function send(id, recive) {
    makeText(id, recive);

    var msg = msgreal;
    var time = msg.date;
    ws.send(JSON.stringify(msg));
    $('#chating').append("<p>" + msg.id + ": " + msg.text + "_" + time + "</p>");
    $('#chatting').val("");
}


/*날짜 형식 지정*/
function getCurrentDate()
{
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

    return year +"/"+ month +"/"+ day +"/"+ hour +"/"+ minites +"/"+ seconds;
}

/*메세지 형태 json으로 만들기*/
function makeText(id, recive) {
    // Construct a msg object containing the data the server needs to process the message from the chat client.

    msgreal = {
        type: "message",
        text: document.getElementById("chatting").value,
        id: id,
        date: getCurrentDate().toString(),
        recive: recive
    };
}

$('#but').click(function (){


send(me, you);
});
