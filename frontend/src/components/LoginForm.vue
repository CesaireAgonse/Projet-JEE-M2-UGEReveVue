<template>
  <div>
    <div v-if="isLoginModalVisible" class="modal" @click="handleModalClick">
      <div class="modal-content">
        <span class="close" @click="hideLoginModal">&times;</span>
        <h1 class="label">Connexion</h1>
        <form @submit.prevent="connect">
          <input type="text" id="username" v-model="username" placeholder="Nom d'utilisateur" required>
          <input type="password" id="password" v-model="password" placeholder="Mot de passe" required>
          <span v-if="exist" class="error-message">Nom d'utilisateur ou mot de passe incorrect</span>
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
      password: '',
      exist: false
    };
  },
  methods: {
    connect() {
      authenticationService.login({username: this.username,  password: this.password})
          .then(res => {
            authenticationService.addToken('bearer', res.data.bearer)
            authenticationService.addToken('refresh', res.data.refresh)
            this.username = '';
            this.password = '';
            this.$emit('close-modal');
            this.$emit('connect');

          })
          .catch(() => this.exist = true)
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
  margin-bottom: 30px;
}


input {
  padding: 10px;
  margin-bottom: 30px;
  border-radius: 5px;
  border: 1px solid #fff;
  outline: none;
  background-color: rgba(200, 200, 200, 0.2);
  color: #ffffff;
  font-size: large;
  margin-right: 5%;
  margin-left: 5%;
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
