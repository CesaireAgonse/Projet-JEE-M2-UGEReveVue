<template>
  <div>
    <div v-if="isLoginModalVisible" class="modal" @click="handleModalClick">
      <div class="modal-content">
        <span class="close" @click="hideLoginModal">&times;</span>
        <h2>Connexion</h2>
        <form @submit.prevent="connect">
          <label for="username">Nom d'utilisateur:</label>
          <input type="text" id="username" v-model="username" required>

          <label for="password">Mot de passe:</label>
          <input type="password" id="password" v-model="password" required>

          <button type="submit">Se connecter</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import {authenticationService} from '@/services/authentication.service'
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
    connect() {
      authenticationService.login({username: this.username,  password: this.password})
          .then(res => {
              authenticationService.addToken(res.data.bearer)
              this.username = '';
              this.password = '';
              this.$emit('close-modal');
              this.$emit('connect');
          })
          .catch(err => console.log(err))
    },
    hideLoginModal() {
      this.$emit('close-modal');
    },
    handleModalClick(event) {
      if (!event.target.closest('.modal-content')) {
        this.hideLoginModal();
      }
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
  background-color: rgba(0, 0, 0, 0.4);
}

.modal-content {
  background-color: transparent;
  margin: 15% auto;
  padding: 20px;
  border-radius: 10px;
  width: 80%;
  max-width: 600px;
  border: 2px solid #fff;
}

.close {
  color: #fff;
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
  color: #fff;
}

input {
  padding: 10px;
  margin-bottom: 15px;
  border-radius: 5px;
  border: 1px solid #fff;
  outline: none;
  background-color: transparent;
  color: #fff;
}

button {
  padding: 10px 20px;
  background-color: transparent;
  color: #fff;
  border: 1px solid #fff;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #fff;
  color: #000;
}
</style>
