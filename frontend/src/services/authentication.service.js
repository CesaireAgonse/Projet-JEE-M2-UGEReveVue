import Axios from "@/services/caller.service";
let signup = (credentials) => {
    return Axios.post('/api/v1/signup', credentials)
}
let login = (credentials) => {
    return Axios.post('/api/v1/login', credentials)
}

let logout = () => {
    return Axios.post('/api/v1/logout')
}

let addToken = (name, token) => {
    localStorage.setItem(name, token)
}

let removeToken = () => {
    localStorage.removeItem('bearer')
}

let getToken = (name) => {
    return localStorage.getItem(name)
}

let isLogged = () => {
    let token = localStorage.getItem('bearer')
    return !!token
}

let getTokenInformation = () => {
    if (isLogged()) {
        return 0
    }
}

export const authenticationService = {
    signup,
    login,
    logout,
    addToken,
    removeToken,
    getToken,
    isLogged,
    getTokenInformation
}


