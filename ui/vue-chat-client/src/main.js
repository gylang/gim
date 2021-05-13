import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Vant from 'vant';
import 'vant/lib/index.css';
import '@/style/color.css';
import '@/style/layout.css';
import '@/style/style.css';
import IndexDB from "@/util/IndexedDBStrategy"
import socket from "@/util/socket";

setInterval(() => {
    socket.check()

  }, 2000)
IndexDB.openDB()
Vue.config.productionTip = false
Vue.use(Vant)
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
