import Axios from "@/services/caller.service";
let profile = () => {
    return Axios.get('/api/v1/users/profile')
}

let user = (username) => {
    return Axios.get('/api/v1/users/' + username)
}


let updatePassword = (credentials) => {
    return Axios.post('/api/v1/users/password', credentials)
}

let follow = (username) => {
    return Axios.post('/api/v1/users/follow/' + username)
}

let unfollow = (username) => {
    return Axios.post('/api/v1/users/unfollow/' + username)
}

let codes = (username, numberPage) => {
    return Axios.get('/api/v1/users/codes/' + username + "?pageNumber=" + numberPage)
}

let reviews = (username, numberPage) => {
    return Axios.get('/api/v1/users/reviews/' + username + "?pageNumber=" + numberPage)
}

let comments = (username, numberPage) => {
    return Axios.get('/api/v1/users/comments/' + username + "?pageNumber=" + numberPage)
}

let followed = (username, numberPage) => {
    return Axios.get('/api/v1/users/followed/' + username + "?pageNumber=" + numberPage)
}

let photo = (photo) => {
    return Axios.post('/api/v1/users/photo', photo)
}

let reviewsContents = (username, numberPage) => {
    return Axios.get("/api/v1/users/reviews/contents/" + username + "?pageNumber=" + numberPage)
}

export const userService = {
    profile,
    updatePassword,
    follow,
    unfollow,
    user,
    codes,
    reviews,
    comments,
    followed,
    photo,
    reviewsContents
}


