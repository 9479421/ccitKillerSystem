<template>
  <div
    style="height:480px"
    class="demo-infinite-container"
    :infinite-scroll-distance="10"
  >
    <a-list :data-source="data">
      <a-list-item slot="renderItem" slot-scope="item">
        <a-list-item-meta :description="item.content">
          <a slot="title" :href="item.href">{{ item.name }}</a>
          <a-avatar slot="avatar" :src="item.avatar" />
        </a-list-item-meta>
        <div>{{ item.date }}</div>
      </a-list-item>
    </a-list>
  </div>
</template>
<script>
const data = [
  {
    name: "王权霸业",
    avatar:
      "https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/img/9479421_4256f22136d9e713cda998a1b00c30a5",
    date: "22/5/10",
    content: "增加i博思自动签到功能，同时更换企业邮箱"
  },
  {
    name: "王权霸业",
    avatar:
      "https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/img/9479421_4256f22136d9e713cda998a1b00c30a5",
    date: "22/5/6",
    content: "增加QQ机器人自动提醒，包括群提醒和私聊提醒"
  },
  {
    name: "王权霸业",
    avatar:
      "https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/img/9479421_4256f22136d9e713cda998a1b00c30a5",
    date: "22/5/5",
    content: "新增今日校园QQ字段及邮箱提醒，QQ机器人提醒功能正在开发中……"
  },
  {
    name: "王权霸业",
    avatar:
      "https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/img/9479421_4256f22136d9e713cda998a1b00c30a5",
    date: "22/5/4",
    content: "更新校园网自动连接功能"
  },
  {
    name: "王权霸业",
    avatar:
      "https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/img/9479421_4256f22136d9e713cda998a1b00c30a5",
    date: "22/5/3",
    content: "更新自动青年大学习功能，增加打赏功能"
  },
  {
    name: "王权霸业",
    avatar:
      "https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/img/9479421_4256f22136d9e713cda998a1b00c30a5",
    date: "22/5/3",
    content: "优化今日校园签到判断机制，修复短信内容"
  },
  {
    name: "孙志强(大佬)",
    avatar: "https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/img/lm.jpg",
    date: "22/4/21",
    content: "更新题斗罗功能"
  },
  {
    name: "王权霸业",
    avatar:
      "https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/img/QQ图片20220422002506.jpg",
    date: "22/4/21",
    content: "突破三个加密算法，更新自动疫情签到功能！"
  },
  {
    name: "王权霸业",
    avatar:
      "https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/img/QQ图片20220422002506.jpg",
    date: "22/4/19",
    content: "伪造今日校园协议，卡住了！"
  },
  {
    name: "王权霸业",
    avatar:
      "https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/img/QQ图片20220422002506.jpg",
    date: "22/4/18",
    content: "逆向分析出常信加密算法，实现自动login"
  },
  {
    name: "王权霸业",
    avatar: "https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png",
    date: "22/4/17",
    content: "测试调优，打包部署，准备上线Docker"
  },
  {
    name: "王权霸业",
    avatar: "https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png",
    date: "22/4/17",
    content: "更新卡密授权模式接口与前端"
  },
  {
    name: "王权霸业",
    avatar: "https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png",
    date: "22/4/17",
    content: "完善短信与邮箱任务队列与消费者"
  },
  {
    name: "王权霸业",
    avatar: "https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png",
    date: "22/4/16",
    content: "更新后端获取作业列表Api"
  },
  {
    name: "王权霸业",
    avatar: "https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png",
    date: "22/4/16",
    content: "完善i博思UI界面"
  },
  {
    name: "王权霸业",
    avatar: "https://wqby-1304194722.cos.ap-nanjing.myqcloud.com/img/wqby.jpg",
    date: "22/4/15",
    content: "构建项目脚手架,设计界面UI"
  }
];

import infiniteScroll from "vue-infinite-scroll";

export default {
  directives: { infiniteScroll },
  data() {
    return {
      data
    };
  },
  methods: {}
};
</script>
<style scoped>
.demo-infinite-container {
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  overflow: auto;
  padding: 8px 24px;
  height: 300px;
}
.demo-loading-container {
  position: absolute;
  bottom: 40px;
  width: 100%;
  text-align: center;
}
</style>
