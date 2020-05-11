import Vue from 'vue';
import VueRouter, { RouteConfig } from 'vue-router';
import Home from '../modules/Home.vue';

Vue.use(VueRouter);

let routes: Array<RouteConfig> = [
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

routes = routes.map((route) => {
  route.path = `/CyFHIR${route.path}`;
  return route;
});

const router = new VueRouter({
  routes,
  mode: 'history',
});

export default router;
