import axios from "axios";
import {authenticationService} from '@/services/authentication.service'

const Axios = axios.create()

Axios.interceptors.request.use(request => {
    if (authenticationService.isLogged()){
        request.headers.Authorization = 'Bearer ' + authenticationService.getToken('bearer')
    }
    return request;
})

Axios.interceptors.response.use(response => response, async error =>{
    if(error.response.status === 401){
        const {bearer, refresh} = Axios.post("/refresh", localStorage.getItem("refresh"))
        localStorage.removeItem('bearer')
        localStorage.removeItem('refresh')
        localStorage.setItem('bearer', bearer)
        localStorage.setItem('refresh', refresh)
        console.log("refresh")
    }
    return error;
})



export default Axios