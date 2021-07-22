const USER_WRITE_DATA = "USER_WRITE_DATA";
const USER_SET_LOGIN = "USER_SET_LOGIN";

let initialState = {
    courses: [],
    availableSkills: [],
    materials: [],
    userInfo: {
        email: "",
        login: ""
    }
}

export const userReducer = (state = initialState, action) => {
    switch (action.type) {
        case USER_WRITE_DATA: {
            return {...state, ...action.userData}
        }
        case USER_SET_LOGIN: {
            return {...state, userInfo: {...state.userInfo, login: action.login}}
        }
    }
    return state;
}

export const userWriteData = userData => {
    return {type: USER_WRITE_DATA, userData}
}

export const userSetLogin = login => {
    return {type: USER_SET_LOGIN, login}
}