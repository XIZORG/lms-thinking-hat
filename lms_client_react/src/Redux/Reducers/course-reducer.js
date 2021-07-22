const CHANGE_ALL_COURSES = "CHANGE_ALL_COURSES";
let initialState = {
    allCourses: []
}

export const courseReducer = (state = initialState, action) => {
    switch(action.type) {
        case CHANGE_ALL_COURSES: {
            return {...state, allCourses: action.courses}
        }
    }
    return state;
}

export const changeAllCourses = (courses) => {
    return {type: CHANGE_ALL_COURSES, courses}
}