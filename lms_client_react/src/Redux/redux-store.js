import {applyMiddleware, combineReducers, createStore} from "redux";
import {authReducer} from "./Reducers/auth-reducer";
import {userReducer} from "./Reducers/user-reducer";
import thunkMiddleware from 'redux-thunk';
import {materialsReducer} from "./Reducers/materials-reducer";
import {courseReducer} from "./Reducers/course-reducer";

let reducers = combineReducers({
    auth: authReducer,
    materials: materialsReducer,
    user: userReducer,
    course: courseReducer
});

export const store = createStore(reducers, applyMiddleware(thunkMiddleware));