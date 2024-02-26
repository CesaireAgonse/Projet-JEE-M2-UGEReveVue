import Axios from "@/services/caller.service";

let vote = (postId, vote) => {
    return Axios.post('/api/v1/posts/vote/' + postId + '?voteType=' + vote)
}

let comment = (postId, content) => {
    return Axios.post('/api/v1/posts/comment/' + postId, content)
}

let review = (postId, content) => {
    return Axios.post('/api/v1/posts/review/' + postId, content)
}

export const postService = {
    vote,
    comment,
    review
}


