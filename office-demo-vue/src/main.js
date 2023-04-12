import Vue from 'vue'
import App from './App.vue'
import './plugins/element.js'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'

// 导入全局样式
import './assets/css/global.css'
// 导入字体图标
import './assets/fonts/iconfont.css'


Vue.config.productionTip = false
Vue.prototype.$http = axios
new Vue({
  render: h => h(App),
}).$mount('#app')
