<template>
  <CodeForm v-if="isCodeModalVisible" @close-modal="hideCodeModal" :isCodeModalVisible="isCodeModalVisible"></CodeForm>
  <LoginForm v-if="isLoginModalVisible" @close-modal="hideLoginModal" :isLoginModalVisible="isLoginModalVisible" @connect="connect"/>
  <SignupForm v-if="isSignupModalVisible" @close-modal="hideSignupModal" :isSignupModalVisible="isSignupModalVisible" />
  <HomeVisual @show-login-modal="showLoginModal" :isLoginModalVisible="isLoginModalVisible"
            @show-signup-modal="showSignupModal" :isSignupModalVisible="isSignupModalVisible"
            @show-code-modal="showCodeModal" :isCodeModalVisible="isCodeModalVisible"
            @disconnect="disconnect"
            :isLogged="isLogged"/>
</template>

<script>

import LoginForm from "@/components/LoginForm.vue";
import SignupForm from "@/components/SignupForm.vue";
import HomeVisual from "@/visuals/HomeVisual.vue";
import CodeForm from "@/components/CodeForm.vue";
import {authenticationService} from '@/services/authentication.service'
export default {
  mounted() {
    document.title = "Home"
  },
  components: {
    CodeForm,
    HomeVisual,
    SignupForm,
    LoginForm,
  },
  data() {
    return {
      isLogged: authenticationService.isLogged(),
      isLoginModalVisible: false,
      isSignupModalVisible: false,
      isCodeModalVisible: false,
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
    },
    showCodeModal() {
      this.isCodeModalVisible = true;
    },
    hideCodeModal() {
      this.isCodeModalVisible = false;
    }
  }
};
</script>
