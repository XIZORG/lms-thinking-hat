import {readAccessToken} from "../LocalDAL/LocalDAL.api"
import axiosInstance from "./DAL_API"
import {performTaskChain} from "./task-api"

//Requests
export const getAllCourses = (token = readAccessToken()) => {
    return axiosInstance.get('api/courses', {headers: {"Authorization": `Bearer ${token}`}})
}

export const getAllMaterials = (token = readAccessToken()) => {
    return axiosInstance.get(`api/material`, {headers: {"Authorization": `Bearer ${token}`}})
}
export const getAllSkills = (token = readAccessToken()) => {
    return axiosInstance.get(`api/skill`, {headers: {"Authorization": `Bearer ${token}`}})
}

export const createNewCourse = ({description, endDate, name, passingScore, startDate}, token = readAccessToken()) => {
    return axiosInstance.post(`api/courses`, {
        description,
        endDate,
        name,
        passingScore,
        startDate
    }, {headers: {"Authorization": `Bearer ${token}`}})
}

export const addMaterialsToCourse = (courseId, materialId, token = readAccessToken()) => {
    return axiosInstance.post(`api/courses/${courseId}/material`, {materialId}, {headers: {"Authorization": `Bearer ${token}`}})
}

export const addSkillsToCourse = (courseId, skills, token = readAccessToken()) => {
    return axiosInstance.post(`api/courses/${courseId}/skill`, skills, {headers: {"Authorization": `Bearer ${token}`}})
}

export const addTasksToCourse = (courseId, tasks, token = readAccessToken()) => {
    return axiosInstance.post(`api/courses/${courseId}/task`, tasks, {headers: {"Authorization": `Bearer ${token}`}})
}

export const getCourse = (courseName, token = readAccessToken()) => {
    return axiosInstance.get(`api/courses/name/${courseName}`, {headers: {"Authorization": `Bearer ${token}`}})
}

export const getCourseResult = (courseId, token = readAccessToken()) => {
    return axiosInstance.get(`api/courses/${courseId}/result`, {headers: {"Authorization": `Bearer ${token}`}})
}

export const getPassedCourses = (token = readAccessToken()) => {
    return axiosInstance.get(`api/courses/results`, {headers: {"Authorization": `Bearer ${token}`}})
}

//Handlers
export const sendCreateCourseDataToServer = async (courseInputData, materialsIds, skills, tasks) => {
    console.log(courseInputData, materialsIds, skills, tasks);
    try {
        let courseToServer = {
            ...courseInputData,
            startDate: new Date(courseInputData.startDate).toISOString(),
            endDate: new Date(courseInputData.endDate).toISOString()
        }

        const {data: {id: courseId}} = await createNewCourse(courseToServer)

        await addMaterialsToCourse(courseId, materialsIds)

        await addSkillsToCourse(courseId, skills)

        let tasksToServer = tasks.map(task => ({
            ...task,
            choices: task.choices.split(',').map(choice => choice.trim())
        }))

        await addTasksToCourse(courseId, tasksToServer)

        return true

    } catch (e) {
        console.log("Failed to create course!")
        console.log(e)
        return false
    }
}

export const submitCourse = async (courseId, tasks) => {
    try {
        let tasksToServer = tasks.map(task => ({id: task.id, answer: task.answer}))
        console.log(tasksToServer)

        let performTasksResult = await performTaskChain(tasks)
        if (!performTasksResult) {
            console.log("Tasks not performed good!")
            return
        }

        let courseResult = await getCourseResult(courseId)
        console.log("Course result: ", courseResult)
    } catch (e) {
        console.log("Submit course error!")
        console.log(e)
    }
}

export const getPassedCoursesData = async () => {
    let response = await getPassedCourses();
    return response.data;
}