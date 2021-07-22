import axiosInstance from './DAL_API';
import {writeTwoTokens, writeUserId, writeUserLogin} from "../LocalDAL/LocalDAL.api"

//Requests
export const getTokens = (login, password) => {
    return axiosInstance.post('auth', {login,password});
}

export const sendRegisterData = (login, password, email) => {
    return axiosInstance.post("register", {login,password,email});
}

export const refreshToken = (refreshToken) => {
    return axiosInstance.post("refresh-token", {refreshToken});
}

//Handlers
export const getTokensHandler = (login, password, setIsAuthRedux, history) => {
    getTokens(login, password).then(response => {
        let accessToken = response.data.accessToken
        let refreshToken = response.data.refreshToken

        writeUserId(response.data.id)
        writeUserLogin(response.data.username)
        writeTwoTokens(accessToken, refreshToken)
        setIsAuthRedux(true)
        history.push('/user')
    }).catch(e => console.log("AN ERROR OCCURED!",e))
}