function sendMsg(key, content, type, ws) {

    let message = {
        key,
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

    sendMsg("chat", content, receiveType.GROUP, ws)
}

const receiveType = {
    PRIVICE: "1",
    GROUP: "2"
};