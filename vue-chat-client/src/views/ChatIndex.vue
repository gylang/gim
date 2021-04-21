<template>
  <div>
    <!--    顶部 -->
    <van-nav-bar :title="userInfo.nickname" left-arrow :fixed="true" :placeholder="true">
      <template #right>
        <van-icon name="ellipsis" size="18"/>
      </template>
    </van-nav-bar>

    <!--    聊天主窗口-->
    <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
      <van-list
          v-model="loading"
          :finished="finished"
          finished-text="没有更多了"
          @load="onLoad"
      >
        <ChatStyleSelect v-for="item in chatList" :data="item"/>
      </van-list>
    </van-pull-refresh>

    <!--    底部 -->
    <van-tabbar style="height: auto;min-height: 40px">
      <van-row class="w100p h100p" style="padding-top: 10px">
        <van-col class="h100p circle" span="3">
          <van-icon size="30" name="volume-o"/>
        </van-col>
        <van-col class="h100p" span="15">
          <van-field
              v-model="content"
              @input="showSendBtnHaveText"
              :autosize="{ maxHeight: 100, minHeight: 30}"
              style="padding: 0"
              type="textarea"
              maxlength="500"
              row="4"
              placeholder="请输入消息"
          />
        </van-col>
        <van-col class="h100p" span="3">
          <van-icon size="30" name="smile-o"/>
        </van-col>
        <van-col v-show="showSendBtn" class="h100p" span="3">
          <van-button type="primary" @click="sendPrivateMsg">发送</van-button>
        </van-col>
        <van-col v-show="!showSendBtn" class="h100p" span="3">
          <van-icon size="30" name="add-o"/>
        </van-col>


      </van-row>
    </van-tabbar>
  </div>
</template>

<script>
import ChatItem from "@/components/chat/ChatItem";
import socketApi from "@/api/socketApi";
import ChatStyleSelect from "@/components/chat/ChatStyleSelect";
import ChatStoreUtil from "@/util/ChatStoreUtil";
import socket from "@/util/socket";

export default {
  name: "ChatIndex",
  components: {ChatStyleSelect, ChatItem},
  data() {
    return {
      loading: false,
      finished: true,
      active: true,
      uid: '',
      showSendBtn: false,
      chatList: [
        {
          isMe: true,
          cmd: socketApi.PRIVATE_CHAT,
          chatType : 1,
          targetId: "11111111",
          content: "你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          chatType : 1,
          content: "你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          chatType : 1,
          content: "你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!!",
          nickname: "张大仙",
          style: "mess",
          isMe: true,

          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          chatType : 1,
          content: "你好呀!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          chatType : 1,
          content: "你好呀!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          chatType : 1,
          content: "你好呀!",
          nickname: "张大仙",
          style: "mess",
          isMe: true,

          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          chatType : 1,
          content: "你好呀!",
          isMe: true,
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }
      ],
      refreshing: false,
      userInfo: {
        nickname: "张大仙",
        style: "mess",
        timestamp: '2021-03-11 14:11:12',
        uid: '1111111'
      },
      content: ''

    }
  },
  methods: {
    onLoad() {
      // 异步更新数据
      // setTimeout 仅做示例，真实场景中一般为 ajax 请求

    },
    showSendBtnHaveText() {
      console.log(this.content)
      if ('' !== this.content) {
        if (!this.showSendBtn) {
          this.showSendBtn = true;
        }
      } else {
        if (this.showSendBtn) {
          this.showSendBtn = false;
        }
      }
    },
    onRefresh() {
      // 清空列表数据
      // this.finished = false;

      // 重新加载数据
      // 将 loading 设置为 true，表示处于加载状态
      this.loading = true;
      this.onLoad();
    },
    sendPrivateMsg() {

      let msg = {
        content: this.content,
        receiveId: this.userInfo.uid,
        targetId: this.userInfo.uid,
        cmd: socketApi.PRIVATE_CHAT,
        type: socketApi.type.BIZ_MSG,
        qos: true
      };
      this.content = '';

      // 发送socket
      socket.send(msg)
    },
    listener(msg) {
      // 接受到异步socket消息
      // 判断是否为当前用户
      if (this.data.sender === msg.sender) {

        // 加入到当前消息当中

        this.chatList.concat(msg);
      }
    },

    loadMsg() {
      if (this.cursor) {
        let cursor = this.cursor;
        // 加载数据 每次加载20条
        let num = 0;
        let chatList = this.chatList;
        for (let i; i < num; i++) {

          // 先实现插入 后面再改顺序问题

          chatList.concat(cursor.value);
          // 下一条
          cursor.continue();
        }

      }
    }
  },
  created() {

    // 监听消息 并加载indexedDB消息
    this.cursor = ChatStoreUtil.getPrivateChat(this.data.targetId);
    // 监听实时socket
    socket.bindListener(socketApi.PRIVATE_CHAT, this.listener)
  },
  destroyed() {
    socket.unBindListener(socketApi.PRIVATE_CHAT, this.listener)
  },
}
</script>

<style scoped>
.circle {

  radius: 50%;
  border: 1px;
}
</style>