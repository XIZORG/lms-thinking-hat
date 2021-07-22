import axiosInstance from './DAL_API';
import {readAccessToken} from "../LocalDAL/LocalDAL.api";

export const getUserDataById = (id_path, token = readAccessToken()) => {
    return axiosInstance.get(`api/users/${id_path}`, { headers: {"Authorization" : `Bearer ${token}`} });
}
export const subscribeToCourse = (courseId,token = readAccessToken()) => {
    return axiosInstance.post(`api/users/courses`, courseId, { headers: {"Authorization" : `Bearer ${token}`, "Content-Type": "application/json"} });
}
