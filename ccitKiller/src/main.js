// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import Antd from 'ant-design-vue';
import axios from 'axios';
import VueParticles from 'vue-particles'

import VideoPlayer from 'vue-video-player'
Vue.use(VideoPlayer)
import 'video.js/dist/video-js.css'


Vue.use(VueParticles)
import "ant-design-vue/dist/antd.css";

Vue.config.productionTip = false
Vue.use(Antd);

import Meta from 'vue-meta'
Vue.use(Meta)

axios.defaults.baseURL='http://82.157.146.37:9001' ///82.157.146.37 localhost
Vue.prototype.$axios = axios;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
