import React from "react"
import {getTokensHandler} from "../../DAL/ServerDAL/auth-api"
import {setIsAuth} from "../../Redux/Reducers/auth-reducer"
import {connect} from "react-redux"
import {useHistory} from "react-router-dom"
import {useFormik} from "formik"
import {formikSubmit} from "../../Utils/utils"

const LoginForm = (props) => {
    const history = useHistory()
    const formik = useFormik({
        initialValues: {
            login: "",
            password: ""
        },
        onSubmit: values => {
            getTokensHandler(values.login, values.password, props.setIsAuth, history)
            console.log(values)
        }
    })

    return (
        <div className={'login_page'}>
            <div className={'login_title'}>Login</div>
            <form className={"login_form"} onSubmit={formikSubmit(formik.handleSubmit)}>
                <input className={'login_input'} name={"login"}
                       value={formik.values.login}
                       onChange={formik.handleChange}
                />
                <input className={'password_input'} name={"password"}
                       value={formik.values.password} type={'password'}
                       onChange={formik.handleChange}
                />
                <button className={'btn_login'}>Login</button>
            </form>
        </div>
    )
}

const mapStateToProps = (state) => ({
    isAuth: state.auth.isAuth
})

export default connect(mapStateToProps, {
    setIsAuth
})(LoginForm)