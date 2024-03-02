import Axios from "@/services/caller.service";

let get = (reviewId) => {
    return Axios.get('/api/v1/reviews/' + reviewId)
}

let del = (reviewId) => {
    return Axios.delete('/api/v1/reviews/delete/' + reviewId)
}

export const reviewService = {
    get,
    del
}