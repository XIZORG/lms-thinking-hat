import {useEffect, useRef, useState} from "react"
import CourseSection from "./Parts/CourseSection"
import MaterialsSection from "./Parts/Materials/MaterialsSection"
import SkillsSection from "./Parts/Skills/SkillsSection"
import {
    getAllMaterials,
    getAllSkills, sendCreateCourseDataToServer
} from "../../../DAL/ServerDAL/course-api"
import TaskSection from "./Parts/Tasks/TaskSection"
import {useHistory} from "react-router-dom"
import {useFormik} from "formik"

export const CourseCreate = () => {

    const history = useHistory()

    useEffect(async () => {
        let materials = await getAllMaterials()
        setAllMaterials(materials.data)
        let skills = await getAllSkills()
        setAllSkills(skills.data)
    }, [])

    //States
    const [allMaterials, setAllMaterials] = useState([])
    const [allSkills, setAllSkills] = useState([])
    const [allTasks, setAllTasks] = useState([{name: "", description: "", choices: ""}])
    const formikCourse = useFormik({
        initialValues: {
            name: "",
            description: "",
            passingScore: "",
            endDate: "",
            startDate: ""
        },
        onSubmit: values => {
            console.log(values)
        },
    })
    //Refs
    const selectedMaterialsIds = useRef([])
    const selectedSkills = useRef([])
    //Mock data
    // const testMaterials = useRef(new Array(9).fill(null).map((_, index) => ({
    //     name: "rewjigw",
    //     id: index,
    //     selected: false,
    //     creator: "Ya"
    // })))
    // const testSkills = useRef(new Array(11).fill(null).map((_, index) => ({
    //     name: "Power!!!"
    // })))

    //Functions
    const toggleSkill = useRef((skillObj) => {
        if (selectedSkills.current.find(skill => skill.name === skillObj.name) === undefined) {
            selectedSkills.current.push({...skillObj})
        } else {
            selectedSkills.current = selectedSkills.current.filter(skill => skill.name !== skillObj.name)
        }
    })

    const changeSkillLevel = useRef((skillName, level) => {
        let skill = selectedSkills.current.find(skill => skill.name === skillName)
        if (skill) {
            skill.level = level
        }
    })

    const changeTaskByIndex = (index, inputName, value) => {
        setAllTasks(prev => {
            let newTasks = [...prev]
            newTasks[index] = {...newTasks[index], [inputName]: value}
            return newTasks
        })
    }

    const removeTaskByIndex = (index) => {
        console.log(index)
        setAllTasks(prev => {

            let newTasks = [...prev];
            newTasks.splice(index, 1);
            return newTasks;
        })
    }

    const addTask = () => {
        setAllTasks([...allTasks, {name: "", description: "", choices: ""}])
    }

    const submitCourseForm = async (e) => {
        e.preventDefault()
        let success = await sendCreateCourseDataToServer(
            formikCourse.values,
            selectedMaterialsIds.current,
            selectedSkills.current,
            allTasks
        )
        if (success) history.push('/courses')
    }

    return (
        <form className={'create_course_page'} onSubmit={submitCourseForm}>
            {/*<CourseSection courseInputData={courseInputData} setCourseInputData={setCourseInputData}/>*/}
            <CourseSection handleChange={formikCourse.handleChange} values={formikCourse.values}/>
            <MaterialsSection selectedMaterialsIds={selectedMaterialsIds} materials={allMaterials}/>
            <SkillsSection toggleSkill={toggleSkill.current} skills={allSkills}
                           selectedSkills={selectedSkills.current} changeSkillLevel={changeSkillLevel.current}/>

            <TaskSection changeTaskByIndex={changeTaskByIndex} tasks={allTasks} addTask={addTask}
                         removeTaskByIndex={removeTaskByIndex}
            />

            <button className={'btn_submit'}>Submit</button>
        </form>
    )
}