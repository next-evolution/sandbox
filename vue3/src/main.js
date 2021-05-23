import { createApp } from 'vue'
import App from './App.vue'
import { router } from './libs/router'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import { store } from "./libs/store";

import axios from 'axios'
import VueAxios from 'vue-axios'

//import Loading from 'vue-loading-overlay'
import 'vue-loading-overlay/dist/vue-loading.css'

import mixinJs from './libs/mixin'

createApp(App)
  .use(router)
  .use(store)
  .use(VueAxios, axios)
  .mixin(mixinJs)
  .mount('#app')
