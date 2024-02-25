import Axios from "@/services/caller.service";

let get = (reviewId) => {
    return Axios.get('/api/v1/reviews/' + reviewId)
}

export const reviewService = {
    get
}