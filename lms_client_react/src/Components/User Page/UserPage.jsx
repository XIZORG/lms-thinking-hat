import {getUserDataById} from "../../DAL/ServerDAL/user-api"
import {useEffect, useState} from "react"
import {readUserId} from "../../DAL/LocalDAL/LocalDAL.api"
import {useHistory} from "react-router"
import {getPassedCoursesData} from "../../DAL/ServerDAL/course-api"
import dayjs from "dayjs"


const UserPage = () => {
    const [userData, setUserData] = useState({courses: [], availableSkills: [], materials: [], passedCourses: []})
    const history = useHistory()

    const getAllMaterialsLocal = async () => {
        let data
        try {
            data = (await getUserDataById(+readUserId())).data
            data.passedCourses = await getPassedCoursesData()
            setUserData(data)
        } catch (e) {
            console.log(e.message)
        }
    }

    useEffect(getAllMaterialsLocal, [])

    const courseClickHandler = (course,courseStartDate) => {
        let dateSubtract = new Date(courseStartDate) - new Date()
        console.log(dateSubtract)
        if(dateSubtract <= 0)
            history.push(`/course/${course}`)
        else {
            alert('Course is not started yet!');
        }
    }

    console.log("User data: ", userData)

    return (
        <div className={"user_page"}>
            <div className="left_side">
                <img className="avatar"
                     src={'data:image/png;base64,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'}/>

                <div className="login">{userData.login}</div>
                <div className={"user_data"}>
                    <div className="full_name">{userData.fullName}</div>
                    <div className="age">{userData.age}</div>
                    <div className="email">{userData.email}</div>
                    <div className="phone">{userData.phone}</div>
                </div>
            </div>
            <div className="right_side">
                <div className="row">
                    <div className="materials">
                        {userData.materials.map(material =>
                            <div className="material">
                                <div className="name">{material}</div>
                            </div>
                        )}
                    </div>
                    <div className="skills">
                        <div className="skills_wrapper">
                            {userData.availableSkills.map(skill =>
                                <div className="skill">
                                    <img src={skill.image} alt=""
                                         className={'skill_img'}/>
                                    <div className={'skill_text'}>{skill.name}</div>
                                    <div className="skill_level">{skill.level}</div>
                                </div>
                            )}
                        </div>

                    </div>
                </div>
                <div className="courses">
                    {userData.courses.map(course => {
                            return <div className="course">
                                <div className="name" onClick={() => courseClickHandler(course.name,course.startDate)}>{course.name}</div>
                            </div>
                        }
                    )}
                </div>
                <div className="passed_courses">
                    <div className="title">
                        Passed Courses
                    </div>
                    <div className="cols_names">
                        <div className="first_col">
                            Course name
                        </div>
                        <div className="second_col">
                            Course status
                        </div>
                    </div>
                    <div className="courses_wrapper">
                        {userData.passedCourses.map(course => {
                            return (
                                <div className="passed_course">
                                    <div className="course_name">
                                        {course.courseName}
                                    </div>
                                    <div className={"course_status" + (course.result === "FAILED" ? " failed" : "")} >
                                        {course.result === "FAILED" ? "Failed" : "Success"}
                                    </div>
                                </div>
                            )
                        })}

                    </div>
                </div>
            </div>
        </div>
    )
}
export default UserPage
//courseName result userName