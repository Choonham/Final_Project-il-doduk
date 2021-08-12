

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
function showNotification(msg){
    const notification = new Notification(msg.id,{
        body:msg.text
    });
}


console.log(Notification.permission);
function get_chat(id,auc){
    window.open("https://localhost:9991/chat/chatting?id="+id);
}
