var storeMap = {};
// 打开数据库，两个参数（数据库名字，版本号），如果数据库不存在则创建一个新的库
var request;
var db;
// var request = window.indexedDB.open('GIM_chat', '1')
// 数据库操作过程中出错，则错误回调被触发
export default {
    SOURCE_NAME: {

        PRIVATE_CHAT_STORE: "pri_chat",
        GROUP_CHAT_STORE: "group_chat"
    },

    openDB() {
        let indexedDB = window.indexedDB || window.webkitindexedDB;
        console.log(indexedDB)
        request = indexedDB.open('GIM_chat', 12);
        let that = this;
        request.onerror = function (event) {
            console.log("error")
            console.log(event)
            return new Promise(resolve => resolve.call("失败"));
        }
        request.onsuccess = function (event) {
            console.log("success")
            console.log(event)
            db = event.target.result
            // 数据读取
            let priChatStore = db.transaction(that.SOURCE_NAME.PRIVATE_CHAT_STORE, "readwrite").objectStore(that.SOURCE_NAME.PRIVATE_CHAT_STORE)
            let groupChatStore = db.transaction(that.SOURCE_NAME.GROUP_CHAT_STORE, "readwrite").objectStore(that.SOURCE_NAME.GROUP_CHAT_STORE)
            storeMap[that.SOURCE_NAME.PRIVATE_CHAT_STORE] = priChatStore
            storeMap[that.SOURCE_NAME.GROUP_CHAT_STORE] = groupChatStore
            return new Promise(resolve => resolve.call("成功了"));
        }
        request.onupgradeneeded = function (event) {
            console.log("upgradeneeded")
            console.log(event)
            db = event.target.result

            // 创建对象仓库用来存储数据，把id作为keyPath，keyPath必须保证不重复，相当于数据库的主键
            let priChatStore = db.createObjectStore(that.SOURCE_NAME.PRIVATE_CHAT_STORE, {
                keyPath: 'id',
                autoIncrement: true
            })
            // 建立索引，name和age可能重复，因此unique设置为false
            // priChatStore.createIndex('name', 'name', {unique: false})
            // 私信
            priChatStore.createIndex('msgId', 'msgId', {unique: true})
            priChatStore.createIndex('uid', 'uid', {unique: false})
            priChatStore.createIndex('timeStamp', 'timeStamp', {unique: false})


            // 群聊
            let groupChatStore = db.createObjectStore(that.SOURCE_NAME.GROUP_CHAT_STORE, {
                keyPath: "id",
                autoIncrement: true
            })
            groupChatStore.createIndex('msgId', 'msgId', {unique: true})
            groupChatStore.createIndex('uid', 'uid', {unique: false})
            groupChatStore.createIndex('timeStamp', 'timeStamp', {unique: false})
            // 确保在插入数据前对象仓库已经建立
            // priChatStore.transaction.oncomplete = () => {
            //     // 将数据保存到数据仓库
            //     var usersObjectStore = db.transaction('users', 'readwrite').objectStore('users')
            //     data.forEach(data => {
            //         usersObjectStore.add(data)
            //     })
            // }
            storeMap[that.SOURCE_NAME.PRIVATE_CHAT_STORE] = priChatStore
            storeMap[that.SOURCE_NAME.GROUP_CHAT_STORE] = groupChatStore
            return new Promise(resolve => resolve.call("成功了"));
        }
    },

    getObjectStore(storeName) {
        console.log(storeName)
        console.log(storeMap)
        console.log(storeMap[storeName])
        return db.transaction(storeName, 'readwrite').objectStore(storeName)
    }

}

