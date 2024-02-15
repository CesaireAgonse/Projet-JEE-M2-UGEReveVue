import Axios from "@/services/caller.service";
let profile = () => {
    return Axios.get('/api/v1/users/profile')
}

let updatePassword = (credentials) => {
    return Axios.post('api/v1/users/password', credentials)
}


export const userService = {
    profile,
    updatePassword
}


