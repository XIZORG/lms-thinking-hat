import * as axios from 'axios';
const axiosInstance = axios.create({
    baseURL: "http://31.133.81.55:8080/"
});
export default axiosInstance;


