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
        <ChatStyleSelect v-for="item in list" :data="item"/>
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
        <van-col class="h100p" span="3">
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
export default {
  name: "ChatIndex",
  components: {ChatStyleSelect, ChatItem},
  data() {
    return {
      loading: false,
      finished: true,
      active: true,
      uid : '',
      list: [
        {
          isMe : true,
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!!",
          nickname: "张大仙",
          style: "mess",
          isMe : true,

          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          style: "mess",
          isMe : true,

          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          isMe : true,

          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!你好呀!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          isMe : true,

          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          isMe : true,

          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          isMe : true,

          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          isMe : true,

          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        }, {
          cmd: socketApi.PRIVATE_CHAT,
          targetId: "11111111",
          content: "你好呀!",
          nickname: "张大仙",
          isMe : true,
          style: "mess",
          timestamp: '2021-03-11 14:11:12'
        },
      ],
      refreshing: false,
      userInfo: {
        nickname: "张大仙",
        style: "mess",
        timestamp: '2021-03-11 14:11:12'
      },
      content: ''

    }
  },
  methods: {
    onLoad() {
      // 异步更新数据
      // setTimeout 仅做示例，真实场景中一般为 ajax 请求
      ChatStoreUtil.getPrivateChat()
    },
    onRefresh() {
      // 清空列表数据
      // this.finished = false;

      // 重新加载数据
      // 将 loading 设置为 true，表示处于加载状态
      this.loading = true;
      this.onLoad();
    },
  }
}
</script>

<style scoped>
.circle {

  radius: 50%;
  border: 1px;
}
</style>