<template>
  <div class="post">
    <div class="post-header">
      <div class="post-header-info" @click="user()">
        <img class="post-author-avatar" src="../assets/profile.jpg" alt="Author Avatar" />
        <div class="post-author-info">
          <h2>{{ comment.userInformation.username }}</h2>
        </div>
      </div>
      <p class="post-date">{{ comment.date }}</p>
      <div class="post-actions"></div>
    </div>
    <pre v-if="comment.codeSelection !== null && comment.codeSelection !== ''" class="select-code"><code class="language-java">{{ comment.codeSelection }}</code></pre>
    <pre><div v-html="markdownToHtml(comment.content)"></div></pre>
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

library.add(fas, far, fab)
dom.watch();
export default {
  mounted() {
    this.highlightCode();
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

</style>
