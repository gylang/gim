<template>
  <div style="background: #eeecec; height: 100%">
    <van-nav-bar title="发现" left-text="返回" left-arrow :fixed="true" :placeholder="true">
      <template #right>
        <van-icon name="search" size="18"/>
      </template>
    </van-nav-bar>
    <van-cell-group class="cell-group">
      <van-cell title="朋友圈" is-link/>
    </van-cell-group>
    <van-cell-group class="cell-group">
      <van-cell title="视频号" is-link/>
    </van-cell-group>
    <van-cell-group class="cell-group">
      <van-cell title="扫一扫" is-link/>
      <van-cell title="摇一摇" is-link/>
    </van-cell-group>

    <van-cell-group class="cell-group">
      <van-cell title="看一看" is-link/>
      <van-cell title="搜一搜" is-link/>
    </van-cell-group>

    <van-cell-group class="cell-group">
      <van-cell title="直播和附近" is-link/>
    </van-cell-group>
    <van-cell-group class="cell-group">
      <van-cell title="购物和附近" is-link/>
      <van-cell title="游戏" is-link/>
    </van-cell-group>
    <van-cell-group class="cell-group">
      <van-cell title="小程序" is-link/>
    </van-cell-group>

  </div>
</template>

<script>
export default {
  name: "FindIndex",
  data() {
    return {}
  }
}
</script>

<style scoped>
.cell-group {
  margin-bottom: 5px;
}

</style>