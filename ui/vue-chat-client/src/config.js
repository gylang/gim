var evn = "dev";

var dev = {
    socketUrl: "ws://127.0.0.1:46000/ws"
}
var prod = {
    socketUrl: "ws://127.0.0.1:46000/ws"
}
export default {


    getConfig() {

        if (evn === "dev") {
            return dev;
        } else {
            return prod;
        }
    }


}