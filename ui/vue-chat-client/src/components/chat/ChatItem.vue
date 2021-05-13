<template>


  <van-row ustify="space-around" align="center" @click="openChat">
    <van-col span="6">
      <van-badge style="margin: 10px" :content="5">
        <van-image
            fit="fill"
            src="https://img01.yzcdn.cn/vant/cat.jpeg"
        />
      </van-badge>
    </van-col>
    <van-col span="14">
      <van-cell-group>

        <van-cell class="ipt-non-border-bottom">
          <span style="font-size: 1rem">{{ chatUser.nickname }}</span>
        </van-cell>
        <van-cell class="ipt-non-border-bottom">
          <div style="overflow: hidden; white-space: nowrap">GIM: {{ chatUser.content }}</div>
        </van-cell>
      </van-cell-group>

    </van-col>
    <van-col span="4">
      <div class="min-word color-gray" style="margin-top: 5px">{{ chatUser.timestamp }}</div>
    </van-col>


  </van-row>

</template>

<script>
export default {
  name: "ChatItem",
  props: ["userChatItem"],
  data() {
    return {
      chatUser: this.userChatItem
    }
  },
  methods : {
    openChat() {
    this.$router.push({name: 'ChatIndex', params: {uid: this.chatUser.uid}})
    }
  }
}
</script>

<style scoped>

</style>