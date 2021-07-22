import React, {useState} from "react"
import {sendRegisterData} from "../../DAL/ServerDAL/auth-api"
import {useHistory} from "react-router-dom";

const Register = (props) => {
    const [password, setPassword] = useState("")
    const [email, setEmail] = useState("")
    const [login, setLogin] = useState("")
    const history = useHistory();

    const sendDataToServer = (e) => {
        e.preventDefault()
        sendRegisterData(login, password, email).then(response => {
            console.log(response);
            history.push('/login')
        }).catch(e => console.log(e.response.status))

    }

    const changeLoginString = (e) => {
        setLogin(e.target.value)
    }

    const changePasswordString = (e) => {
        setPassword(e.target.value)
    }

    const changeEmailString = (e) => {
        setEmail(e.target.value)
    }

    return (
        <div className={'register_page'}>
            <div className="register_title">Register</div>
            <form className={"register_form"} onSubmit={sendDataToServer}>
                <input type={'email'} placeholder={'email'} value={email} onChange={changeEmailString}/>
                <input placeholder={'login'} value={login} onChange={changeLoginString}/>
                <input type={'password'} placeholder={'password'} value={password} onChange={changePasswordString}/>
                <button>Register</button>
            </form>
        </div>
    )
}

export default Register