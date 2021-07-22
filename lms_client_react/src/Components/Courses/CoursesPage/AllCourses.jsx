import {connect} from "react-redux"
import {useEffect} from "react"
import {getAllCourses} from "../../../DAL/ServerDAL/course-api"
import {changeAllCourses} from "../../../Redux/Reducers/course-reducer"
import Course from "./Course"
import {NavLink} from "react-router-dom"
import {subscribeToCourse} from "../../../DAL/ServerDAL/user-api"

const AllCourses = (props) => {
    useEffect(() => {
        getAllCoursesLocal()
    }, [])

    const getAllCoursesLocal = async () => {
        // let coursesMock = {
        //     data: {
        //         allCourses: [{
        //             id: 1,
        //             name: "Course Name",
        //             beginDate: "2021-07-19T11:53:08.887Z",
        //             endDate: "2021-07-19T11:53:08.887Z",
        //             creator: "Holy Father",
        //             description: "iuhfgehfuehtuesht8etehu7ethtgetuytfgewgrteawyrtgeyrgeilwrhehawer",
        //             passingScore: 54
        //         }]
        //     }
        // };
        let courses
        try {
            courses = await getAllCourses()
            if (!courses) throw new Error("COURSES ARE EMPTY!")
        } catch (e) {
            console.log(e.message)
        }
        console.log(courses.data)
        props.changeAllCourses(courses.data)
    }

    const subscribeToCourseLocal = (courseId) => {
        subscribeToCourse(courseId)
    }

    return (
        <div className={"courses_page"}>
            {props && props.allCourses && props.allCourses.map(course => {
                return <Course subscribeToCourseLocal={subscribeToCourseLocal} {...course} key={course.id}
                               id={course.id}/>
            })}
            <NavLink to={"/course/create"} className="btn_createCourse" onClick={() => window.scroll(0,0)}>
                Add Course
            </NavLink>
        </div>
    )
}
export default connect((state) => ({
    allCourses: state.course.allCourses
}), {
    changeAllCourses
})(AllCourses)