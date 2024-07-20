import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    redirect: '/home',  // 重定向到主页
    children: [
      { path: '403', name: 'NoAuth', meta: { name: '无权限' }, component: () => import('../views/manager/403') },
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/manager/Home') },
      { path: 'admin', name: 'Admin', meta: { name: '管理员信息' }, component: () => import('../views/manager/Admin') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'password', name: 'Password', meta: { name: '修改密码' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: '公告信息' }, component: () => import('../views/manager/Notice') },
      { path: 'teacher', name: 'Teacher', meta: { name: '教员信息' }, component: () => import('../views/manager/Teacher') },
      { path: 'user', name: 'User', meta: { name: '家长信息' }, component: () => import('../views/manager/User') },
      { path: 'type', name: 'Type', meta: { name: '家教分类' }, component: () => import('../views/manager/Type') },
      { path: 'info', name: 'Info', meta: { name: '教员审核' }, component: () => import('../views/manager/Info') },
      { path: 'reserve', name: 'Reserve', meta: { name: '家教预约' }, component: () => import('../views/manager/Reserve') },
      { path: 'information', name: 'Information', meta: { name: '家教资讯' }, component: () => import('../views/manager/Information') },
      { path: 'publish', name: 'Publish', meta: { name: '发布家教' }, component: () => import('../views/manager/Publish') },
      { path: 'message', name: 'Message', meta: { name: '留言反馈' }, component: () => import('../views/manager/Message') },
    ]
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/Front.vue'),
    children: [
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/front/Home') },
      { path: 'person', name: 'Person', meta: { name: '个人信息' }, component: () => import('../views/front/TeacherPerson.vue') },
      { path: 'teacherPerson', name: 'TeacherPerson', meta: { name: '教员中心' }, component: () => import('../views/front/TeacherPerson') },
      { path: 'userPerson', name: 'UserPerson', meta: { name: '家长中心' }, component: () => import('../views/front/UserPerson') },
      { path: 'teacherDetail', name: 'TeacherDetail', meta: { name: '教员详情' }, component: () => import('../views/front/TeacherDetail') },
      { path: 'reserve', name: 'Reserve', meta: { name: '我的预约' }, component: () => import('../views/front/Reserve') },
      { path: 'information', name: 'Information', meta: { name: '家教资讯' }, component: () => import('../views/front/Information') },
      { path: 'informationDetail', name: 'InformationDetail', meta: { name: '资讯详情' }, component: () => import('../views/front/InformationDetail') },
      { path: 'publish', name: 'Publish', meta: { name: '我的发布' }, component: () => import('../views/front/Publish') },
      { path: 'publishSquare', name: 'PublishSquare', meta: { name: '家教广场' }, component: () => import('../views/front/PublishSquare') },
      { path: 'message', name: 'Message', meta: { name: '留言反馈' }, component: () => import('../views/front/Message') },
      { path: 'myMessage', name: 'MyMessage', meta: { name: '我的留言' }, component: () => import('../views/front/MyMessage') },
      { path: 'teacherType', name: 'TeacherType', meta: { name: '分类教员' }, component: () => import('../views/front/TeacherType') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: '登录' }, component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', meta: { name: '注册' }, component: () => import('../views/Register.vue') },
  { path: '*', name: 'NotFound', meta: { name: '无法访问' }, component: () => import('../views/404.vue') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
// router.beforeEach((to ,from, next) => {
//   let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
//   if (to.path === '/') {
//     if (user.role) {
//       if (user.role === 'USER') {
//         next('/front/home')
//       } else {
//         next('/home')
//       }
//     } else {
//       next('/login')
//     }
//   } else {
//     next()
//   }
// })

export default router
