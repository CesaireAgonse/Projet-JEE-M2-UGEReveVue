import { createRouter, createWebHistory } from 'vue-router';

// Import your components
import HomePage from './components/HomePage.vue';


const routes = [
    { path: '/', component: HomePage },
];

const router = new createRouter({
    history:createWebHistory(),
    routes
});

export default router;