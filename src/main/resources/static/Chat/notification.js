

// 페이지가 오픈되는 동시에 소켓 열기
window.onload = function () {
    ws = new WebSocket("wss://localhost:9991/chating");


    wsEvt();
}



function wsEvt() {
    ws.onopen = function (data) {
        //소켓이 열리면 초기화 세팅하기
        console.log(data);
    }



    ws.onmessage = function (data) {
        var msg = JSON.parse(data.data);
        var time = msg.date;
        if(Notification.permission==="granted"){
            if(msg.recive===me) {
                showNotification(msg);
            }
        }


        else if(Notification.permission!=='denied'){
            Notification.requestPermission().then(permission =>{
                if(permission==="granted"){

                    if(msg.from===me){
                        showNotification(msg);

                    }


                }

            });
        }




    }

}
var window1=null;

function showNotification(msg){
    const notification = new Notification(msg.send,{
        body:msg.text,
        icon: "https://i.ibb.co/vZT6bkR/F9bbc0a9764cd542.png"
    });
    notification.onclick = function(event) {
        event.preventDefault();

        if(window1==null){

        window1=window.open("https://localhost:9991/chat/chatting?id="+msg.send+"&auc="+msg.auc,'_blank','width=300 , height=400');

        }else {
            window1.focus();
        }


    }

}

