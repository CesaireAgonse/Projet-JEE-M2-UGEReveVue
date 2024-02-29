import axios from "axios";
import {authenticationService} from '@/services/authentication.service'

const Axios = axios.create()

Axios.interceptors.request.use(request => {
    if (authenticationService.isLogged()){
        request.headers.Authorization = 'Bearer ' + authenticationService.getToken('bearer')
    }
    return request;
})

let refresh = false;
let refreshSubscribers = [];
Axios.interceptors.response.use(response => response, async error => {
    const { config, response: { status } } = error;
    const originalRequest = config;
    if ((status === 401 || status === 403) && !refresh) {
        refresh = true;
        authenticationService.removeToken('bearer')
        await Axios.post("api/v1/refresh", { refresh: localStorage.getItem("refresh")}).then(res =>
        {
            if (res.status === 200){
                authenticationService.addToken("bearer", res.data.bearer)
                authenticationService.addToken("refresh", res.data.refresh)
                originalRequest.headers.Authorization = 'Bearer ' + res.data.bearer;

                refreshSubscribers.forEach(callback => callback(res.data.bearer));
                refreshSubscribers = [];
                window.location.reload()
                return Axios(originalRequest)
            }
        });
    }
    refresh = false;
    return error;
});



export default Axios