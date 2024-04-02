<template>
  <div>
    <div class="post-header">
      <div class="post-header-info" @click="user()">
        <img v-if="photo == null" class="post-author-avatar" src="../assets/profile.jpg" alt="Author Avatar" />
        <img v-if="photo != null" class="post-author-avatar" :src="photo" alt="Author Avatar" />
        <div class="post-author-info">
          <h2>{{ comment.userInformation.username }}</h2>
        </div>
      </div>
      <p class="post-date">{{ comment.date }}</p>
      <div class="post-actions"></div>
    </div>
    <pre v-if="comment.codeSelection !== null && comment.codeSelection !== ''" class="select-code"><code class="language-java">{{ comment.codeSelection }}</code></pre>
    <pre class="post-content"><div v-html="markdownToHtml(comment.content)"></div></pre>
    <button v-if="auth !== null && auth.role === 'ADMIN'" class="post-button" @click="del">
      <i class="fa-solid fa-trash-can fa-bounce fa-xs" style="color: #b3b2b2;"></i>
    </button>
  </div>
</template>

<script>
import { library, dom } from "@fortawesome/fontawesome-svg-core";
import { fas } from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';
import router from "@/router";
import MarkdownIt from "markdown-it";
import Prism from 'prismjs';
import "prismjs/themes/prism-tomorrow.css"
import 'prismjs/components/prism-java'
import {authenticationService} from "@/services/authentication.service";
import {commentService} from "@/services/comment.service";

library.add(fas, far, fab)
dom.watch();
export default {
  mounted() {
    this.highlightCode();
    if (this.comment.userInformation.profilePhoto != null){
      this.photo = "data:image/jpg;base64," + this.comment.userInformation.profilePhoto
    }
  },
  data() {
    return {
      photo: null,
      auth: authenticationService.getAuth()
    }
  },
  props: {
    comment: {
      type: Object,
      required: true
    }
  },
  methods: {
    user() {
      router.push('/profile/' + this.comment.userInformation.username)
    },
    markdownToHtml(markdown) {
      const md = new MarkdownIt();
      return md.render(markdown);
    },
    highlightCode() {
      Prism.highlightAll();
    },
    del() {
      if (confirm("Êtes-vous sûr de vouloir supprimer ce commentaire ?")) {
        commentService.del(this.comment.id).then(() => {
          this.$emit("refresh")
        })
      }
    }
  }
}
</script>

<style scoped>
.post {
  background-color: #1e1e1e;
  border-radius: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2); /* Ombre tout autour */
  margin: 20px;
  text-align: left;
  padding: 10px;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.post-header-info {
  font-size: 12px;
  display: flex;
  align-items: center;
}

.post-date {
  font-size: 12px;
  color: #666;
  margin: 0 0 0 auto;
}

.post-author-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 10px;
}

.select-code{
  max-height: 100px;
  font-size: 75%;
}

.post-button {
  background-color: transparent;
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

.post-content{
  white-space: pre-wrap;
  word-wrap: break-word;
}

</style>
