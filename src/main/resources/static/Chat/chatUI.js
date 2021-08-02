var ws;


window.onload = function () {


    ws = new WebSocket("wss://localhost:9991/chating");
    wsEvt();
}
//to ==recive from == id
//||(list[i].recive==me&&list[i].send==you)
for(var i=0; i<list.length; i++){
    //메세지 받을 떄
    if(list[i].recive==me&&list[i].send==other) {

        $('.chats').append("<span class='u1 chat'>"  + list[i].message +"</span>");
    }//보낼 때
    else {
        $('.chats').append("<span class='u2 chat'>" + list[i].message +"</span>");
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
        if(msg.send==other&&msg.recive==me){

            $('.chats').appendTo("<span class='u1 chat'>" + msg.text + "</span>");
        }

    }

    document.addEventListener("keypress", function (e) {
        if (e.keyCode == 13) { //enter press
            send(me,other);
        }
    });
}


function send(id, recive) {
    makeText(id, recive);

    var msg = msgreal;
    var time = msg.date;
    ws.send(JSON.stringify(msg));
    $('.chats').append("<span class='u2 chat'>"+ $('#message').val()+"</span>");
    $('#message').val("");
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
        text: document.getElementById("message").value,
        send: id,
        date: getCurrentDate().toString(),
        recive: recive
    };
}

$('#send').click(function (){


    send(me, other);
});




$(document).ready(function(){

    var preloadbg = document.createElement("img");
    preloadbg.src = "https://s3-us-west-2.amazonaws.com/s.cdpn.io/245657/timeline1.png";

    $("#searchfield").focus(function(){
        if($(this).val() == "Search contacts..."){
            $(this).val("");
        }
    });
    $("#searchfield").focusout(function(){
        if($(this).val() == ""){
            $(this).val("Search contacts...");

        }
    });

    $("#sendmessage input").focus(function(){
        if($(this).val() == "Send message..."){
            $(this).val("");
        }
    });
    $("#sendmessage input").focusout(function(){
        if($(this).val() == ""){
            $(this).val("Send message...");

        }
    });


    $(".friend").each(function(){
        $(this).click(function(){
            var childOffset = $(this).offset();
            var parentOffset = $(this).parent().parent().offset();
            var childTop = childOffset.top - parentOffset.top;
            var clone = $(this).find('img').eq(0).clone();
            var top = childTop+12+"px";

            $(clone).css({'top': top}).addClass("floatingImg").appendTo("#chatbox");

            setTimeout(function(){$("#profile p").addClass("animate");$("#profile").addClass("animate");}, 100);
            setTimeout(function(){
                $("#chat-messages").addClass("animate");
                $('.cx, .cy').addClass('s1');
                setTimeout(function(){$('.cx, .cy').addClass('s2');}, 100);
                setTimeout(function(){$('.cx, .cy').addClass('s3');}, 200);
            }, 150);

            $('.floatingImg').animate({
                'width': "68px",
                'left':'108px',
                'top':'20px'
            }, 200);

            var name = $(this).find("p strong").html();
            var email = $(this).find("p span").html();
            $("#profile p").html(name);
            $("#profile span").html(email);

            $(".message").not(".right").find("img").attr("src", $(clone).attr("src"));
            $('#friendslist').fadeOut();
            $('#chatview').fadeIn();


            $('#close').unbind("click").click(function(){
                $("#chat-messages, #profile, #profile p").removeClass("animate");
                $('.cx, .cy').removeClass("s1 s2 s3");
                $('.floatingImg').animate({
                    'width': "40px",
                    'top':top,
                    'left': '12px'
                }, 200, function(){$('.floatingImg').remove()});

                setTimeout(function(){
                    $('#chatview').fadeOut();
                    $('#friendslist').fadeIn();
                }, 50);
            });

        });
    });
});





























