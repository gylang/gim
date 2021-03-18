<template>
  <div>
    <van-nav-bar title="通讯录" :left-text="index" :fixed="true" :placeholder="true">
      <template #right>
        <van-icon name="search" size="18"/>
      </template>
    </van-nav-bar>

    <van-index-bar :sticky="false" @change="selectIndex" @select="selectIndex" :index-list="Object.keys(userList)">
      <div v-for="(item,key) in userList">
        <van-index-anchor class="gray_bg" :index="key">{{ key }}</van-index-anchor>
        <van-cell v-for="entity in item" :title="entity.nickname"/>
      </div>
    </van-index-bar>
  </div>
</template>

<script>

export default {
  name: "UserIndex",
  components: {},
  data() {
    return {
      list: [],
      index: '',
      loading: false,
      finished: false,
      userList: {
        "A": [
          {nickname: "A路人1"},
          {nickname: "A路人2"}
        ],
        "B": [
          {nickname: "B路人1"},
          {nickname: "B路人2"}
        ],
        "C": [
          {nickname: "C路人1"},
          {nickname: "C路人2"}
        ],
        "D": [
          {nickname: "D路人1"},
          {nickname: "D路人2"}
        ],
        "E": [
          {nickname: "E路人1"},
          {nickname: "E路人2"}
        ],
        "F": [
          {nickname: "F路人1"},
          {nickname: "F路人2"}
        ],
        "H": [
          {nickname: "H路人1"},
          {nickname: "H路人2"}
        ],
        "J": [
          {nickname: "J路人1"},
          {nickname: "J路人2"}
        ],
        "K": [
          {nickname: "K路人1"},
          {nickname: "K路人2"}
        ],
        "L": [
          {nickname: "L路人1"},
          {nickname: "L路人2"}
        ],
        "M": [
          {nickname: "M路人1"},
          {nickname: "M路人2"}
        ],
        "N": [
          {nickname: "N路人1"},
          {nickname: "N路人2"}
        ],
        "O": [
          {nickname: "O路人1"},
          {nickname: "O路人2"}
        ],
        "P": [
          {nickname: "P路人1"},
          {nickname: "P路人2"}
        ],
        "Q": [
          {nickname: "Q路人1"},
          {nickname: "Q路人2"}
        ],
        "R": [
          {nickname: "R路人1"},
          {nickname: "R路人2"}
        ],
        "S": [
          {nickname: "S路人1"},
          {nickname: "S路人2"}
        ],
        "T": [
          {nickname: "T路人1"},
          {nickname: "T路人2"}
        ],
        "U": [
          {nickname: "U路人1"},
          {nickname: "U路人2"}
        ],
        "V": [
          {nickname: "V路人1"},
          {nickname: "V路人2"}
        ],
        "W": [
          {nickname: "W路人1"},
          {nickname: "W路人2"}
        ],
        "X": [
          {nickname: "X路人1"},
          {nickname: "X路人2"}
        ],
        "Y": [
          {nickname: "Y路人1"},
          {nickname: "Y路人2"}
        ],
        "Z": [
          {nickname: "Z路人1"},
          {nickname: "Z路人2"}
        ],
      }
    }
  },
  methods: {
    onLoad() {
      // 异步更新数据
      // setTimeout 仅做示例，真实场景中一般为 ajax 请求
      setTimeout(() => {
        for (let i = 0; i < 10; i++) {
          this.list.push(this.list.length + 1);
        }

        // 加载状态结束
        this.loading = false;

        // 数据全部加载完成
        if (this.list.length >= 40) {
          this.finished = true;
        }
      }, 10);
    },
    selectIndex(Event) {
      this.index = Event;
    }
  }
}
</script>

<style scoped>
.van-index-anchor {

}
</style>