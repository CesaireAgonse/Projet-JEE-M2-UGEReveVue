import Axios from "@/services/caller.service";
let profile = (profile) => {
    return Axios.get('/api/v1/users/' + profile)
}

export const userService = {
    profile
}


