<template>
  <div>
    <van-nav-bar title="聊天" :fixed="true" :placeholder="true">
      <template #right>
        <van-icon name="search" size="18"/>
      </template>
    </van-nav-bar>
    <van-list
        v-model="loading"
        :finished="finished"
        finished-text="没有更多了"
        @load="onLoad"
    >
      <ChatItem v-for="(item,key,index) in chatMap"  :user-chat-item="item"/>
    </van-list>
  </div>
</template>

<script>
import ChatItem from "@/components/chat/ChatItem";
import socketApi from "@/api/socketApi";

export default {
  name: "ChatList",
  components: {ChatItem},
  props : {
    userChatList : {

    }
  },
  data() {
    return {
      loading: false,
      finished: false,

      chatMap:
          {
            "1000001": {
              isMe: true,
              cmd: socketApi.PRIVATE_CHAT,
              chatType: 1,
              sender: "1000001",
              content: "你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!!",
              nickname: "张大仙",
              style: "mess",
              timestamp: '2021-03-11 14:11:12'
            },
            "1000002": {
              isMe: true,
              cmd: socketApi.PRIVATE_CHAT,
              chatType: 1,
              sender: "1000002",
              content: "你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!!",
              nickname: "张大仙",
              style: "mess",
              timestamp: '2021-03-11 14:11:12'
            },
            "1000003": {
              isMe: true,
              cmd: socketApi.PRIVATE_CHAT,
              chatType: 1,
              sender: "1000003",
              content: "你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!!",
              nickname: "张大仙",
              style: "mess",
              timestamp: '2021-03-11 14:11:12'
            },
            "1000004": {
              isMe: true,
              cmd: socketApi.PRIVATE_CHAT,
              chatType: 1,
              sender: "11111111",
              content: "你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!!",
              nickname: "张大仙",
              style: "mess",
              timestamp: '2021-03-11 14:11:12'
            },
          }

    }
  },
  methods: {
    onLoad() {
      // 存储当前聊天用户

    },
  }
}
</script>

<style scoped>

</style>