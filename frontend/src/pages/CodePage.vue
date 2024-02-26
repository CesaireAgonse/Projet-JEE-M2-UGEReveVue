<template>
  <div class="col">
    <button class="basic-button left" @click="home">
      <i>
        <i class="fa-solid fa-arrow-left"></i>
        Home
      </i>
    </button>
    <div class="row">
      <CodeVisual :post="post" v-if="post != null"></CodeVisual>
      <CodeTestVisual :post="post" v-if="post != null"></CodeTestVisual>
      <div class="comments" v-if="post != null">
        <h2>Commentaires:</h2>
        <p v-for="comment in post.comments" :key="comment">
          <CommentVisual  :comment="comment"></CommentVisual>
        </p>
        <div class="row">
          <textarea v-model="contentTextarea" placeholder="Entrez votre texte ici"></textarea>
          <div class="send-button" @click="comment()">
            <i class="fa-regular fa-paper-plane fa-2xl" style="color: #ffffff;"></i>
          </div>
        </div>
      </div>
    </div>
    <div class="reviews" v-if="post != null">
      <h2>Reviews:</h2>
      <p v-for="review in post.reviews" :key="review">
        <ReviewVisual  :post="review"></ReviewVisual>
      </p>
      <div class="row">
        <textarea v-model="reviewTextarea" placeholder="Entrez votre texte ici"></textarea>
        <div class="send-button" @click="review()">
          <i class="fa-regular fa-paper-plane fa-2xl" style="color: #ffffff;"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CodeVisual from "@/visuals/CodeVisual.vue";
import CommentVisual from "@/visuals/CommentVisual.vue";
import CodeTestVisual from "@/visuals/CodeTestVisual.vue";
import ReviewVisual from "@/visuals/ReviewVisual.vue";
import { codeService } from "@/services/code.service";
import {postService } from "@/services/post.service";
import router from "@/router";
import { library, dom } from "@fortawesome/fontawesome-svg-core";
import { fas } from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';
library.add(fas, far, fab)
dom.watch();

export default {
  components: {CodeVisual, CodeTestVisual, CommentVisual, ReviewVisual},
  mounted() {
    document.title = "Code"
    this.code()
  },
  data() {
    return {
      post: null,
      contentTextarea: '',
      reviewTextarea:''
    }
  },
  methods : {
    code(){
      codeService.get(this.$route.params.id).then(res => {
        this.post = res.data
        console.log(this.post)
      })
    },
    home(){
      router.push("/")
    },
    comment(){
      postService.comment(this.$route.params.id, {content:this.contentTextarea, codeSelection:null})
      this.contentTextarea = ''
    },
    review(){
      postService.review(this.$route.params.id, {content:this.reviewTextarea})
      this.reviewTextarea = ''
    }
  }
}
</script>

<style scoped>

.comments {
  background-color: #282828;
  border-radius: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2); /* Ombre tout autour */
  margin: 20px;
  width: calc(33.3333% - 40px);
}

.reviews {
  background-color: #282828;
  border-radius: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2); /* Ombre tout autour */
  margin: 20px;
  padding: 20px;
  width: calc(60% - 40px);
}

.row {
  display: flex;
  flex-direction: row;
}

.col {
  display: flex;
  flex-direction: column;
}

.left {
  float:left;
}

textarea{
  height: 80px;
  width: 80%;
  margin-left: 20px;
  margin-right: 15px;
  resize: none;
  border-radius: 10px;
}

.send-button{
  margin-top: 15px;
  margin-bottom: 25px;
  padding: 10px;
  border: 2px solid #ccc;
  border-radius: 10px;
}

</style>
