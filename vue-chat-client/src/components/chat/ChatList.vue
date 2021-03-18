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
      <ChatItem v-for="item in list"  :user-chat-item="item"/>
    </van-list>
  </div>
</template>

<script>
import ChatItem from "@/components/chat/ChatItem";

export default {
  name: "ChatList",
  components: {ChatItem},
  data() {
    return {
      loading: false,
      finished: false,
      list: [
        {
          nickname: '张大仙',
          content: '我是张大仙!',
          timestamp: '22:21:21'
        }, {
          nickname: '张大仙',
          content: '我是张大仙!',
          timestamp: '22:21:21'
        }, {
          nickname: '张大仙',
          content: '我是张大仙!',
          timestamp: '22:21:21'
        }, {
          nickname: '张大仙',
          content: '我是张大仙!',
          timestamp: '22:21:21'
        }, {
          nickname: '张大仙',
          content: '我是张大仙!',
          timestamp: '22:21:21'
        }, {
          nickname: '张大仙',
          content: '我是张大仙!',
          timestamp: '22:21:21'
        }, {
          nickname: '张大仙',
          content: '我是张大仙!',
          timestamp: '22:21:21'
        }, {
          nickname: '张大仙',
          content: '我是张大仙!',
          timestamp: '22:21:21'
        }, {
          nickname: '张大仙',
          content: '我是张大仙!',
          timestamp: '22:21:21'
        }, {
          nickname: '张大仙',
          content: '我是张大仙!',
          timestamp: '22:21:21'
        }, {
          nickname: '张大仙',
          content: '我是张大仙!',
          timestamp: '22:21:21'
        }, {
          nickname: '张大仙',
          content: '我是张大仙!',
          timestamp: '22:21:21'
        }
      ],
    }
  },
  methods: {
    onLoad() {
      // 异步更新数据
      // setTimeout 仅做示例，真实场景中一般为 ajax 请求

    },
  }
}
</script>

<style scoped>

</style>