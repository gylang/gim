import config from "@/config";
import api from "@/api";
import socketApi from "@/api/socketApi";
import ChatStoreUtil from "@/util/ChatStoreUtil";

var listenerMap = new Map
var socket = null;
var onOpen = null;
var status = WebSocket.CLOSED
var errorNum = 0;


export default {

    /**
     * 绑定监听器
     * @param k cmd 命令值
     * @param callback 回调方法
     */
    bindListener(k, callback) {
        let listenerList = listenerMap.get(k) || new Set();
        if (!listenerList) {
            console.log("执行了吗")
            listenerMap.set(k, listenerList)
        }
        listenerList.add(callback)
    },
    unBindListener(k, callback) {
        let listenerList = listenerMap.get(k);
        if (listenerList) {
            listenerList.delete(callback)
        }
    },
    /**
     * 连接方法
     * @param open 打开websocket
     * @returns {WebSocket} websocket对象
     */
    connect(open) {
        if (typeof (WebSocket) === "undefined") {
            console.log("您的浏览器不支持socket")
        } else {
            // 实例化socket
            status = WebSocket.CONNECTING
            console.log(config.getConfig())
            socket = new WebSocket(config.getConfig().socketUrl);
            // 监听socket连接
            socket.onopen = this.open;
            // 监听socket错误信息
            socket.onerror = this.error;
            // 监听socket消息
            socket.onmessage = this.getMessage;
            // 心跳包
            // this.sendHeartTask();
            onOpen = open;
            return socket;
        }
    },
    /**
     * 手动检查连接状况
     */
    check() {
        let token = sessionStorage.getItem("token");
        if (WebSocket.OPEN !== status) {
            this.sendHeart();
        }
        if (WebSocket.OPEN !== status && 5 < errorNum) {
            console.log("token: " + token)
            // 尝试重连
            if (WebSocket.CONNECTING !== status) {
                this.connect(() => {
                    console.log("连接成功, 发送登录消息")
                    this.login(token);
                    errorNum = 0;
                    status = WebSocket.OPEN;
                })
            }


        }
    },
    /**
     * 定时任务发送心跳包
     */
    // sendHeartTask() {
    //     setInterval(() => {
    //         this.sendHeart();
    //
    //     }, 2000)
    // },
    /**
     * 发送心跳包
     */
    sendHeart() {
        if (socket && WebSocket.CONNECTING !== status) {
            this.connect(this.login)
            return;
        }
        if (socket && (status === WebSocket.OPEN)) {
            socket.send("{}");
            //重连
            console.log("发送心跳")
        } else {
            errorNum = errorNum + 1;
        }
    },
    /**
     * 建立连接事件
     */
    open: function () {
        console.log("socket连接成功")
        status = WebSocket.OPEN
        onOpen()
    },
    /**
     * 连接错误事件
     * @param ev
     */
    error: function (ev) {
        console.log("连接错误")
        console.log(ev)
        status = WebSocket.CLOSING
    },
    getMessage: function (msg) {
        console.log("接收到消息")
        let message = JSON.parse(msg.data)
        console.log(message)
        console.log(message.cmd)
        if ('' !== message.cmd) {
            console.log("查询回调方法")
            console.log(listenerMap)
            let list = listenerMap.get(message.cmd)
            console.log("回调方法如下")
            console.log(list)
            if (list) {
                console.log("迭代调用")
                list.forEach(listener => {
                    listener(message)
                })
            }
        }
    },
    /**
     * 发送消息
     * @param message 消息体
     */
    send(message) {
        console.log(socket)
        socket.send(JSON.stringify(message));
        console.log({
            message: "发送成功",
            type: 'success'
        });
        // 按照消息策略 进行消息保存
        if (socketApi.PRIVATE_CHAT === message.cmd) {
            ChatStoreUtil.setPrivateChat(message)
        } else if (socketApi.GROUP_CHAT === message.cmd) {
            ChatStoreUtil.setg
        }
    },
    /**
     * tcp 连接登录
     * @param token 用户登录返回的token
     */
    login(token) {
        let message = {
            cmd: api.LOGIN_SOCKET,
            content: token,
            msgId: token + "-" + Date.now()
        };
        socket.send(
            JSON.stringify(message)
        )
    },
    close() {
        console.log("socket已经关闭")
        status = WebSocket.CLOSED
    },
    destroyed() {
        // 销毁监听
        socket.onclose = this.close
    }
}