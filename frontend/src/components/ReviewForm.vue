<template>
  <div v-if="modal" class="modal" @click="handleModalClick">
    <div class="modal-content">
      <span class="close" @click="hideCodeModal">&times;</span>
      <h2 class="label">Ajouter un champ d'une ancienne review</h2>
      <div v-for="(content) in reviewContent" :key="content">
        <div class="post" @click="select(content)">
          <pre v-if="content.codeSelection !== null && content.codeSelection !== ''" class="message"><code class="language-java">{{ content.codeSelection }}</code></pre>
          <pre class="post-content"><div v-html="markdownToHtml(content.content)"></div></pre>
        </div>
      </div>
      <div class="row">
        <button v-if="pageNumber > 0" class="basic-button prevButton" @click="prev">
          <i class="fa-solid fa-arrow-left"></i>
        </button>
        <button v-if="pageNumber < maxPageNumber" class="basic-button nextButton" @click="next">
          <i class="fa-solid fa-arrow-right"></i>
        </button>
      </div>
    </div>
  </div>
  <div class="create">
    <form enctype="multipart/form-data" id="postForm">
      <div class="form-group">
        <input type="text" id="title-input" v-model="reviewForm.title" placeholder="Enter your title" required>
      </div>
      <div v-for="(field, index) in reviewForm.content" :key="index">
        <code v-if="field.codeSelection !== ''" class="language-java">{{ field.codeSelection }}</code>
        <div v-if="field.codeSelection !== ''" class="basic-button cross-button right" @click="removeCode(field)"> <i class="fa-solid fa-xmark"></i></div>
        <textarea class="review" v-model="field.content" :name="'content[' + index + '].content'" placeholder="Enter a comment" required></textarea>
        <button type="button" @click="addCode(field)" class="basic-button">Ajouter le code selectionné</button>
        <button type="button" @click="removeField(index)" class="basic-button">Supprimer le champ</button>
      </div>
      <div class="space"></div>
      <button type="button" @click="addField" class="other-button">Ajouter un champ</button>
      <button type="button" @click="addReviewField" class="other-button">Ajouter un champ d'une ancienne review</button>
      <div type="submit" class="send-button" @click="review()">
        <i class="fa-regular fa-paper-plane fa-2xl" style="color: #ffffff;"></i>
      </div>
    </form>
  </div>
</template>

<script scoped>
import {postService} from "@/services/post.service";
import {userService} from "@/services/user.service";
import {authenticationService} from "@/services/authentication.service";
import { library, dom } from "@fortawesome/fontawesome-svg-core";
import { fas } from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';
import MarkdownIt from "markdown-it";
import Prism from "prismjs";
library.add(fas, far, fab)
dom.watch();
export default {
  data() {
    return {
      modal: false,
      reviewContent: [],
      pageNumber:0,
      maxPageNumber:0,
      reviewForm: {
        title: '',
        content: [{ content: '', codeSelection: '' }],
      },
    };
  },
  methods: {
    addField() {
      this.reviewForm.content.push({ content: '', codeSelection: '' });
    },
    addReviewField(){
      this.oldReviewsContents()
      this.modal = true
    },
    removeField(index) {
      if (this.reviewForm.content.length > 1){
        this.reviewForm.content.splice(index, 1);
      }else{
        alert("Il doit y avoir au moins un commentaire pour créer une revue");
      }
    },
    addCode(field) {
      const selection = window.getSelection().toString().trim();
      if (selection !== '') {
        field.codeSelection = selection;
      }else{
        alert("Veuillez selectionner un morceau de code en le surlignant avec votre souris.")
      }
    },
    removeCode(field){
      field.codeSelection = '';
    },
    review(){
      postService.review(this.$route.params.id, this.reviewForm).then(() => {
        this.reviewForm.title = ''
        this.reviewForm.content = [{ content: '', codeSelection: '' }]
        this.$emit('refresh')
      })
    },
    hideCodeModal() {
      this.modal = false;
    },
    handleModalClick(event) {
      if (!event.target.closest('.modal-content')) {
        this.hideCodeModal();
      }
    },
    oldReviewsContents(){
      userService.reviewsContents(authenticationService.getAuth().username, this.pageNumber).then((res) => {
        this.reviewContent = res.data.reviewContent
        this.pageNumber = res.data.pageNumber
        this.maxPageNumber = res.data.maxPageNumber
      })
    },
    next(){
      this.pageNumber++;
      this.oldReviewsContents()
    },
    prev(){
      this.pageNumber--
      this.oldReviewsContents()
    },
    markdownToHtml(markdown) {
      const md = new MarkdownIt();
      return md.render(markdown);
    },
    select(content){
      this.reviewForm.content.push({ content: content.content, codeSelection: content.codeSelection });
      this.hideCodeModal()
    },
    highlightCode() {
      Prism.highlightAll();
    }
  },
};
</script>

<style scoped>
.post {
  background-color: #1e1e1e;
  border-radius: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2); /* Ombre tout autour */
  text-align: left;
  font-size: 75%;
  width: 100%;
  margin: 10px 10px 10px -10px;
  padding: 0 10px;

}
.send-button{
  margin: 15px 100px 25px;
  padding: 10px;
  border: 2px solid #ccc;
  border-radius: 10px;
}

.send-button:hover{
  background: #707070;
  color: black;
}

textarea {
  margin-top: 30px;
  height: 100px;
}

textarea,
input {
  padding-top: 10px;
  padding-bottom: 10px;
  width: 95%;
  margin-left: 20px;
  margin-right: 20px;
  border-radius: 5px;
  border: 1px solid #fff;
  outline: none;
  background-color: rgba(200, 200, 200, 0.2);
  color: #ffffff;
  font-size: large;
}

.space{
  padding: 20px;
}

.other-button{
  border: 1px solid #fff;
  background-color: #fff;
  color: black;
  padding: 8px 12px;
  border-radius: 20px;
  margin-left: 20px;
  cursor: pointer;
  margin-right: 20px;
}

.other-button:hover {
  color: #fff;
  background-color: transparent;
}

.modal {
  display: block;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  overflow-y: auto;
}

.modal-content {
  background-color: rgba(255, 255, 255, 0.1);
  margin: 5% auto;
  padding: 20px;
  border-radius: 10px;
  width: 80%;
  max-width: 800px;
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


.label {
  margin-bottom: 30px;
}

.create {
  background-color: #282828;
  border-radius: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2); /* Ombre tout autour */
  margin: 20px;
  padding: 20px;
  width: calc(60% - 40px);
}


.message{
  max-width: 100%;
  max-height: 100px;
  overflow-y: auto;
}

.post-content{
  white-space: pre-wrap;
  word-wrap: break-word;
}

</style>
