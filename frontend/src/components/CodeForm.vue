<template>
  <div>
    <div v-if="isCodeModalVisible" class="modal" @click="handleModalClick">
      <div class="modal-content">
        <span class="close" @click="hideCodeModal">&times;</span>
        <h1 class="label">Poster un Code</h1>
        <form @submit.prevent="post">
          <div class="form-group">
            <label for="title">Titre:</label>
            <input type="text" id="title" v-model="title" required>
          </div>
          <div class="form-group">
            <label for="description">Description:</label>
            <textarea id="description" v-model="description" required></textarea>
          </div>
          <div class="form-group">
            <label for="file1">Classe Java:</label>
            <input type="file" id="file1" @change="handleFileUpload(1)" accept=".java" required>
            <span v-if="file1">Fichier sélectionné: {{ file1.name }}</span>
          </div>
          <div class="form-group">
            <label for="file2">Classe Test:</label>
            <input type="file" id="file2" @change="handleFileUpload(2)" accept=".java">
            <span v-if="file2">Fichier sélectionné: {{ file2.name }}</span>
          </div>
          <button type="submit">Poster</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    isCodeModalVisible: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      title: '',
      description: '',
      file1: null,
      file2: null
    };
  },
  methods: {
    handleFileUpload(fileNumber) {
      const fileInput = event.target;
      const file = fileInput.files[0];
      if (fileNumber === 1) {
        this.file1 = file;
      } else if (fileNumber === 2) {
        this.file2 = file;
      }
    },
    post() {
      const formData = {
        title: this.title,
        description: this.description,
        file1: this.file1,
        file2: this.file2
      };
      console.log(formData);
      this.hideCodeModal();
    },
    hideCodeModal() {
      this.$emit('close-modal');
    },
    handleModalClick(event) {
      if (!event.target.closest('.modal-content')) {
        this.hideCodeModal();
      }
    }
  }
};
</script>

<style scoped>
.modal {
  display: block;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
}

.modal-content {
  background-color: rgba(255, 255, 255, 0.1);
  margin: 5% auto;
  padding: 20px;
  border-radius: 10px;
  width: 80%;
  max-width: 600px;
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

form {
  display: flex;
  flex-direction: column;
}

.label {
  margin-bottom: 30px;
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

.form-group {
  display: flex;
  flex-direction: column;  /* Ajout de cette ligne pour afficher les labels au-dessus des champs */
}

button {
  padding: 10px 20px;
  margin: 0px 35% 10px;
  background-color: rgba(255, 255, 255, 0.2);;
  color: #fff;
  border: 1px solid #fff;
  border-radius: 50px;
  cursor: pointer;
  font-size: large;
}

button:hover {
  background-color: #fff;
  transition: all .5s;
  color: #000;
}
textarea {
  height: 150px;
  resize: none;
}
</style>