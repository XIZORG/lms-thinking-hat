export const writeAccessToken = (token) => {
    localStorage.setItem('accessToken', token);
}

export const writeRefreshToken = (token) => {
    localStorage.setItem('refreshToken', token);
}

export const readAccessToken = () => {
    return localStorage.getItem('accessToken');
}

export const readRefreshToken = () => {
    return localStorage.getItem('refreshToken');
}

export const writeTwoTokens = (accessToken, refreshToken) => {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
}

export const writeUserId = (id) => {
    localStorage.setItem('userId', id);
}

export const readUserId = () => {
    return localStorage.getItem('userId');
}

export const writeUserLogin = (login) => {
    localStorage.setItem('userLogin', login);
}

export const readUserLogin = () => {
    return localStorage.getItem('userLogin');
}

export const localGetIsAuth = () => {
    return localStorage.getItem('isAuth');
}
export const localSetIsAuth = (value) => {
    localStorage.setItem('isAuth', value);
}