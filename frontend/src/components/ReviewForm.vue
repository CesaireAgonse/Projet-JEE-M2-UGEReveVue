<template>
  <div>
    <form @submit.prevent="submitReview" enctype="multipart/form-data" id="postForm">
      <div class="form-group">
        <label for="title-input">Title:</label>
        <input type="text" id="title-input" v-model="reviewForm.title" placeholder="Enter your title" required>
      </div>
      <div v-for="(field, index) in reviewForm.content" :key="index">
        <code v-if="field.codeSelection !== ''" class="language-java">{{ field.codeSelection }}</code>
        <textarea class="review" v-model="field.content" :name="'content[' + index + '].content'" placeholder="Enter a comment" required></textarea>
        <button @click="addCode(field)" class="basic-button">Add selected code</button>
        <button @click="removeField(index)" class="basic-button">Remove</button>
      </div>
      <button @click="addField" class="basic-button">Ajouter un champ</button>
      <div type="submit" class="send-button" @click="review()">
        <i class="fa-regular fa-paper-plane fa-2xl" style="color: #ffffff;"></i>
      </div>
    </form>
  </div>
</template>

<script scoped>
import {postService} from "@/services/post.service";

export default {
  data() {
    return {
      reviewForm: {
        title: '',
        content: [],
      },
    };
  },
  methods: {
    addField() {
      this.reviewForm.content.push({ content: '', codeSelection: '' });
    },
    removeField(index) {
      this.reviewForm.content.splice(index, 1);
    },
    addCode(field) {
      const baseCode = document.getElementById('codeBlock').innerText;
      const selection = window.getSelection().toString().trim();
      if (selection !== '') {
        field.codeSelection = selection;
      } else {
        field.codeSelection = baseCode;
      }
    },
    review(){
      postService.review(this.$route.params.id, this.reviewForm).then(() => {
        this.reviewForm.title = ''
        this.reviewForm.content = []
        this.$emit('refresh')
      })
    },
  },
};
</script>

<style scoped>
.send-button{
  margin-top: 15px;
  margin-bottom: 25px;
  padding: 10px;
  border: 2px solid #ccc;
  border-radius: 10px;
}

.send-button:hover{
  background: #707070;
  color: black;
}

textarea,
input {
  padding-top: 10px;
  padding-bottom: 10px;
  margin-bottom: 30px;
  border-radius: 5px;
  border: 1px solid #fff;
  outline: none;
  background-color: rgba(200, 200, 200, 0.2);
  color: #ffffff;
  font-size: large;
  width: 100%;
}
</style>