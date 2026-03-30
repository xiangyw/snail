import { createApp } from 'vue'
import { createPinia } from 'pinia'

// 引入 Vant 样式
import 'vant/lib/index.css'

// 全局样式
import './assets/styles/main.css'

import App from './App.vue'
import router from './router'

const app = createApp(App)
const pinia = createPinia()

// 引入 Vant 组件
import {
  ConfigProvider,
  Tabbar,
  TabbarItem,
  NavBar,
  Search,
  NoticeBar,
  Grid,
  GridItem,
  Skeleton,
  List,
  Card,
  Button,
  Field,
  Cell,
  CellGroup,
  Dialog,
  Toast,
  Loading,
  Empty,
  Image as VanImage,
  Icon
} from 'vant'

// 注册 Vant 组件
app.use(ConfigProvider)
app.use(Tabbar)
app.use(TabbarItem)
app.use(NavBar)
app.use(Search)
app.use(NoticeBar)
app.use(Grid)
app.use(GridItem)
app.use(Skeleton)
app.use(List)
app.use(Card)
app.use(Button)
app.use(Field)
app.use(Cell)
app.use(CellGroup)
app.use(Dialog)
app.use(Toast)
app.use(Loading)
app.use(Empty)
app.use(VanImage)
app.use(Icon)

app.use(pinia)
app.use(router)

app.mount('#app')
