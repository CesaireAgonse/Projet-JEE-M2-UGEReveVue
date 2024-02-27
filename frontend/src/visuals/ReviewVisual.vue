<template>
  <div class="post">
    <div class="post-header">
      <div class="post-header-info" @click="user()">
        <img class="post-author-avatar" src="../assets/profile.jpg" alt="Author Avatar" />
        <div class="post-author-info">
          <h2>{{ post.userInformation.username }}</h2>
        </div>
      </div>
      <p class="post-date">{{ post.date }}</p>
      <div class="post-actions"></div>
    </div>
    <div @click="review()">
      <h2 class="post-title">{{ post.title }}</h2>
      <pre>{{ post.content }}</pre>
    </div>
    <div class="post-footer">
      <div class="post-votes">
        <button class="post-button" @click="like">
          <i class="fa-regular fa-thumbs-up fa-beat" style="color: #00ffb3"></i>
        </button>
        <p style="padding-right: 7px">{{ this.score }}</p>
        <button class="post-button" @click="dislike">
          <i class="fa-regular fa-thumbs-down fa-beat" style="color: #f44e4e"></i>
        </button>
      </div>
      <div  class="post-votes">
        <button class="post-button">
          <i class="fa-regular fa-comment fa-bounce" style="color: #74C0FC"></i>
        </button>
        <p style="padding-right: 7px">{{post.comments.length }}</p>
        <button class="post-button" >
          <i class="fa-regular fa-pen-to-square fa-2xs" style="color: #74C0FC"></i>
        </button>
        <p>{{post.reviews.length }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import { library, dom } from "@fortawesome/fontawesome-svg-core";
import { fas } from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';
import { postService } from "@/services/post.service";
import router from "@/router";

library.add(fas, far, fab)
dom.watch();
export default {
  name: 'ReviewVisual',
  props: {
    post: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      score:this.post.score,
    }
  },
  methods: {
    like() {
      postService.vote(this.post.id, "UpVote").then(res => {
        this.score=res.data
      })
    },
    dislike() {
      postService.vote(this.post.id, "DownVote").then(res => {
        this.score=res.data
      })
    },
    review(){
      router.push('/reviews/' + this.post.id)
    },
    user(){
      router.push('/profile/' + this.post.userInformation.username)
    },
  }
}
</script>

<style scoped>
.post {
  background-color: #1e1e1e;
  border-radius: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2); /* Ombre tout autour */
  padding: 20px;
  max-width: 100%;
  text-align: left;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.post-header-info {
  display: flex;
  align-items: center;
}

.post-author-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.post-author-info h2 {
  font-size: 16px;
  margin: 0;
}

.post-date {
  font-size: 14px;
  color: #666;
  margin: 0 0 0 auto;
}

.post-title {
  font-size: 20px;
  margin-bottom: 10px;
  text-align: left;
}

.post-description {
  font-size: 16px;
  margin-bottom: 15px;
  text-align: left;
}

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.post-votes {
  display: flex;
  align-items: center;
}

.post-button {
  background-color: transparent; /* Green */
  border: none;
  color: white;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 30px;
  margin-bottom: 10px;
  cursor: pointer;
  border-radius: 5px;
}

.fa-thumbs-up,
.fa-thumbs-down,
.fa-comment {
  font-size: 20px;
  margin-right: 5px;
}
</style>