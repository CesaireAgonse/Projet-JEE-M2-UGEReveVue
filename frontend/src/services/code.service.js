import Axios from "@/services/caller.service";
let create = (code) => {
    return Axios.post('api/v1/', code)
}

export const userService = {
    create,
}


