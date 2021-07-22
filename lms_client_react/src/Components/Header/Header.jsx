import {connect} from "react-redux";
import {setIsAuth} from "../../Redux/Reducers/auth-reducer";
import {NavLink, useHistory} from "react-router-dom";
import {localGetIsAuth, readUserLogin} from "../../DAL/LocalDAL/LocalDAL.api";

const Header = (props) => {
    const history = useHistory();
    const logoutClickHandler = () => {
        localStorage.clear();
        props.setIsAuth(false);
        history.push("/");
    }
    return (
        <>
            <div className={'header'}>
                <div className="header_wrapper">
                    <div className="left_section">
                        <NavLink to={"/"}>
                            <img src="https://image.flaticon.com/icons/png/512/5093/5093448.png" className="logo"/>
                        </NavLink>
                        <nav className={'header_nav'}>
                            <NavLink to={"/user"} className="el_link">Account</NavLink>
                            <NavLink to={"/courses"} className="el_link">Courses</NavLink>
                            <NavLink to={"/materials"} className="el_link">Materials</NavLink>
                        </nav>
                    </div>
                    <div className="right_section">
                        {props.isAuth
                            ?
                            <>
                                <div className={'login'}>{readUserLogin()}</div>
                                <img className={'exit_image'}
                                     src="https://image.flaticon.com/icons/png/512/3580/3580154.png"
                                     onClick={logoutClickHandler}/>
                            </>
                            :
                            <>
                                <NavLink to={"/register"} className="el_link">Register</NavLink>
                                <NavLink to={"/login"} className="el_link">Login</NavLink>
                            </>

                        }
                    </div>

                </div>

            </div>
        </>
    );
}

const mapStateToProps = (state) => ({
    isAuth: state.auth.isAuth,
});

export default connect(mapStateToProps, {
    setIsAuth
})(Header);