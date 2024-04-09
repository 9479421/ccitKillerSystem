<template>
  <div>
    <a-button
      type="danger"
      style="margin-bottom:10px"
      @click="() => (this.visible = true)"
      >如何获取laravel_session?</a-button
    >
    <br />
    <a-textarea
      v-model="session"
      placeholder="请输入青年大学习laravel_session"
      :rows="2"
      style="width:60%;margin-bottom:10px;"
    />
    <br />
    <a-button type="primary" style="margin-bottom:10px;" @click="testSession"
      >测试获取数据</a-button
    >
    <br />
    <a-textarea
      :rows="4"
      v-model="testData"
      style="width:60%;margin-bottom:10px;text-align:center;color:black"
      disabled
    />
    <a-input
      v-model="qq"
      type="number"
      style="width:50%;margin-bottom:10px;"
      placeholder="请输入QQ号(用来接收任务完成提醒的QQ,还要确保该QQ的邮箱可用)"
    />
    <a-input
      v-model="phone"
      type="number"
      style="width:50%;margin-bottom:10px;"
      placeholder="请输入手机号(用来接收任务完成提醒)"
    />
    <br />
    <a-input
      v-model="cardID"
      style="width:50%;margin-bottom:10px;"
      placeholder="请输入授权码(联系作者获取)"
    />
    <br />
    <a-button type="primary" @click="commit()">提交队列</a-button>

    <a-modal on-ok="handleOk" :destroyOnClose="true" centered title="Title" v-model="visible">
      <p>请全屏观看以下教程</p>
      <a-button @click="openDownload()" type="primary">下载Fiddler</a-button><span style="margin:10px">访问密码：1111</span>
      <template slot="footer">
        <video-player
          class="video-player vjs-custom-skin"
          ref="videoPlayer"
          :playsinline="true"
          :options="playerOptions"
        >
        </video-player>
      </template>
    </a-modal>
  </div>
</template>

<script>
export default {
  data() {
    return {
      session: "",
      cacheSession: "",
      testData: "",
      qq: "",
      phone: "",
      cardID: "",

      visible: false,

      playerOptions: {
        //播放速度
        playbackRates: [0.5, 1.0, 1.5, 2.0],
        //如果true,浏览器准备好时开始回放。
        autoplay: false,
        // 默认情况下将会消除任何音频。
        muted: false,
        // 导致视频一结束就重新开始。
        loop: false,
        // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
        preload: "auto",
        language: "zh-CN",
        // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
        aspectRatio: "16:9",
        // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
        fluid: true,
        sources: [
          {
            //类型
            type: "video/mp4",
            //url地址
            src:
              "https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/bandicam%202022-05-06%2019-40-00-273.mp4"
          }
        ],
        //你的封面地址
        poster:
          "https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/img/20220506194520.png",
        //允许覆盖Video.js无法播放媒体源时显示的默认信息。
        notSupportedMessage: "此视频暂无法播放，请稍后再试",
        controlBar: {
          timeDivider: true,
          durationDisplay: true,
          remainingTimeDisplay: false,
          //全屏按钮
          fullscreenToggle: true
        }
      }
    };
  },
  methods: {
    testSession() {
      this.$axios.get("testSession?session=" + this.session).then(res => {
        if (res.data.code == 200) {
          this.$message.success(res.data.msg);
          this.testData = res.data.data;
          this.cacheSession = this.session;
        } else {
          this.$message.error(res.data.msg);
          this.testData = "";
          this.cacheSession = "";
        }
      });
    },
    commit() {
      if (this.cacheSession == "") {
        this.$message.error("请先获取laravel_session再提交");
        return;
      }
      if (this.qq.length < 5) {
        this.$message.error("请输入正确的QQ号");
        return;
      }
      if (this.phone.length != 11) {
        this.$message.error("请输入正确的手机号");
        return;
      }
      this.$axios
        .post(
          "addTask_qndxx",
          "session=" +
            this.cacheSession +
            "&qq=" +
            this.qq +
            "&phone=" +
            this.phone +
            "&cardID=" +
            this.cardID
        )
        .then(res => {
          if (res.data.code == 200) {
            this.$message.success(res.data.msg);
            this.qq = "";
            this.phone = "";
            this.cardID = "";
            this.cacheSession = "";
            this.session = "";
            this.testData = "";
          } else {
            this.$message.error(res.data.msg);
          }
        });
    },
    openDownload(){
      window.open('https://wws.lanzouh.com/ingiq04cuuna','_blank')
    }
  }
};
</script>

<style scoped>
.demo {
  display: inline-block;
  width: 600px;
  height: 338px;
  text-align: center;
  line-height: 100px;
  border: 1px solid transparent;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;
  position: relative;
  box-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
  margin-right: 4px;
}

.demo:hover {
  display: block;
}
</style>
