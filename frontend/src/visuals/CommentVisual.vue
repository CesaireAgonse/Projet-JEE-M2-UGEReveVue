<template>
  <div class="post">
    <div class="post-header">
      <div class="post-header-info">
        <img class="post-author-avatar" src="../assets/profile.jpg" alt="Author Avatar" />
        <div class="post-author-info">
          <h2>{{ comment.userInformation.username }}</h2>
        </div>
      </div>
      <p class="post-date">{{ comment.date }}</p>
      <div class="post-actions"></div>
    </div>
    <p>{{comment.content}}</p>
  </div>
</template>

<script>
import { library, dom } from "@fortawesome/fontawesome-svg-core";
import { fas } from '@fortawesome/free-solid-svg-icons'
import { fab } from '@fortawesome/free-brands-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';

library.add(fas, far, fab)
dom.watch();
export default {
  props: {
    comment: {
      type: Object,
      required: true
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

</style>
