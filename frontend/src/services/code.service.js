import Axios from "@/services/caller.service";
let create = (code) => {
    return Axios.post('api/v1/codes/create', code, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

let get = (codeId) => {
    return Axios.get('/api/v1/codes/' + codeId)
}

let filter = () => {
    return Axios.get('/api/v1/codes/filter')
}

export const codeService = {
    create,
    filter,
    get
}


