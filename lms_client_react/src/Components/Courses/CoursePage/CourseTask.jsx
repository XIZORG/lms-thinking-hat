import React from 'react'

const CourseTask = ({name, description, answer, id, changeTaskAnswerById}) => {

    const changeTaskAnswerHandler = (e) => {
        changeTaskAnswerById(id, e.target.value);
    }

    return (
        <div className={'task'}>
            <div className="task_name">
                {name}
            </div>
            <div className="task_desc">
                {description}
            </div>
            <div className={'task_answer_title'}>Your answer:</div>
            <input type="text" className={'task_answer'} value={answer} onChange={changeTaskAnswerHandler}/>
        </div>
    )
}
export default React.memo(CourseTask)