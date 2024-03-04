<template>
  <div>
    <transition class="slide-up">
      <div class="image-container">
        <img src="../assets/home.jpg" @click="scrollImageUp" class="scrollable-image" :style="{ transform: imageTransform }"/>
        <div v-if="!(isLoginModalVisible || isSignupModalVisible)" class="welcome-message" :style="{ top: welcomeMessageTop }">
          <h1 class="create-message">Bienvenue sur UGEReveVue</h1>
        </div>
        <div v-if="!(isLoginModalVisible || isSignupModalVisible)" class="search-bar" :style="{ top: searchBarTop }">
          <input class="bar" v-model="query" type="text" placeholder="Rechercher..." @keyup.enter="handleSearch"/>
        </div>
        <div v-if="imageUp" class="chevron-down" @click="scrollImageUp">
          <i class="fa-solid fa-chevron-down fa-beat-fade fa-2xl"></i>
        </div>
        <div v-if="!imageUp" class="chevron-up" @click="scrollImageUp">
          <i class="fa-solid fa-chevron-up fa-beat-fade fa-2xl"></i>
        </div>
        <div class="auth-buttons">
          <div v-if="!isLogged">
            <button class="login-button" @click="showLoginModal">Se connecter</button>
            <button class="signup-button" @click="showSignupModal">S'inscrire</button>
          </div>
          <div v-if="isLogged">
            <div class="auth-buttons">
              <div class="user-avatar" @click="toggleDropdown">
                <img class="post-author-avatar" src="../assets/profile.jpg" alt="User Avatar" />
              </div>
              <div class="show" v-show="showDropdown">
                <select size="2">
                  <option class="dropdown-option" value="profile" @click="profile()">Voir mon profil</option>
                  <option class="dropdown-option" value="logout" @click="logout()">Se déconnecter </option>
                </select>
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>
    <div class="code-visual" :style="{ transform: listCode }">
      <div class="header-button">
        <button class="basic-button" @click="showCodeModal">Poster un code</button>
        <button class="basic-button" @click="newest">Les plus récents</button>
        <button class="basic-button" @click="relevance">Les mieux notés</button>
      </div>
      <div v-for="post in posts" :key="post"><CodeVisual  :post="post"/></div>
      <div class="footer-button">
        <button v-if="pageNumber > 0" class="basic-button" @click="prev">Page précédente</button>
        <button class="basic-button" @click="next">Page suivante</button>
      </div>
    </div>
  </div>
</template>


<script  scoped>
import CodeVisual from "@/visuals/CodeVisual.vue";
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
    CodeVisual
  },
  props: {
    isLoginModalVisible: {
      type: Boolean,
      required: true
    },
    isSignupModalVisible: {
      type: Boolean,
      required: true
    },
    isCodeModalVisible: {
      type: Boolean,
      required: true
    },
    isLogged:{
      type:Boolean
    },
    posts:{
      type:Object
    }
  },
  data() {
    return {
      imageTransform: '',
      searchBarTop: '40%',
      welcomeMessageTop:'20%',
      authUser: "",
      listCode:'',
      createButton:'',
      footerButton:'',
      imageUp: true,
      showDropdown: false,
      query: '',
      sortBy: '',
      pageNumber: 0
    };
  },
  methods: {
    scrollDown(){
      this.imageTransform = 'translateY(-92%)';
      this.searchBarTop = '15%';
      this.welcomeMessageTop = '-90%';
      this.listCode = 'translate(-50%, -300px)'
      this.createButton = 'translate(38%, 75%)'
      this.imageUp = false;
    },
    scrollImageUp() {
      if (this.imageTransform === 'translateY(-92%)') {
        this.imageTransform = '';
        this.searchBarTop = '40%';
        this.welcomeMessageTop = '20%';
        this.listCode = 'translate(-50%, 650px)'
        this.createButton = 'translate(-175%, 3000%)'
        this.imageUp = true;
      } else {
        this.scrollDown()
      }
    },
    showLoginModal() {
      this.$emit('show-login-modal');
    },
    showSignupModal() {
      this.$emit('show-signup-modal');
    },
    showCodeModal() {
      this.$emit('show-code-modal');
    },
    logout(){
      authenticationService.logout().then(() => {
        authenticationService.removeToken('bearer')
        authenticationService.removeToken('refresh')
        this.$emit('disconnect');
      }).catch(err => console.log(err))
    },
    profile(){
      router.push('/profile/' + authenticationService.getAuth().username)
    },
    toggleDropdown() {
      this.showDropdown = !this.showDropdown;
    },
    handleSearch() {
      this.$emit('query', this.query);
      this.query = ""
      this.scrollDown()
    },
    newest(){
      if (this.sortBy === "newest"){
        this.sortBy = ""
      }
      else {
        this.sortBy="newest"
      }
      this.$emit('sortBy', this.sortBy);
    },
    relevance(){
      if (this.sortBy === "relevance"){
        this.sortBy = ""
      }
      else {
        this.sortBy="relevance"
      }
      this.$emit('sortBy', this.sortBy);
    },
    prev(){
      this.pageNumber -= 1
      this.$emit('pageNumber', this.pageNumber);
    },
    next(){
      this.pageNumber += 1
      this.$emit('pageNumber', this.pageNumber);
    }
  }
};

</script>

<style>
input::placeholder {
  color: #ccc;
}


.image-container {
  overflow: hidden;
  position: relative;
}

.code-visual {
  position: absolute;
  top: 600px;
  left: 90%;
  transform: translate(-50%, 650px);
  transition: all 1s ease;
  width: 120%;
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

.header-button {
  float: left;
  position: fixed;
  margin-top: -50px;
}

.footer-button {
  float: left;
  position: fixed;
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

.post-author-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  float: right;
}

.auth-buttons select {
  overflow: hidden;
  background-color: #282828;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
  color: #fff;
  margin-left: 20px;
  cursor: pointer;
  margin-top: 10px;
  font-size: 100%;
}
.dropdown-option {
  background-color: transparent; /* Définissez le fond sur transparent */
  color: white;
  border-bottom: 1px solid #555;
  padding: 10px;
  text-align: center;
}
.dropdown-option:hover {
  background-color: #555; /* Changez la couleur de fond au survol */
}

.dropdown-option:last-child {
  border-bottom: none;
}

.post-create-code {
  background-color: #282828;
  border-radius: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2); /* Ombre tout autour */
  padding: 20px;
  margin-bottom: 20px;
  max-width: calc(33.3333% - 40px); /* 1/3 de la largeur de l'écran moins la marge et le padding */
  position: absolute;
  top: calc(50% + 20px); /* Positionnez-le au milieu de la barre de recherche */
  left: 50%;
  transform: translate(-50%, 550%);
  transition: all 1s ease;
  width: 50%;
}
</style>
