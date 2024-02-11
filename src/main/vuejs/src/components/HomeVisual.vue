<template>
  <div>
    <transition class="slide-up">
      <div class="image-container">
        <img v-if="showImage" src="../assets/home.jpg" @click="scrollImageUp" class="scrollable-image" :style="{ transform: imageTransform }" />
        <div v-if="!(isLoginModalVisible || isSignupModalVisible)" class="welcome-message" :style="{ top: welcomeMessageTop }">
          <h1 class="create-message">Bienvenue sur UGEReveVue</h1>
        </div>
        <div v-if="!(isLoginModalVisible || isSignupModalVisible)" class="search-bar" :style="{ top: searchBarTop }">
          <input class="bar" type="text" placeholder="Rechercher..." />
        </div>
        <div class="auth-buttons">
          <div v-if="authUser === ''">
            <button class="login-button" @click="showLoginModal">Se connecter</button>
            <button class="signup-button" @click="showSignupModal">S'inscrire</button>
          </div>
          <div v-if="authUser !== ''">
            <button class="basic-button" @click="logout">Se déconnecter</button>
          </div>
        </div>
        <div class="center-arrow" @click="scrollImageUp">
          <div class="arrow" :style= "{transform : arrowDegree}"></div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import axios from "axios";

export default {
  mounted() {
    this.auth()
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
      showImage: true,
      imageTransform: '',
      searchBarTop: '40%',
      welcomeMessageTop:'20%',
      arrowDegree:'rotate(135deg)',
      authUser: ""
    };
  },
  methods: {
    scrollImageUp() {
      if (this.imageTransform === 'translateY(-90%)') {
        this.imageTransform = '';
        this.searchBarTop = '40%';
        this.welcomeMessageTop = '20%';
        this.arrowDegree = 'rotate(135deg)';
      } else {
        this.imageTransform = 'translateY(-90%)';
        this.searchBarTop = '15%';
        this.welcomeMessageTop = '-90%';
        this.arrowDegree='rotate(-45deg) translate(1500%, -1500%)';
      }
    },
    showLoginModal() {
      this.$emit('show-login-modal');
    },
    showSignupModal() {
      this.$emit('show-signup-modal');
    },
    async auth(){
      const content = await axios.get("/api/v1/auth");
      this.authUser = content.data;
    },
    async logout(){
      await axios.post("/api/v1/logout");
      this.authUser = "";
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

center-arrow{
  transition: top 1s ease;
}

div.arrow {
  width: 4vmin;
  height: 4vmin;
  box-sizing: border-box;
  position: absolute;
  left: 49%;
  top: 75%;

  &::before {
    content: '';
    width: 100%;
    height: 100%;
    border-width: .8vmin .8vmin 0 0;
    border-style: solid;
    border-color: #fafafa;
    transition: .2s ease;
    display: block;
    transform-origin: 100% 0;
  }

  &:hover::after {
    border-color: orange;
    transform: scale(.8);
  }
  &:hover::before {
    border-color: orange;
    transform: scale(.8);
  }
}

</style>
