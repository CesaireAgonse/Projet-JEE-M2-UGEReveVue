import { createRouter, createWebHistory } from 'vue-router';

// Import your components
import HomePage from "@/pages/HomePage.vue";
import ProfilePage from "@/pages/ProfilePage.vue";
import CodePage from "@/pages/CodePage.vue";
import ReviewPage from "@/pages/ReviewPage.vue";
import UserPage from "@/pages/UserPage.vue";

const routes =  [
    { path: '/', name: "home",component: HomePage },
    { path: '/profile/:name', name:"user", component: UserPage},
    { path: '/profile', name:"profile", component: ProfilePage},
    { path: '/codes/:id', name:"code", component: CodePage},
    { path: '/reviews/:id', name:"review", component: ReviewPage}
];

const router = new createRouter({
    history:createWebHistory(),
    routes
});

export default router;