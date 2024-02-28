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

let comments = (postId, pageNumber) => {
    return Axios.get('/api/v1/posts/comments/' + postId + "/?pageNumber=" + pageNumber)
}

let reviews = (postId, pageNumber) => {
    return Axios.get('/api/v1/posts/reviews/' + postId + "/?pageNumber=" + pageNumber)
}



export const postService = {
    vote,
    comment,
    review,
    comments,
    reviews
}


