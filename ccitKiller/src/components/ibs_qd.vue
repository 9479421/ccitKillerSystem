<template>
  <div>
    <a-form-model layout="inline" :model="formInline">
      <a-form-model-item>
        <a-input v-model="formInline.username" placeholder="Username">
          <a-icon slot="prefix" type="user" style="color:rgba(0,0,0,.25)" />
        </a-input>
      </a-form-model-item>
      <a-form-model-item>
        <a-input
          v-model="formInline.password"
          type="password"
          placeholder="Password"
        >
          <a-icon slot="prefix" type="lock" style="color:rgba(0,0,0,.25)" />
        </a-input>
      </a-form-model-item>
      <a-form-model-item>
        <a-button
          @click="queryHomeWork()"
          type="primary"
          html-type="submit"
          :disabled="formInline.user === '' || formInline.password === ''"
        >
          查询课程
        </a-button>
      </a-form-model-item>
    </a-form-model>

    <a-table
      :pagination="{ pageSize: 10 }"
      :row-selection="{
        selectedRowKeys: selectedRowKeys,
        onChange: onSelectChange,
        type: 'radio'
      }"
      :columns="columns"
      :data-source="data"
    >
      <!-- 标题 -->
      <span slot="customTitle"><a-icon type="smile-o" />课程名</span>

      <a slot="courseName" slot-scope="text">{{ text }}</a>

      <span slot="countdownStr" slot-scope="text" style="color:red">{{
        text
      }}</span>
    </a-table>

    <a-row type="flex">
      <a-col flex="auto">
        <a-button style="width:100%" type="primary" @click="openTask()"
          >提交任务</a-button
        >
      </a-col>
    </a-row>

    <a-modal
      style="text-align: center;"
      title="请输入授权码"
      :visible="visible"
      :confirm-loading="confirmLoading"
      footer=""
      @cancel="handleCancel"
    >
      <a-input style="text-align:center" v-model="cardID"></a-input>
      <a-button style="margin-top:10px;margin-bottom:30px" type="primary"
        @click="submitTask()">确认卡密</a-button
      >
      <h3>授权码请联系王权霸业qq9479421索取</h3>
    </a-modal>
  </div>
</template>

<script>
const columns = [
  {
    dataIndex: "courseName",
    key: "courseName",
    slots: { title: "customTitle" },
    scopedSlots: { customRender: "courseName" }
  },
  {
    title: "主讲人",
    dataIndex: "teacherName",
    key: "teacherName"
  },
  {
    title: "课程结束时间",
    dataIndex: "endTime",
    key: "endTime"
  },
];

export default {
  data() {
    return {
      formInline: {
        username: "",
        password: ""
      },
      data: [],
      columns,
      selectedRowKeys: [],
      cardID: "",
      visible: false,
      confirmLoading: false,
      submitData:{
        username:'',
        password:'',
        courseId:'',
        userId:'',
        token:'',
        cardID:''
      }
    };
  },
  methods: {
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
      console.log(this.data[selectedRowKeys[0]]);
    },
    queryHomeWork() {
      this.$axios
        .get(
          "getCourseList?username=" +
            this.formInline.username +
            "&password=" +
            this.formInline.password
        )
        .then(res => {
          if (res.data.code == 200) {
            this.$message.success(res.data.msg);
            this.data = res.data.data;
          } else {
            this.$message.error(res.data.msg);
            this.data = [];
          }
        });
    },
    openTask() {
      if (this.data[this.selectedRowKeys[0]] == null) {
        this.$message.error("请选择你要代签到的课程");
      } else {
        this.cardID = "";
        this.visible = true;
      }
    },
    submitTask() {
      this.submitData.username = this.data[this.selectedRowKeys[0]].username;
      this.submitData.password = this.data[this.selectedRowKeys[0]].password;
      this.submitData.courseId = this.data[this.selectedRowKeys[0]].courseId;
      this.submitData.userId = this.data[this.selectedRowKeys[0]].userId;
      this.submitData.token = this.data[this.selectedRowKeys[0]].token;
      this.submitData.cardID = this.cardID;

      this.$axios
        .post(
          "submitTask_rollCall",
          this.submitData
        )
        .then(res => {
          if (res.data.code == 200) {
            this.$message.success(res.data.msg)
            this.visible=false
          }else{
            this.$message.error(res.data.msg)
          }
        });
    },
    handleCancel() {
      this.visible = false;
    }
  }
};
</script>

<style scoped>


</style>
