import IndexDB from "@/util/IndexedDBStrategy";

export default {

    // 获取最新消息
    getPrivateChat(uid) {
        // 获取数据源
        let objectStore = IndexDB.getObjectStore(IndexDB.SOURCE_NAME.PRIVATE_CHAT_STORE);

        // 获取最后记录的id
        // 打开迭代器
        objectStore.openCursor(IDBKeyRange.only({"uid": uid})
            .upperBound({"timeStamp": new Date().getTime()}, true), "prev")
            .onsuccess = ev => {
            return ev.target.result;

        }
    },

    setPrivateChat(message) {

        let objectStore = IndexDB.getObjectStore(IndexDB.SOURCE_NAME.PRIVATE_CHAT_STORE);
        let result = this.setData(objectStore, message);
        if (result.onerror) {
            console.log("设置值失败")
            console.log(result.onerror)
        } else {
            console.log("设置值成功")
            console.log(result.onsuccess)
        }

    },
    setGroupChat(message) {

        let objectStore = IndexDB.getObjectStore(IndexDB.SOURCE_NAME.GROUP_CHAT_STORE);
        let result = this.setData(objectStore, message);
        if (result.onerror) {
            console.log("设置值失败")
            console.log(result.onerror)
        } else {
            console.log("设置值成功")
            console.log(result.onsuccess)
        }

    },
    /**
     * 存储当前正在聊天的会话
     * @param message
     */
    setCurrentChat(message) {

    },

    /**
     * 存储当前正在聊天的会话
     * @param message
     */
    getCurrentChat() {

    },

    setData(store, data) {

        let ret = {};
        if (null != store) {
            console.log(data)
            let result = store.add(data);
            result.onsuccess = (ev) => {
                console.log(ev)
                ret.onerror = ev
            };
            result.onerror = (ev) => {
                ret.onsuccess = ev
            };
        } else {
            ret.onerror = "数据源不存在"
        }
        return ret;
    }
}