var ws = new WebSocket("ws://localhost:46000/ws");

//申请一个WebSocket对象，参数是服务端地址，同http协议使用http://开头一样，WebSocket协议的url使用ws://开头，另外安全的WebSocket协议使用wss://开头

function login() {
    console.log('ws.state() : ' + ws.readyState);
    console.log('ws.OPEN : ' + ws.OPEN);
    if (ws.readyState === ws.OPEN) {
        console.log("登录");
        // 登录
        let user = $("#inputUser").val();
        console.log('user : ' + user);
        sendMsg("login", user, receiveType.GROUP, ws);
        $(".inputUser").css("display", "none");
        $(".c-container").css("display", "block");
    }
}

ws.onopen = function () {
    //当WebSocket创建成功时，触发onopen事件
    console.log("open");


    setTimeout(function () {
        send({}, ws)
    }, 3000)

};
ws.onmessage = function (e) {
    //当客户端收到服务端发来的消息时，触发onmessage事件，参数e.data包含server传递过来的数据
    console.log(e.data);

    var parse = JSON.parse(e.data);
    $(".chat-container").append('<div class="receiveMsg"><span style="color: chocolate">【 ' + parse.sender + '】</span>: ' + parse.content + '</div><br>')

};
ws.onclose = function (e) {
    //当客户端收到服务端发送的关闭连接请求时，触发onclose事件
    console.log("close");
};
ws.onerror = function (e) {
    //如果出现连接、处理、接收、发送数据失败的时候触发onerror事件
    console.log(error);
};
