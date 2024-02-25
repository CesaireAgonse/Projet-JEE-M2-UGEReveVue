<template>
  <div class="row">
    <ReviewVisual :post="post" v-if="post != null"></ReviewVisual>
    <div class="comments" v-if="post != null">
      <p v-for="comment in post.comments" :key="comment">
        <CommentVisual  :comment="comment"></CommentVisual>
      </p>
    </div>
  </div>
  <div class="reviews" v-if="post != null">
    <p v-for="review in post.reviews" :key="review">
      <ReviewVisual  :post="review"></ReviewVisual>
    </p>
  </div>
</template>

<script>
import router from "@/router";
import CommentVisual from "@/visuals/CommentVisual.vue";
import ReviewVisual from "@/visuals/ReviewVisual.vue";
import { reviewService } from "@/services/review.service";

export default {
  components: { CommentVisual, ReviewVisual},
  mounted() {
    document.title = "Review"
    this.review()
  },
  data() {
    return {
      post: null
    }
  },
  methods : {
    review(){
      reviewService.get(this.$route.params.id).then(res => {
        this.post = res.data
        console.log(this.post)
      })
    },
    home(){
      router.push("/")
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
  max-width: calc(33.3333% - 40px);
}

.reviews {
  background-color: #1e1e1e;
  border-radius: 10px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2); /* Ombre tout autour */
  max-width: 100%;
}

.row {
  display: flex;
  flex-direction: row;
}


</style>
