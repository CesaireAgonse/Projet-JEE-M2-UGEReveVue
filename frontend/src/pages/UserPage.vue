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
        <img v-if="photo == null" src="../assets/profile.jpg" alt="Photo de profil" class="profile-photo">
        <img v-if="photo != null" :src="photo" alt="Photo de profil" class="profile-photo">
        <div class="profile-info">
          <p style="font-size: 200%">{{ username }}</p>
          <p style="margin-top: -20px">{{nbFollowed}} <i class="fa-solid fa-user-group"></i></p>
          <p>{{ "Ceci est une description de profile" }}</p>
          <button v-if="auth != null && auth.username !== username" class="basic-button button-profile left" @click="follow"><i class="fa-solid fa-user-plus"></i> Suivre</button>
          <button v-if="auth != null && auth.username !== username" class="basic-button button-profile left" @click="unfollow"><i class="fa-solid fa-user-minus"></i> Ne plus suivre</button>
          <button v-if="auth != null && auth.username === username" class="basic-button button-profile left" @click="showPasswordModal">Modifier son mot de passe</button>
          <button v-if="auth != null && auth.username === username" class="basic-button button-profile left" @click="uploadPhoto"><i class="fa-solid fa-camera"></i> Changer la photo de profil</button>
        </div>
      </div>
    </div>
    </div>
    <div class="profile-tabs">
      <button class="profile-tab" :class="{ 'profile-tab-selected': selectedTab === 'followed' }" @click="showFollowed">Suivis</button>
      <button class="profile-tab" :class="{ 'profile-tab-selected': selectedTab === 'codes' }" @click="showCodes">Codes</button>
      <button class="profile-tab" :class="{ 'profile-tab-selected': selectedTab === 'reviews' }" @click="showReviews">Revues</button>
      <button class="profile-tab" :class="{ 'profile-tab-selected': selectedTab === 'comments' }" @click="showComments">Commentaires</button>
    </div>
  <div v-if="selectedTab === 'followed'">
    <div class="row" v-for="user in userPage.users" :key="user"><p>{{ user.username }}</p></div>
    <div>
      <button v-if="userPage.pageNumber > 0" class="basic-button" @click="prevUser">Page précédente</button>
      <button v-if="userPage.pageNumber < userPage.totalPage" class="basic-button" @click="nextUser">Page suivante</button>
    </div>
  </div>
    <div v-if="selectedTab === 'codes'">
      <div class="row" v-for="code in codePage.codes" :key="code"><CodeVisual :post="code" @refresh="codes"/></div>
      <div>
        <button v-if="codePage.pageNumber > 0" class="basic-button" @click="prevCode">Page précédente</button>
        <button v-if="codePage.pageNumber < codePage.totalPage" class="basic-button" @click="nextCode">Page suivante</button>
      </div>
    </div>
    <div v-if="selectedTab === 'reviews'">
      <div class="row" v-for="review in reviewPage.reviews" :key="review"><ReviewVisual :post="review" @refresh="reviews"/></div>
      <div>
        <button v-if="reviewPage.pageNumber > 0" class="basic-button" @click="prevReview">Page précédente</button>
        <button v-if="reviewPage.pageNumber < reviewPage.totalPage" class="basic-button" @click="nextReview">Page suivante</button>
      </div>
    </div>
    <div v-if="selectedTab === 'comments'">
      <div class="row" v-for="comment in commentPage.comments" :key="comment"><CommentVisual :comment="comment" @refresh="comments"/></div>
      <div>
        <button v-if="commentPage.pageNumber > 0" class="basic-button" @click="prevComment">Page précédente</button>
        <button v-if="commentPage.pageNumber < commentPage.totalPage" class="basic-button" @click="nextComment">Page suivante</button>
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
import CodeVisual from "@/visuals/CodeVisual.vue";
import ReviewVisual from "@/visuals/ReviewVisual.vue";
import CommentVisual from "@/visuals/CommentVisual.vue";
library.add(fas, far, fab)
dom.watch();

export default {
  components: {ReviewVisual, CodeVisual, PasswordForm, CommentVisual},
  mounted() {
    document.title = "Profile"
    userService.user(this.$route.params.name).then(res => {
      this.username = res.data.username
      this.nbFollowed = res.data.followed.length
      if (res.data.profilePhoto != null){
        this.photo = "data:image/jpg;base64," + res.data.profilePhoto;

      }
    }).catch(err => console.log(err))
    this.codes()
    this.reviews()
    this.comments()
    this.users()
    document.body.style.overflowY = "visible"
  },
  data() {
    return {
      username: null,
      nbFollowed: 0,
      photo: null,
      auth: authenticationService.getAuth(),
      isPasswordModalVisible: false,
      selectedTab: 'followed',
      codePage:{
        codes:[],
        pageNumber:0,
        totalPage:0
      },
      reviewPage:{
        reviews:[],
        pageNumber:0,
        totalPage:0
      },
      commentPage:{
        comments:[],
        pageNumber:0,
        totalPage:0
      },
      userPage:{
        users:[],
        pageNumber:0,
        totalPage:0
      }
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
    },
    codes(){
      userService.codes(this.$route.params.name, this.codePage.pageNumber).then(res => {
        this.codePage.codes = res.data.codes
        this.codePage.pageNumber = res.data.pageNumber
        this.codePage.totalPage = res.data.maxPageNumber
      }).catch(err => console.log(err))
    },
    reviews(){
      userService.reviews(this.$route.params.name, this.reviewPage.pageNumber).then(res => {
        this.reviewPage.reviews = res.data.reviews
        this.reviewPage.pageNumber = res.data.pageNumber
        this.reviewPage.totalPage = res.data.maxPageNumber
      }).catch(err => console.log(err))
    },
    comments(){
      userService.comments(this.$route.params.name, this.commentPage.pageNumber).then(res => {
        this.commentPage.comments = res.data.comments
        this.commentPage.pageNumber = res.data.pageNumber
        this.commentPage.totalPage = res.data.maxPageNumber
      }).catch(err => console.log(err))
    },
    users(){
      userService.followed(this.$route.params.name, this.userPage.pageNumber).then(res => {
        this.userPage.users = res.data.users
        this.userPage.pageNumber = res.data.pageNumber
        this.userPage.totalPage = res.data.maxPageNumber
      }).catch(err => console.log(err))
    },
    showCodes() {
      this.selectedTab = 'codes';
    },
    showReviews() {
      this.selectedTab = 'reviews';
    },
    showComments() {
      this.selectedTab = 'comments';
    },
    showFollowed() {
      this.selectedTab = 'followed';
    },
    prevCode(){
      this.codePage.pageNumber -= 1
      this.codes()
    },
    nextCode(){
      this.codePage.pageNumber += 1
      this.codes()
    },
    prevReview(){
      this.reviewPage.pageNumber -= 1
      this.reviews()
    },
    nextReview(){
      this.reviewPage.pageNumber += 1
      this.reviews()
    },
    prevComment(){
      this.commentPage.pageNumber -= 1
      this.comments()
    },
    nextComment(){
      this.commentPage.pageNumber += 1
      this.comments()
    },
    prevUser(){
      this.userPage.pageNumber -= 1
      this.users()
    },
    nextUser(){
      this.userPage.pageNumber += 1
      this.users()
    },
    uploadPhoto() {
      // Créez une référence à l'élément input de type file
      const fileInput = document.createElement('input');
      fileInput.type = 'file';
      fileInput.style.display = 'none';

      // Écoutez l'événement 'change' de l'input file
      fileInput.addEventListener('change', (event) => {
        const file = event.target.files[0];
        let formData = new FormData();
        formData.append('photo', file);
        userService.photo(formData).then(() => {
          userService.user(this.$route.params.name).then(res => {
            if (res.data.profilePhoto != null){
              this.photo = "data:image/jpg;base64," + res.data.profilePhoto;
            }
          }).catch(err => console.log(err))
        })
      });
      fileInput.click();
    },
  }
}
</script>
<style scoped>
.button-profile {
  font-size: 110%;
  margin-bottom: 20px;
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
  margin: 40px 30px 10px 10px;
}

.profile-info {
  margin-top: -20px;
  font-size: 120%;
}

.left {
  float: left;
}

.banner-profile {
  margin-top: -50px;
  background: #282828;
}

.row {
  display: flex;
  flex-direction: row;
  justify-content: center;
}

.profile-tabs {
  display: flex;
  justify-content: center; /* Centrer horizontalement les onglets */
  margin-top: 20px; /* Espacement entre les onglets et le contenu */
}

.profile-tab {
  margin: 0 5px; /* Espacement entre les onglets */
  padding: 5px 10px; /* Espacement interne des onglets */
  font-size: 14px; /* Taille de la police des onglets */
  border: 1px solid #282828; /* Ajout d'une bordure */
  border-radius: 5px; /* Bordure arrondie */
  background-color: white; /* Couleur de fond transparente */
  color: #282828; /* Couleur du texte des onglets */
  cursor: pointer; /* Curseur de la souris en pointeur */
  transition: background-color 0.3s, color 0.3s; /* Transition sur les propriétés de style */
}

.profile-tab:hover {
  background-color: #282828; /* Couleur de fond des onglets au survol */
  color: white; /* Couleur du texte des onglets au survol */
}

.profile-tab-selected {
  background-color: #282828; /* Couleur de fond pour l'onglet sélectionné */
  color: white;
}

.column {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}
</style>
