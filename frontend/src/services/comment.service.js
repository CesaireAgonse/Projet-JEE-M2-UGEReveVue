import Axios from "@/services/caller.service";

let del = (reviewId) => {
    return Axios.delete('/api/v1/comments/' + reviewId)
}

export const commentService = {
    del
}