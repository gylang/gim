function sendMsg(cmd, content, type, ws) {

    let message = {
        cmd,
        type: "json",
        content,
        receive: "default",
        receiverType: type
    };

    send(message, ws);

}

function send(msg, ws) {

    ws.send(JSON.stringify(msg))
}

function chatMsg(content, ws) {

    sendMsg("test:test", content, receiveType.GROUP, ws)
}

const receiveType = {
    PRIVICE: "1",
    GROUP: "2"
};
