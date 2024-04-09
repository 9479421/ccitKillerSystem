<template>
  <div id="app">  
    <router-view />
  </div>
</template>

<script>
export default {
  name: "App",
  methods: {
    // 判断手机端还是PC端
    _isMobile() {
      let flag = navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
      );
      return flag;
    },
    info() {
      this.$info({
        title:
          "尊敬的用户你好！为了您的使用体验，我们强烈建议您使用电脑访问本站",
        onOk() {}
      });
    }
  },
  created() {
    if (this._isMobile()) {
      console.log("手机端");
      this.info();
    } else {
      console.log("pc端");
    }
  }
};
</script>

<style >

</style>
