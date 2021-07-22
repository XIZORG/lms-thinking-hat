import {localGetIsAuth, localSetIsAuth} from "../../DAL/LocalDAL/LocalDAL.api"

const CHANGE_AUTH = "CHANGE_AUTH";

let initialState = {
    isAuth: localGetIsAuth() === "true"
}

export const authReducer = (state = initialState, action) => {
    switch(action.type) {
        case CHANGE_AUTH: {
            localSetIsAuth(action.isAuth);
            return {...state, isAuth: action.isAuth}
        }
    }
    return state;
}

export const setIsAuth = (isAuth) => {
    return {type: CHANGE_AUTH, isAuth}
}