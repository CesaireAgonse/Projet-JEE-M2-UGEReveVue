<template  v-if="username !== null">
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
        <h1 class="left">{{ username }}</h1>
        <p class="left"> {{nbFollowed}} <i class="fa-solid fa-user-group"></i></p>
        <p class="left">{{ "Ceci est une description de profile" }}</p>
        <button class="basic-button button-profile left">Modifier son mot de passe</button>
      </div>
    </div>
  </div>
</template>

<script>

import {userService} from "@/services/user.service";
import router from "@/router";
import { library, dom } from "@fortawesome/fontawesome-svg-core";
import { fas } from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';
library.add(fas, far, fab)
dom.watch();

export default {
  mounted() {
    document.title = "Profile"
    userService.profile().then(res => {
          this.username = res.data.username
          this.nbFollowed = res.data.followed.size()
        }).catch(err => console.log(err))
  },
  data() {
    return {
      username: null,
      nbFollowed: 0
    };
  },
  methods : {
    home(){
      router.push("/")
    }
  }
}
</script>

<style scoped>
.button-profile{
  font-size: 120%;
}

.profile {
  max-width: 600px;
  /* Aligner le contenu à gauche */
  margin: 50px auto 0;
}

.profile-header {
  display: flex;
  align-items: flex-start; /* Aligner les éléments en haut */
}

.profile-photo {
  width: 250px;
  height: 250px;
  margin-right: 20px; /* Ajouter une marge à droite pour séparer la photo du contenu */
}

.profile-info {
  font-size: 120%;
}
.left {
  float:left;
}

</style>
