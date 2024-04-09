<template>
  <div id="basic">
    <vue-particles
      id="particles-js"
      color="#409eff"
      :particleOpacity="0.7"
      :particlesNumber="100"
      shapeType="circle"
      :particleSize="4"
      linesColor="#000"
      :linesWidth="1"
      :lineLinked="true"
      :lineOpacity="0.4"
      :linesDistance="150"
      :moveSpeed="2"
      :hoverEffect="true"
      hoverMode="grab"
      :clickEffect="true"
      clickMode="push"
      class="lizi"
    >
    </vue-particles>
    <img
      src="https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/img/1.webp"
      style="width:150px;height:150px;border-radius: 50%;margin-top:20px;margin-bottom:20px"
    />
    <h1 style="color:#409eff">CCIT杀手,让大学生没有难应付的高校形式主义</h1>

    <h3>作者：王权霸业 QQ：9479421</h3>

    <a-button type="primary" @click="openBlog()">！！！点我-查看作者主页</a-button>

    <a-popover title="感谢支持" style="margin-top:10px">
      <template slot="content">
        <div style="font-size: 12px;text-align: center;margin-bottom: 5px;">
          一毛是鼓励
        </div>
        <img
          width="350px"
          height="350px"
          src="https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/img/QQ图片20220512140237.png"
          alt=""
          class="ui rounded bordered image"
        />
        <div style="font-size: 12px;text-align: center;margin-top:5px;">
          一块是真爱
        </div>
      </template>
      <a-button type="dashed" block>
        <h3>{赞赏一下}.[无聊瞎写的一个小项目] .{赞赏一下}. [觉得好用就投个币把！].{赞赏一下}</h3>
        <!---->
      </a-button>
    </a-popover>

    <a-layout>
      <a-layout-header class="bgwhite">
        <Header></Header>
      </a-layout-header>
      <a-layout-content>
        <a-row class="bgwhite">
          <a-col :span="8">
            <Update />
          </a-col>
          <a-col :span="16">
            <router-view />
          </a-col>
        </a-row>
      </a-layout-content>
      <a-layout-footer class="bgwhite"><Footer /></a-layout-footer>
    </a-layout>
  </div>
</template>

<script>
import Header from "./Header";
import Footer from "./Footer";
import Update from "./Update";

export default {
  data() {
    return {};
  },
  components: {
    Header,
    Update,
    Footer
  },
  methods: {
    openBlog() {
      window.open("http://blog.wqby.vip", "_blank");
    }
  },
  // metaInfo: {
  //   title: "批量创建",
  //   meta: [
  //     { name: "apple-mobile-web-app-capable", content: "no-referrer" },
  //     { name: "apple-mobile-web-app-status-bar-style", content: "white" }
  //   ]
  // }
};
</script>

<style scoped>
.bgwhite {
  background-color: white;
  opacity: 0.8;
}
#basic {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#particles-js {
  width: 100%;
  height: calc(100% - 100px);
  position: absolute;
}
</style>
