import './Assets/Styles/styles.css'
import {Route, Switch} from "react-router"
import {MainPage} from "./Components/Main Page/MainPage"
import React, {useEffect} from "react"
import LoginForm from "./Components/Auth/LoginForm"
import {
    readAccessToken,
    readRefreshToken,
    writeAccessToken,
    writeRefreshToken,
    localSetIsAuth, localGetIsAuth
} from "./DAL/LocalDAL/LocalDAL.api"
import RegisterForm from "./Components/Auth/RegisterForm"
import Materials from "./Components/Materials/Materials"
import MaterialCreate from "./Components/Materials/MaterialCreate"
import {refreshToken} from "./DAL/ServerDAL/auth-api"
import {parseJwt} from "./Utils/utils"
import UserPage from "./Components/User Page/UserPage"
import Header from "./Components/Header/Header"
import {Forbidden404} from "./Components/Utils Components/Forbidden404"
import AllCourses from "./Components/Courses/CoursesPage/AllCourses";
import {CourseCreate} from "./Components/Courses/CourseCreate/CourseCreate";
import {CoursePage} from "./Components/Courses/CoursePage/CoursePage"

function App() {
    useEffect(() => {
        let accessToken = readAccessToken()
        let refToken = readRefreshToken()

        let payload
        try {
            payload = parseJwt(accessToken)
        } catch (e) {

            console.log("please sign in!")
            localSetIsAuth("false");
            return
        }

        const expiration = new Date(payload.exp)
        const now = new Date()
        const fiveMinutes = 1000 * 60 * 5

        if (expiration.getTime() - now.getTime() < fiveMinutes) {
            refreshToken(refToken).then(response => {
                writeAccessToken(response.data.accessToken)
                writeRefreshToken(response.data.refreshToken)
                localSetIsAuth("true")
            }).catch(e => {
                localSetIsAuth("false")
            })
        } else {
            localSetIsAuth("true")
        }
        localSetIsAuth(true);
    }, [])

    return (
        <>
            <Header/>
            <div className={'container'}>

                <Switch>
                    <Route path={"/"} exact render={() => <MainPage/>}/>

                    <Route path={"/login"} exact
                           render={() => localGetIsAuth() !== "true" ? <LoginForm/> : <MainPage/>}/>
                    <Route path={"/register"} exact
                           render={() => localGetIsAuth() !== "true" ? <RegisterForm/> : <MainPage/>}/>

                    <Route path={"/user"} exact
                           render={() => localGetIsAuth() === "true" ? <UserPage/> : <LoginForm/>}/>
                    <Route path={"/materials"} exact
                           render={() => localGetIsAuth() === "true" ? <Materials/> : <LoginForm/>}/>
                    <Route path={"/courses"} exact
                           render={() => localGetIsAuth() === "true" ? <AllCourses/> : <LoginForm/>}/>
                    <Route path={"/course/create"} exact
                           render={() => localGetIsAuth() === "true" ? <CourseCreate/> : <LoginForm/>}/>
                    <Route path={"/course"}
                           render={() => localGetIsAuth() === "true" ? <CoursePage/> : <LoginForm/>}/>

                    <Route path={"/material/create"} exact
                           render={() => localGetIsAuth() === "true" ? <MaterialCreate/> : <LoginForm/>}/>

                    <Route path={"*"} component={Forbidden404}/>
                </Switch>

            </div>
        </>
    )
}

export default App