<template  v-if="username !== null">
  <button class="basic-button left" @click="home">Home</button>
  <div class="profile">
    <div class="profile-header">
      <img src="../src/assets/profile.jpg" alt="Photo de profil" class="profile-photo">
      <div class="profile-info">
        <h1>{{ username }}</h1>
        <p>{{ "Ceci est une description de profile" }}</p>
        <button class="basic-button button-profile">Suivre</button>
        <button class="basic-button button-profile">Ne plus suivre</button>
        <button class="basic-button button-profile">Modifier son mot de passe</button>
      </div>
    </div>
  </div>
</template>

<script>

import {userService} from "@/services/user.service";
import router from "@/router";
export default {
  mounted() {
    document.title = "Profile"
    userService.profile(this.$route.params.name).then(res => {
          this.username = res.data.username
        }).catch(err => console.log(err))
  },
  data() {
    return {
      username: null
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
