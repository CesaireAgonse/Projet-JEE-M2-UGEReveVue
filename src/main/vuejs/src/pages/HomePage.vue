<template>
  <LoginForm v-if="isLoginModalVisible" @close-modal="hideLoginModal" :isLoginModalVisible="isLoginModalVisible" @connect="connect"/>
  <SignupForm v-if="isSignupModalVisible" @close-modal="hideSignupModal" :isSignupModalVisible="isSignupModalVisible" />
  <HomeVisual @show-login-modal="showLoginModal" :isLoginModalVisible="isLoginModalVisible"
            @show-signup-modal="showSignupModal" :isSignupModalVisible="isSignupModalVisible"
            @disconnect="disconnect"
            :isLogged="isLogged"/>
</template>

<script>

import LoginForm from "@/components/LoginForm.vue";
import SignupForm from "@/components/SignupForm.vue";
import HomeVisual from "@/components/HomeVisual.vue";
import {authenticationService} from '@/services/authentication.service'
export default {
  mounted() {
    document.title = "Home"
  },
  components: {
    HomeVisual,
    SignupForm,
    LoginForm,
  },
  data() {
    return {
      isLogged: authenticationService.isLogged(),
      isLoginModalVisible: false,
      isSignupModalVisible: false,
    };
  },
  methods: {
    connect(){
      this.isLogged = true;
    },
    disconnect(){
      this.isLogged = false;
    },
    showLoginModal() {
      this.isLoginModalVisible = true;
    },
    hideLoginModal() {
      this.isLoginModalVisible = false;
    },
    showSignupModal() {
      this.isSignupModalVisible = true;
    },
    hideSignupModal() {
      this.isSignupModalVisible = false;
    }
  }
};
</script>
