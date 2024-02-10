<template>
  <div>
    <div v-if="isLoginModalVisible" class="modal">
      <div class="modal-content">
        <span class="close" @click="hideLoginModal">&times;</span>
        <h2>Connexion</h2>
        <form @submit.prevent="connect">
          <label for="username">Nom d'utilisateur:</label>
          <input type="text" id="username" v-model="username">

          <label for="password">Mot de passe:</label>
          <input type="password" id="password" v-model="password">

          <button type="submit">Se connecter</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  props: {
    isLoginModalVisible: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      username: '',
      password: ''
    };
  },
  methods: {
    async connect() {
      const response = await axios.post("/api/v1/login", {
        username: this.username,
        password: this.password
      })
      console.log(response);
      this.username = '';
      this.password = '';
      this.$emit('close-modal');
    },
    hideLoginModal() {
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
