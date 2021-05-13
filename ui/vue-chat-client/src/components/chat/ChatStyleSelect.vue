<template>

  <div>
    <van-row v-if="data.isMe" type="flex" justify="end">
      <van-col span="14">
        <div style="text-align: end;">
          <p class="mes">{{ data.content }}</p>
        </div>
      </van-col>
      <van-col span="4">
        <van-badge style="transform: translateY(16px)">
          <van-image
              fit="fill"
              src="https://img01.yzcdn.cn/vant/cat.jpeg"
          />
        </van-badge>
      </van-col>
    </van-row>
    <van-row v-else class="">
      <van-col span="4">
        <van-badge style="transform: translateY(15px)">
          <van-image
              fit="fill"
              src="https://img01.yzcdn.cn/vant/cat.jpeg"
          />
        </van-badge>
      </van-col>
      <van-col span="14">
        <div class="">
          <p class="mes arrow">{{ data.content }}</p>
        </div>
      </van-col>
    </van-row>
  </div>
</template>

<script>


export default {
  name: "ChatStyleSelect",
  props: ["data"],
  data() {
    return {

    }
  },

  methods: {


  }
}
</script>

<style scoped>
.chatBox {
  position: relative;
  margin: 12px;
  padding: 5px 8px;
  word-break: break-all;
  background: #ffffff;
  border: 1px solid #989898;
  border-radius: 5px;
  max-width: 180px;
}


.chatBox-left::before {
  content: '';
  position: absolute;
  width: 100px;
  height: 100px;
  border: 50px solid;
  border-color: #3B64BC #3B64BC #3B64BC #bc3b4a;

}

p.mes {
  background-color: #acd9f8;
  border-radius: 5px;
  padding: 10px;
  box-sizing: border-box;
  position: relative;
  text-align: left;
  word-wrap: break-word;
  font-size: 14px;
  word-break: break-all;
  display: inline-block;
  color: #323232;

}

.text {
  display: inline-block;
  position: relative;
  padding: 0 10px;
  max-width: calc(100% - 75px);
  min-height: 35px;
  line-height: 2.1;
  font-size: 15px;
  padding: 6px 10px;
  text-align: left;
  word-break: break-all;
  background-color: #fff;
  color: #000;
  border-radius: 4px;
  box-shadow: 0px 1px 7px -5px #000;
}
</style>