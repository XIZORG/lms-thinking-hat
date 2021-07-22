import axiosInstance from './DAL_API';
import {readAccessToken} from "../LocalDAL/LocalDAL.api";

export const getAllMaterials = (token = readAccessToken()) => {
    return axiosInstance.get('api/material', { headers: {"Authorization" : `Bearer ${token}`} });
}

export const createNewMaterial = (link, name, token = readAccessToken()) => {
    return axiosInstance.post('api/material', {link,name}, { headers: {"Authorization" : `Bearer ${token}`} });
}

export const updateMaterial = (id_path, link, name, token = readAccessToken()) => {
    return axiosInstance.patch(`api/material/${id_path}`, {link,name}, { headers: {"Authorization" : `Bearer ${token}`} });
}