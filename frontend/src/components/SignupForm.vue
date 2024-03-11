<template>
  <div>
    <div v-if="isSignupModalVisible" class="modal" @click="handleModalClick">
      <div class="modal-content">
        <span class="close" @click="hideSignupModal">&times;</span>
        <h1 class="label">Inscription</h1>
        <form @submit.prevent="register">
          <input type="text" id="username" v-model="username" placeholder="Nom d'utilisateur" required>
          <input type="password" id="password" v-model="password" placeholder="Mot de passe" required>
          <input type="password" id="ConfirmPassword" v-model="ConfirmPassword" placeholder="Confirmation du mot de passe" required>
          <span v-if="password !== ConfirmPassword" class="error-message">Les mots de passe ne correspondent pas.</span>
          <button type="submit">S'inscrire</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import {authenticationService} from '@/services/authentication.service'
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
      if (this.password !== this.ConfirmPassword){
        return
      }
      authenticationService.signup({
        username: this.username,
        password: this.password
      }).then(res => {
        authenticationService.addToken('bearer', res.data.bearer)
        authenticationService.addToken('refresh', res.data.refresh)
        this.username = '';
        this.password = '';
        this.$emit('close-modal');
        this.$emit('connect');
      }).catch(err => console.log(err))
    },
    hideSignupModal() {
      this.$emit('close-modal');
    },
    handleModalClick(event) {
      if (!event.target.closest('.modal-content')) {
        this.hideSignupModal();
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
  background-color: rgba(255, 255, 255, 0.1);
  margin: 15% auto;
  padding: 20px;
  border-radius: 10px;
  width: 80%;
  max-width: 600px;
  border: 2px solid #fff;
  backdrop-filter: blur(10px);
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

.label {
  margin-bottom: 15px;
}


input {
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #fff;
  outline: none;
  background-color: rgba(200, 200, 200, 0.2);
  color: #ffffff;
  font-size: large;
  margin: 15px 5%;
}

button {
  padding: 10px 20px;
  margin: 0px 35% 10px;
  background-color: rgba(255, 255, 255, 0.2);;
  color: #fff;
  border: 1px solid #fff;
  border-radius: 50px;
  cursor: pointer;
  font-size: large;
}

button:hover {
  background-color: #fff;
  transition: all .5s;
  color: #000;
}

.error-message {
  color: #ff4d4d; /* Red color */
  font-size: small;
  padding-bottom: 10px;
}
</style>
