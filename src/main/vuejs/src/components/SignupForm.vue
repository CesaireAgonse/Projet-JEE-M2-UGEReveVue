<template>
  <div>
    <div v-if="isSignupModalVisible" class="modal">
      <div class="modal-content">
        <span class="close" @click="hideSignupModal">&times;</span>
        <h2>Inscription</h2>
        <form @submit.prevent="register">
          <label for="username">Nom d'utilisateur:</label>
          <input type="text" id="username" v-model="username">

          <label for="password">Mot de passe:</label>
          <input type="password" id="password" v-model="password">

          <label for="password">Confirmation du mot de passe:</label>
          <input type="password" id="ConfirmPassword" v-model="ConfirmPassword">

          <button type="submit">S'inscrire</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  props: {
    isSignupModalVisible: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      username: '',
      password: '',
      ConfirmPassword: ''
    };
  },
  methods: {
    async register() {
      const response = await axios.post("/api/v1/signup", {
        username: this.username,
        password: this.password
      })
      console.log(response);
      this.username = '';
      this.password = '';
      this.ConfirmPassword = '';
      this.$emit('close-modal');
    },
    hideSignupModal() {
      this.$emit('close-modal');
    }
  }
};
</script>

<style scoped>
.modal {
  display: block;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4); /* Fond transparent */
}

.modal-content {
  background-color: transparent; /* Contenu de la modal transparent */
  margin: 15% auto;
  padding: 20px;
  border-radius: 10px;
  width: 80%;
  max-width: 600px;
  border: 2px solid #fff; /* Bordure blanche */
}

.close {
  color: #fff; /* Couleur du texte */
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: #fff;
  text-decoration: none;
  cursor: pointer;
}

form {
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 10px;
  color: #fff; /* Couleur du texte */
}

input {
  padding: 10px;
  margin-bottom: 15px;
  border-radius: 5px;
  border: 1px solid #fff; /* Bordure blanche */
  outline: none;
  background-color: transparent; /* Fond transparent */
  color: #fff; /* Couleur du texte */
}

button {
  padding: 10px 20px;
  background-color: transparent; /* Fond transparent */
  color: #fff; /* Couleur du texte */
  border: 1px solid #fff; /* Bordure blanche */
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #fff; /* Fond blanc */
  color: #000; /* Couleur du texte */
}
</style>
