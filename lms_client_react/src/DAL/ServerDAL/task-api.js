import {readAccessToken} from "../LocalDAL/LocalDAL.api"
import axiosInstance from "./DAL_API"

//Request
export const performTask = (task, token = readAccessToken()) => {
    return axiosInstance.post(`api/tasks/${task.id}`, {answer: task.answer}, {headers: {"Authorization": `Bearer ${token}`}})
}

//Handler
export const performTaskChain = async (tasks) => {
    try {
        await Promise.all(tasks.map(task => performTask(task)))
        return true;
    } catch(e) {
        console.log(e)
        return false;
    }
}