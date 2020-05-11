import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import Home from '../modules/Home.vue';

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: '/',
    name: 'Home',
    component: Home,
  },
  // {
  //   path: '/about',
  //   name: 'About',
  //   component: () => import('../views/About.vue'),
  // },
];

const router = new VueRouter({
  routes,
  base: '/CyFHIR/',
  mode: 'history',
});

export default router;
