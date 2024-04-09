<template>
  <div>
    <a-row>
      <a-col :span="8">
        <div>
          <div @click="changeKey('1')" class="avatar_box" style="margin-top: 20px;">
            <img
              src="https://ss-1311201415.cos.ap-nanjing.myqcloud.com/pic/u=2824483722,218364351&fm=253&fmt=auto&app=138&f=JPEG"
              style="width: 100px; height: 100px; border-radius: 50%"
            />
            <img
              src="https://ss-1311201415.cos.ap-nanjing.myqcloud.com/pic/9PEH4@[LAPIGZGM7LOCRATG.png"
            />
          </div>
          <div @click="changeKey('2')" class="another_box" style="margin-top: 20px;">
            <img
              src="https://ss-1311201415.cos.ap-nanjing.myqcloud.com/pic/5b29936216a36_610.jpg"
              style="width: 100px; height: 100px; border-radius: 20%"
            />
            <img
              src="https://ss-1311201415.cos.ap-nanjing.myqcloud.com/pic/20220421195033.png"
            />
          </div>
        </div>
      </a-col>
      <a-col :span="16">
        <a-tabs v-model="activeKey" default-active-key="2">
          <a-tab-pane key="1">
            <!-- <span slot="tab">
              <a-icon type="apple" />
              拍照搜题
            </span> -->
            开发中………………
          </a-tab-pane>
          <a-tab-pane key="2">
            <!-- <span slot="tab">
              <a-icon type="android" />
              文字搜题
            </span> -->
            <template>
              <img
                src="https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/img/QQ截图20220422010213.png"
                alt=""
              />
              <a-input-search
                placeholder="请输入你要搜索的题目后回车(例如：中国历史上第一位皇帝是谁)"
                style="width: 90%"
                @search="searchproblem()"
                autosize="true"
                size="large"
                v-model="question"
              />
              <a-row>
                <a-col :span="12">
                  <a-spin style="margin-top:40px" size="small" />
                  <a-spin style="margin-top:20px" />
                  <a-spin size="large" />

                  <h3>题目：{{ answerJSON.rawQuestion }}</h3>
                  <h3>
                    答案：<span style="color:red">{{ answerJSON.answer }}</span>
                  </h3>
                </a-col>
                <a-col :span="12">
                  <vue-json-pretty :path="'res'" :data="answerJSON">
                  </vue-json-pretty>
                  <!-- <json-viewer

                    :value="textarea2"
                    :expand-depth="4"
                    copyable
                    sort
                  ></json-viewer> -->
                </a-col>
              </a-row>
            </template>
          </a-tab-pane>
        </a-tabs>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import VueJsonPretty from "vue-json-pretty";
import "vue-json-pretty/lib/styles.css";
export default {
  components: {
    VueJsonPretty
  },
  data() {
    return {
      question: "",
      answerJSON: {},
      activeKey: '2'
    };
  },
  methods: {
    async searchproblem() {
      const { data: res } = await this.$axios.get(
        "http://101.35.142.176:9876/singlesearch?question=" + this.question
      );
      if(res.question==null){
        this.$message.error("未搜到该题目！")
      }
      this.answerJSON = res;
      
    },
    changeKey(key){
      if(key=='1'){
        this.$message.info('该功能正在开发中……请先使用文字搜题')
        return;
      }
      this.activeKey=key
    }
  }
};
</script>

<style scoped>
.avatar_box:hover {
  background-color: #4cc9f0;
  -webkit-box-shadow: 10px 10px 99px 6px rgba(76, 201, 240, 1);
  -moz-box-shadow: 10px 10px 99px 6px rgba(76, 201, 240, 1);
  box-shadow: 10px 10px 99px 6px rgba(76, 201, 240, 1);
}
.avatar_box {
  margin: 0 auto;
  width: 200px;
  height: 200px;
  background-color: white;
  border-radius: 40px;
  box-shadow: 0 0 2px black;
}
.another_box:hover {
  background-color: #b9e769;
  -webkit-box-shadow: 10px 10px 99px 6px rgba(185, 231, 105, 1);
  -moz-box-shadow: 10px 10px 99px 6px rgba(185, 231, 105, 1);
  box-shadow: 10px 10px 99px 6px rgba(185, 231, 105, 1);

}
.another_box {
  margin: 0 auto;
  width: 200px;
  height: 200px;
  box-shadow: 0 0 2px black;
  border-radius: 40px;
  background-color: white;
}
</style>
