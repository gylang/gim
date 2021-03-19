import IndexDB from "@/util/IndexedDBStrategy";

export default {

    // 获取最新消息
    getPrivateChat(uid, consumer) {
        // 获取数据源
        let objectStore = IndexDB.getObjectStore(IndexDB.SOURCE_NAME.PRIVATE_CHAT_STORE);

        // 获取最后记录的id
        let lastId = localStorage.getItem(uid)
        // 打开迭代器
        objectStore.openCursor(IDBKeyRange.only({"uid": uid})
            .upperBound({"timeStamp": new Date().getTime()}, true), "prev")
            .onsuccess = ev => {

            consumer(ev)
            cursor.continue(); // 移到下一个位置
        }
    },

    setPrivateChat(message) {

        let uid = message.targetId;
        let objectStore = IndexDB.getObjectStore(IndexDB.SOURCE_NAME.PRIVATE_CHAT_STORE);
        console.log(message)
        console.log(objectStore)
        let result = this.setData(objectStore, message);
        if (result.onerror) {
            console.log("设置值失败")
            console.log(result.onerror)
        } else {
            console.log("设置值成功")
            console.log(result.onsuccess)
        }

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