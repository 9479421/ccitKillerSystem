<template>
  <div >
    <a-menu :selectedKeys="selectedKeys" mode="horizontal">
      <template v-for="item in menuData">
        <a-menu-item
          v-if="!item.children"
          :key="item.path"
          @click="routeJump(item.path)"
        >
          <a-icon :type="item.icon" /><span style="font-size:16px">{{
            item.name
          }}</span>
        </a-menu-item>

        <a-sub-menu v-else :key="item.path">
          <span slot="title" class="submenu-title-wrapper"
            ><a-icon type="setting" /><span style="font-size:16px">{{
              item.name
            }}</span></span
          >
          <a-menu-item  @click="swk()" v-for="it in item.children" :key="it.path">
            <a-icon :type="item.icon" />
            <span style="font-size:16px">{{ it.name }}</span>
          </a-menu-item>
        </a-sub-menu>
      </template>
    </a-menu>
  </div>
</template>

<script>
import NProgress from "nprogress";
import "nprogress/nprogress.css";
export default {
  data() {
    this.selectedKeysMap = {};
    const menuData = this.getMenuData(this.$router.options.routes);
    return {
      menuData,
      selectedKeys: this.selectedKeysMap[this.$route.path]
    };
  },
  watch: {
    "$route.path": function(val) {
      this.selectedKeys = this.selectedKeysMap[val];
    }
  },
  methods: {
    getMenuData(routes = []) {
      const menuData = [];
      routes.forEach(item => {
        if (!item.hideInMenu && item.name && !item.isSub) {
          this.selectedKeysMap[item.path] = [item.path];
          menuData.push(item);
        }
        if (item.children) {
          menuData.push(...this.getMenuData(item.children));
        }
      });
      return menuData;
    },
    swk(){
      this.$message.warning('该功能暂未开放,详情联系作者')
    },
    routeJump(path){
      NProgress.start();
      this.$router.push({ path: path })
      NProgress.done();
    }
  }
};
</script>

<style>
  .ant-menu-submenu-title{
    font-size: 30px;
    color: black
}
</style>
