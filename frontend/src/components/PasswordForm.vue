<template>
  <div>
    <div v-if="isPasswordModalVisible" class="modal" @click="handleModalClick">
      <div class="modal-content">
        <span class="close" @click="hideUpdateModal">&times;</span>
        <h1 class="label">Modifier son mot de passe</h1>
        <form @submit.prevent="updatePassword">
          <input type="password" id="currentPassword" v-model="currentPassword" placeholder="Mot de passe actuel" required>
          <input type="password" id="newPassword" v-model="newPassword" placeholder="Nouveau mot de passe" required>
          <input type="password" id="confirmPassword" v-model="confirmPassword" placeholder="Confirmation du nouveau mot de passe" required>
          <span v-if="newPassword !== confirmPassword" class="error-message">Les mots de passe ne correspondent pas.</span>
          <button type="submit">Confirmer</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import {userService} from "@/services/user.service";
export default {
  props: {
    isPasswordModalVisible: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    };
  },
  methods: {
    async updatePassword() {
      let response = userService.updatePassword({currentPassword: this.currentPassword, newPassword: this.newPassword})
      console.log(response);
      this.currentPassword = '';
      this.newPassword = '';
      this.confirmPassword = '';
      this.$emit('close-modal');
    },
    hideUpdateModal() {
      this.$emit('close-modal');
    },
    handleModalClick(event) {
      if (!event.target.closest('.modal-content')) {
        this.hideUpdateModal();
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
  margin: 10px 35% 10px;
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