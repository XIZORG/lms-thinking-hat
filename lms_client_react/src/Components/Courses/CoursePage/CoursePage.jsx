import {useEffect, useRef, useState} from "react"
import {useHistory} from "react-router"
import {getCourse, submitCourse} from "../../../DAL/ServerDAL/course-api"
import CourseMaterial from "./CourseMaterial"
import CourseTask from "./CourseTask"

export const CoursePage = () => {
    const history = useHistory()
    const [courseData, setCourseData] = useState({materials: []})
    const [materialsData, setMaterialsData] = useState([])
    const [tasksData, setTasksData] = useState([])
    const [hiddenSections, setHiddenSections] = useState({materials: true, tasks: true})

    let pathVar = history.location.pathname.split('/').slice(-1);

    useEffect(async () => {
        let courseData = (await getCourse(pathVar[pathVar.length - 1])).data
        courseData.materials = courseData.materials.map(material => {
            material.status = false
            return material
        })
        courseData.tasks = courseData.tasks.map(task => {
            task.answer = ""
            return task
        })
        setMaterialsData(courseData.materials)
        setTasksData(courseData.tasks);
        setCourseData(courseData)
    }, [])
    // console.log(courseData)
    const toggleSection = (name) => {
        setHiddenSections(prev => ({...prev, [name]: !prev[name]}))
    }

    const toggleMaterialStatus = useRef((materialId) => {
        setMaterialsData(prev => {
            return prev.map(material => {
                if (material.id === materialId) material.status = true;
                return material;
            })
        })
    })

    const changeTaskAnswerById = useRef((taskId, answerInputValue) => {
        setTasksData(prev => {
            return prev.map(task => {
                if (task.id === taskId) task.answer = answerInputValue;
                return task;
            })
        })
    })

    const submitCourseButtonHandler = async () => {
        await submitCourse(courseData.id, tasksData);
        history.push('/user')
    }

    return (
        <div className={'course_page_wrapper'}>
            <div className={'course_page'}>
                <div className="title">{pathVar} Course Page</div>
                <section className={"materials"}>
                    <div className="section_header">
                        <div className="title">Materials</div>
                        <button className={'btn_hide_section' + (hiddenSections.materials ? " up" : "")}
                                onClick={() => toggleSection("materials")}>
                            <img src="https://image.flaticon.com/icons/png/512/727/727245.png" alt="*"/>
                        </button>
                    </div>
                    <div className={"wrapper materials_wrapper" + (hiddenSections.materials ? " hidden" : "")}>
                        {materialsData.map(material => <CourseMaterial key={material.id} {...material}
                                                                       toggleMaterialStatus={toggleMaterialStatus.current}/>)}
                    </div>

                </section>
                <section className="tasks">
                    <div className="section_header">
                        <div className="title">Tasks</div>
                        <button className={'btn_hide_section' + (hiddenSections.tasks ? " up" : "")}
                                onClick={() => toggleSection("tasks")}>
                            <img src="https://image.flaticon.com/icons/png/512/727/727245.png" alt="*"/>
                        </button>
                    </div>
                    <div className={"wrapper tasks_wrapper" + (hiddenSections.tasks ? " hidden" : "")}>
                        {tasksData.map(task => <CourseTask {...task} changeTaskAnswerById={changeTaskAnswerById.current}/>)}
                    </div>
                </section>
            </div>
            <button className={'btn_submit_course'} onClick={submitCourseButtonHandler}>
                Submit
            </button>
        </div>


    )
}