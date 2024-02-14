<template>
  <div>
    <transition class="slide-up">
      <div class="image-container">
        <img src="../assets/home.jpg" @click="scrollImageUp" class="scrollable-image" :style="{ transform: imageTransform }" />
        <div v-if="!(isLoginModalVisible || isSignupModalVisible)" class="welcome-message" :style="{ top: welcomeMessageTop }">
          <h1 class="create-message">Bienvenue sur UGEReveVue</h1>
        </div>
        <div v-if="!(isLoginModalVisible || isSignupModalVisible)" class="search-bar" :style="{ top: searchBarTop }">
          <input class="bar" type="text" placeholder="Rechercher..." />
        </div>
        <div v-if="imageUp" class = "chevron-down" @click="scrollImageUp">
          <i class="fa-solid fa-chevron-down fa-beat-fade fa-2xl"></i>
        </div>
        <div v-if="!imageUp" class = "chevron-up" @click="scrollImageUp">
          <i class="fa-solid fa-chevron-up fa-beat-fade fa-2xl"></i>
        </div>
        <div class="auth-buttons">
          <div v-if="!authenticationService.isLogged()">
            <button class="login-button" @click="showLoginModal">Se connecter</button>
            <button class="signup-button" @click="showSignupModal">S'inscrire</button>
          </div>
          <div v-if="authenticationService.isLogged()">
            <button class="basic-button" @click="logout">Se déconnecter</button>
            <i class="fa-regular fa-circle-user fa-2xl " @click="profile" style="margin-right: 10px"></i>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import {authenticationService} from "@/services/authentication.service";
import router from "@/router";
import { library, dom } from "@fortawesome/fontawesome-svg-core";
import { fas } from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';
library.add(fas, far, fab)
dom.watch();

export default {
  components: {
  },
  computed: {
    authenticationService() {
      return authenticationService
    }
  },
  props: {
    isLoginModalVisible: {
      type: Boolean,
      required: true
    },
    isSignupModalVisible: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      imageTransform: '',
      searchBarTop: '40%',
      welcomeMessageTop:'20%',
      authUser: "",
      imageUp: true,
      post: {
        title: 'Titre du post',
        description: 'Description du post',
        author: 'Auteur du post',
        date: 'Date du post',
        code: 'function example() {\n  console.log("Hello, world!");\n}',
        liked: true,
        score: 0
      }
    };
  },
  methods: {
    scrollImageUp() {
      if (this.imageTransform === 'translateY(-92%)') {
        this.imageTransform = '';
        this.searchBarTop = '40%';
        this.welcomeMessageTop = '20%';
        this.imageUp = true;
      } else {
        this.imageTransform = 'translateY(-92%)';
        this.searchBarTop = '15%';
        this.welcomeMessageTop = '-90%';
        this.imageUp = false;
      }
    },
    showLoginModal() {
      this.$emit('show-login-modal');
    },
    showSignupModal() {
      this.$emit('show-signup-modal');
    },
    logout(){
      authenticationService.logout().then(res => {
        console.log(res)
        authenticationService.removeToken()
      }).catch(err => console.log(err))
    },
    profile(){
      router.push('/profile/')
    }
  }
};

</script>

<style>
input::placeholder {
  color: #ccc;
}


.image-container {
  position: fixed;
}

.scrollable-image {
  transition: all 1s;
  width: 100%;
  margin: 0;
  padding: 0;
  border: 0;
}

.welcome-message{
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  color: white;
  transition: top 1s ease;
  font-size: 140%;
}

.bar{
  background: transparent;
  color: white;
}

.search-bar {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  padding: 5px;
  border-radius: 5px;
  transition: top 1s ease;
  border: 2px solid #fff;
}

.search-bar i {
  position: absolute;
  bottom: -20px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 20px;
}

.search-bar input {
  width: 400px;
  padding: 8px;
  border: none;
  border-radius: 5px;
  outline: none;
}

.auth-buttons {
  position: absolute;
  top: 10px;
  right: 10px;
}

.login-button,
.signup-button,
.basic-button {
  border: 1px solid #fff;
  background-color: transparent;
  color: #fff;
  padding: 8px 12px;
  border-radius: 20px;
  margin-left: 20px;
  cursor: pointer;
  margin-top: 10px;
}

.signup-button,
.basic-button{
  margin-right: 10px;
}

.login-button:hover,
.signup-button:hover,
.basic-button:hover {
  color: black;
  background-color: #fff;
}

.slide-up{
  background: #333333;
}

body {
  background-color: #282828;
}

.chevron-down{
  position: absolute;
  bottom: 200px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 20px;
}

.chevron-up{
  position: absolute;
  top: 25px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 20px;
}

</style>
