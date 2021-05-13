  <template>
    <div class="gray_bg">
      <van-nav-bar title="发现" left-text="返回" left-arrow :fixed="true" :placeholder="true">
        <template #right>
          <van-icon name="search" size="18"/>
        </template>
      </van-nav-bar>

      <!--    avatar -->
      <van-cell-group class="cell-group">
        <van-cell :center="true" is-link>
          <van-row type="flex">
            <van-col span="6">
              <van-image
                  style="padding: 1rem"
                  width="5rem"
                  height="5rem"
                  src="https://img01.yzcdn.cn/vant/cat.jpeg"
              />
            </van-col>
            <van-col span="16">
              <van-cell-group class="single_text" style="margin-left: 2rem;padding-top: 1rem">
                <van-cell style="">
                  <span style="font-size: 1rem">{{userInfo.nickname}}</span>
                </van-cell>
                <van-cell>
                  <span style="overflow: hidden; white-space: nowrap">GIM: {{userInfo.username}}</span>
                </van-cell>
              </van-cell-group>
            </van-col>
          </van-row>
        </van-cell>

        <!--    func tree  -->
      </van-cell-group>
      <van-cell-group class="cell-group">
        <van-cell title="支付" is-link/>
      </van-cell-group>
      <van-cell-group class="cell-group">
        <van-cell title="收藏" is-link/>
        <van-cell title="朋友圈" is-link/>
        <van-cell title="卡包" is-link/>
        <van-cell title="表情" is-link/>
      </van-cell-group>


      <van-cell-group class="cell-group">
        <van-cell title="设置" is-link/>
      </van-cell-group>

    </div>
  </template>

  <script>
  export default {
    name: "MeIndex",
    data() {
      return {
        userInfo : {
          id : '16156645156651',
          username : 'qq1179346492',
          nickname : 'j丶at'
        }
      }
    }
  }
  </script>

  <style scoped>

  .cell-group {
    margin-bottom: 5px;
  }
  </style>