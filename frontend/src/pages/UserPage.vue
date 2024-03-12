<template  v-if="username !== null">
  <PasswordForm v-if="isPasswordModalVisible" @close-modal="hidePasswordModal" :isPasswordModalVisible="isPasswordModalVisible"/>
  <div class="banner-profile">
    <button class="basic-button left" @click="home">
      <i>
        <i class="fa-solid fa-arrow-left"></i>
        Home
      </i>
    </button>
    <div class="profile">
      <div class="profile-header">
        <img src="../assets/profile.jpg" alt="Photo de profil" class="profile-photo">
        <div class="profile-info">
          <p style="font-size: 200%">{{ username }}</p>
          <p style="margin-top: -20px">{{nbFollowed}} <i class="fa-solid fa-user-group"></i></p>
          <p>{{ "Ceci est une description de profile" }}</p>
          <button v-if="auth != null && auth.username !== username" class="basic-button button-profile left" @click="follow"><i class="fa-solid fa-user-plus"></i> Suivre</button>
          <button v-if="auth != null && auth.username !== username" class="basic-button button-profile left" @click="unfollow"><i class="fa-solid fa-user-minus"></i> Ne plus suivre</button>
          <button v-if="auth != null && auth.username === username" class="basic-button button-profile left" @click="showPasswordModal">Modifier son mot de passe</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

import {userService} from "@/services/user.service";
import {authenticationService} from "@/services/authentication.service";
import router from "@/router";
import { library, dom } from "@fortawesome/fontawesome-svg-core";
import { fas } from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';
import PasswordForm from "@/components/PasswordForm.vue";
library.add(fas, far, fab)
dom.watch();

export default {
  components: {PasswordForm},
  mounted() {
    document.title = "Profile"
    userService.user(this.$route.params.name).then(res => {
      this.username = res.data.username
      this.nbFollowed = res.data.followed.length
    }).catch(err => console.log(err))
  },
  data() {
    return {
      username: null,
      nbFollowed: 0,
      auth: authenticationService.getAuth(),
      isPasswordModalVisible: false
    };
  },
  methods : {
    home(){
      router.push("/")
    },
    follow() {
      userService.follow(this.username)
    },
    unfollow() {
      userService.unfollow(this.username)
    },
    showPasswordModal() {
      this.isPasswordModalVisible = true;
    },
    hidePasswordModal() {
      this.isPasswordModalVisible = false;
    }
  }
}
</script>

<style scoped>
.button-profile{
  font-size: 110%;
}

.profile {
  max-width: 600px;
  margin: 50px auto 0;
}

.profile-header {
  display: flex;
  text-align: left;
}

.profile-photo {
  width: 250px;
  height: 250px;
  margin: 10px 30px 10px 10px;
}

.profile-info {
  margin-top: -20px;
  font-size: 120%;
}
.left {
  float:left;
}

.banner-profile{
  margin-top: -50px;
  background: #282828;
}

</style>
