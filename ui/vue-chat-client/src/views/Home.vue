<template>
  <div>
    <ChatList v-if="active===0"/>
    <UserList v-else-if="active===1"/>
    <FindIndex v-else-if="active===2"/>
    <MeIndex v-else-if="active===3"/>
    <van-tabbar v-model="active">
      <van-tabbar-item icon="chat-o" badge="9">消息</van-tabbar-item>
      <van-tabbar-item icon="friends-o">通讯录</van-tabbar-item>
      <van-tabbar-item icon="fire-o">发现</van-tabbar-item>
      <van-tabbar-item icon="user-o">我的</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script>
// @ is an alias to /src

import MeIndex from "@/components/me/MeIndex";
import FindIndex from "@/components/find/FindIndex";
import ChatList from "@/components/chat/ChatList";
import UserList from "@/components/user/UserIndex";
import ChatIndex from "@/components/user/UserIndex";

export default {
  name: 'Home',
  components: {ChatIndex, MeIndex, FindIndex, ChatList, UserList},
  data() {
    return {
      active: 0
    }
  }
}
</script>
