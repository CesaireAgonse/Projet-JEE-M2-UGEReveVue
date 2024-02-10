<template>
  <div style="background: #333333;">
    <transition class="slide-up">
      <div class="image-container">
        <img v-if="showImage" src="../assets/home.jpg" @click="scrollImageUp" class="scrollable-image" :style="{ transform: imageTransform }" />
        <div class="welcome-message" :style="{ top: welcomeMessageTop }">
          <h1>Bienvenue sur UGEReveVue</h1>
        </div>
        <div v-if="!(isLoginModalVisible || isSignupModalVisible)" class="search-bar" :style="{ top: searchBarTop }">
          <input class="bar" type="text" placeholder="Rechercher..." />
        </div>
        <div class="auth-buttons">
          <button class="login-button" @click="showLoginModal">Se connecter</button>
          <button class="signup-button" @click="showSignupModal">S'inscrire</button>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
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
      searchBarTop: '40%', // Position initiale de la barre de recherche
      welcomeMessageTop:'20%'
    };
  },
  methods: {
    scrollImageUp() {
      if (this.imageTransform === 'translateY(-90%)') {
        this.imageTransform = '';
        this.searchBarTop = '40%';
        this.welcomeMessageTop = '20%';
      } else {
        this.imageTransform = 'translateY(-90%)';
        this.searchBarTop = '15%';
        this.welcomeMessageTop = '-90%';
      }
    },
    showLoginModal() {
      this.$emit('show-login-modal');
    },
    showSignupModal() {
      this.$emit('show-signup-modal');
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

.search-bar input {
  width: 400px;
  padding: 8px;
  border: none;
  border-radius: 5px;
  outline: none;
}

.auth-buttons {
  position: absolute;
  top: 10px; /* Ajustez la position verticale selon vos préférences */
  right: 10px; /* Ajustez la position horizontale selon vos préférences */
}

.login-button,
.signup-button {
  border: 1px solid #fff; /* Bordure blanche */
  background-color: transparent; /* Fond transparent */
  color: #fff; /* Texte blanc */
  padding: 8px 12px; /* Ajustez le rembourrage selon vos préférences */
  border-radius: 20px; /* Coins arrondis */
  margin-left: 20px; /* Marge entre les boutons */
  cursor: pointer;
  margin-top: 10px;
}

.signup-button{
  margin-right: 10px;
}

.login-button:hover,
.signup-button:hover {
  color: black; /* Texte blanc avec une opacité réduite */
  background-color: #fff; /* Fond blanc */
}

.slide-up{
  background: #333333;
}

</style>
