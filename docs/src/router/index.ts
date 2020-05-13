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
  {
    path: '/concepts',
    name: 'Concepts',
    component: () => import('../modules/Concepts.vue'),
  },
  {
    path: '/setup',
    name: 'Setup',
    component: () => import('../modules/Setup.vue'),
  },
  {
    path: '/guide',
    name: 'Guide',
    component: () => import('../modules/Guide.vue'),
  },
];

const router = new VueRouter({
  routes,
  base: '/CyFHIR/',
  mode: 'history',
});

export default router;
