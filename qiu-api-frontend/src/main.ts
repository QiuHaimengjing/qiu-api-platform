import { createApp } from 'vue'
import pinia from './stores'
import App from './App.vue'
import router from './router'
import '@arco-design/web-vue/dist/arco.css'
import './assets/icon/iconfont.css'

const app = createApp(App)
app.use(pinia)
app.use(router)

app.mount('#app')
