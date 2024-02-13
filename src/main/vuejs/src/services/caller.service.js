import axios from "axios";
import {authenticationService} from '@/services/authentication.service'

const Axios = axios.create()

Axios.interceptors.request.use(request => {
    if (authenticationService.isLogged()){
        request.headers.Authorization = 'Bearer ' + authenticationService.getToken()
    }
    return request;
})

export default Axios