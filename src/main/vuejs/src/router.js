import { createRouter, createWebHistory } from 'vue-router';

// Import your components
import HomePage from "@/pages/HomePage.vue";
import ProfilePage from "@/pages/ProfilePage.vue";

const routes =  [
    { path: '/', name: "home",component: HomePage },
    { path: '/profile/:name', name:"profile", component: ProfilePage}
];

const router = new createRouter({
    history:createWebHistory(),
    routes
});

export default router;