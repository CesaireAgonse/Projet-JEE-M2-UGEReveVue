<template>
  <CodeForm v-if="isCodeModalVisible" @close-modal="hideCodeModal" @refresh-filter="filter" :isCodeModalVisible="isCodeModalVisible"></CodeForm>
  <LoginForm v-if="isLoginModalVisible" @close-modal="hideLoginModal" :isLoginModalVisible="isLoginModalVisible" @connect="connect"/>
  <SignupForm v-if="isSignupModalVisible" @close-modal="hideSignupModal" :isSignupModalVisible="isSignupModalVisible" />
  <HomeVisual @show-login-modal="showLoginModal" :isLoginModalVisible="isLoginModalVisible"
            @show-signup-modal="showSignupModal" :isSignupModalVisible="isSignupModalVisible"
            @show-code-modal="showCodeModal" :isCodeModalVisible="isCodeModalVisible"
            @disconnect="disconnect"
            :isLogged="isLogged" :posts="posts"/>
</template>

<script>

import LoginForm from "@/components/LoginForm.vue";
import SignupForm from "@/components/SignupForm.vue";
import HomeVisual from "@/visuals/HomeVisual.vue";
import CodeForm from "@/components/CodeForm.vue";
import {authenticationService} from '@/services/authentication.service'
import {codeService} from "@/services/code.service";
export default {
  mounted() {
    document.title = "Home"
    this.filter()
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
      posts:null,
    };
  },
  methods: {
    filter(){
      codeService.filter().then(res => {
        this.posts = res.data.codes
      })
      console.log("Refresh")
    },
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
    },
  }
};
</script>
