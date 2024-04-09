<template>
  <div>
    <a-steps style="width: 600px;margin:0 auto">
      <a-step status="finish" title="登录账号">
        <a-icon slot="icon" type="user" />
      </a-step>
      <a-step status="finish" title="后端校验">
        <a-icon slot="icon" type="solution" />
      </a-step>
      <a-step status="finish" title="添加队列">
        <a-icon slot="icon" type="smile-o" />
      </a-step>
    </a-steps>
    <a-row style="margin-top:30px">
      <a-col :span="12">
        <a-card hoverable style="width: 300px;margin:0 auto">
          <img
            slot="cover"
            alt="example"
            src="https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png"
          />

          <a-card-meta title="9999人" description="当前已添加队列账号数">
            <a-avatar
              slot="avatar"
              src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
            />
          </a-card-meta>
        </a-card>
      </a-col>
      <a-col :span="12">
        <a-form style="width:300px">
          <a-form-item label="今日校园账号(同常信官网学号)">
            <a-input style="text-align:center" v-model="formInline.username" />
          </a-form-item>
          <a-form-item label="今日校园密码(同常信官网密码)">
            <a-input style="text-align:center" v-model="formInline.password" />
          </a-form-item>
          <a-form-item label="QQ号(接收信息用，确保邮箱可用)">
            <a-input type="number" style="text-align:center" v-model="formInline.qq" />
          </a-form-item>
          <a-form-item label="授权码(联系作者获取)">
            <a-input style="text-align:center" v-model="formInline.cardID" />
          </a-form-item>
          <a-form-item>
            <a-button
              @click="addTask()"
              :disabled="formInline.username === '' || formInline.password === '' || disable===true"
              type="primary"
              >添加任务</a-button
            >
          </a-form-item>
        </a-form>
      </a-col>
    </a-row>
    <a-alert
      message="使用许可"
      description="本功能理论上不存在被查出来的情况，经测试极其稳定。但即使是百分之99.99的安全性，我也不会把话说的太满，所有使用者自行承担任何风险，与作者无关（使用即代表默认该许可）"
      type="info"
    />
    <a-alert
      message="某些一辈子墨守成规、故步自封、一事无成的怂瓜犊子，自觉别用，出门左转"
      type="info"
      close-text="Close Now"
    />
  </div>
</template>

<script>
export default {
  data() {
    return {
      formInline: {
        username: "",
        password: "",
        qq:"",
        cardID: ""
      },
      disable:false
    };
  },
  methods: {
    addTask() {
      this.disable = true;
      this.$axios
        .post(
          "addTask",
          "username=" +
            this.formInline.username +
            "&password=" +
            this.formInline.password +
            "&qq=" +
            this.formInline.qq+
            "&cardID=" +
            this.formInline.cardID
        )
        .then(res => {
          if (res.data.code == 200) {
            this.$message.success(res.data.msg);
            this.formInline = {};
          } else {
            this.$message.error(res.data.msg);
          }
          this.disable = false;
        });
    }
  }
};
</script>

<style scoped>
.color {
  background-image: -moz-linear-gradient(
    45deg,
    rgb(168, 255, 212),
    rgb(189, 233, 255)
  );
  background-image: -webkit-linear-gradient(
    45deg,
    rgb(168, 255, 212),
    rgb(189, 233, 255)
  );
  background-image: linear-gradient(
    45deg,
    rgb(168, 255, 212),
    rgb(189, 233, 255)
  );
}
</style>
