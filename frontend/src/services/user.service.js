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

export const userService = {
    profile,
    updatePassword,
    follow,
    unfollow,
    user
}


