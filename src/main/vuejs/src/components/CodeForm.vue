<template>
  <div>
    <div v-if="isCodeModalVisible" class="modal" @click="handleModalClick">
      <div class="modal-content">
        <span class="close" @click="hideCodeModal">&times;</span>
        <h1 class="label">Poster un Code</h1>
        <form @submit.prevent="post">
          <input type="text" id="title" v-model="title" placeholder="Titre" required>
          <input type="text" id="description" v-model="description" placeholder="Description" required>
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
      description: ''
    };
  },
  methods: {
    post() {

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
  backdrop-filter: blur(10px);
}

.modal-content {
  background-color: rgba(255, 255, 255, 0.1);
  margin: 15% auto;
  padding: 20px;
  border-radius: 10px;
  width: 80%;
  max-width: 600px;
  border: 2px solid #fff;
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


input {
  padding: 10px;
  margin-bottom: 30px;
  border-radius: 5px;
  border: 1px solid #fff;
  outline: none;
  background-color: rgba(200, 200, 200, 0.2);
  color: #ffffff;
  font-size: large;
  margin-right: 5%;
  margin-left: 5%;
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
</style>