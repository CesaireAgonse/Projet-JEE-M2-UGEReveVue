import Axios from "@/services/caller.service";
let vote = (postId, vote) => {
    return Axios.post('api/v1/posts/vote/' + postId + '?voteType=' + vote)
}
export const postService = {
    vote,
}


