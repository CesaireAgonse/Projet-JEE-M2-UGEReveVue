<template>
  <div @click="userPage()">
    <div class="post-header">
      <div class="post-header-info">
        <img v-if="photo == null" class="post-author-avatar" src="../assets/profile.jpg" alt="Author Avatar" />
        <img v-if="photo != null" class="post-author-avatar" :src="photo" alt="Author Avatar" />
        <div class="post-author-info">
          <h2>{{ user.username }}</h2>
          <p>
            Followed: {{ user.nbFollowed }} Codes: {{ user.nbCode }} Reviews: {{ user.nbReview }} Comments: {{ user.nbComments }}
          </p>
        </div>
      </div>
    </div>
  </div>

</template>

<script>

export default {
  name: 'UserVisual',
  props: {
    user: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      photo: null
    }
  },
  methods: {
    userPage() {
      this.$router.push('/profile/' + this.user.username)
    }
  },
  mounted(){
    if (this.user.profilePhoto != null){
      this.photo = "data:image/jpg;base64," + this.user.profilePhoto
    }
  }
}
</script>

<style scoped>

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

.post-author-avatar {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  margin-right: 20px;
}

</style>