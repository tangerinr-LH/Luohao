import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'home',
        component: () => import(/* webpackChunkName: "about" */ '../views/HomeView.vue'),
        redirect: "home",
        children: [
            {
                path: 'home',
                component: () => import(/* webpackChunkName: "about" */ '../components/Home.vue'),
            },


        ]

    },


    {
        path: '/',
        name: '系统管理',
        component: () => import(/* webpackChunkName: "about" */ '../views/HomeView.vue'),
        meta: {
            title: '系统管理'
        },
        children: [

            {
                path: 'user',
                name: '用户管理',
                meta: {
                    title: '用户管理'
                },
                component: () => import(/* webpackChunkName: "about" */ '../components/User.vue'),
            },


        ]

    },



    {
        path: '/about',
        name: 'about',
        component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router
