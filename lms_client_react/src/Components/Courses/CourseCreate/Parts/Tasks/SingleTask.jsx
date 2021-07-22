import * as React from "react"

const SingleTask = ({task, changeTaskByIndex, index, removeTaskByIndex}) => {
    console.log('Task rendered!')

    const inputHandler = (inputName, value) => {
        changeTaskByIndex(index, inputName, value)
    }

    const deleteButtonHandler = (e) => {
        e.preventDefault();
        removeTaskByIndex(index)
    }

    return (
        <div className={"task"}>
            <div className="task_title">
                Task {index+1}
            </div>
            <button className={'btn_delete_task'} onClick={deleteButtonHandler}>

            </button>
            <div className="input_part">
                <div className="input_desc">Question</div>
                <input className={(task.name ? 'correct' : "")} type="text"
                       onChange={e => inputHandler('name', e.target.value)}
                       value={task.name}/>
            </div>
            <div className="input_part">
                <div className="input_desc">Description</div>
                <textarea className={(task.description ? 'correct' : "")}
                          onChange={e => inputHandler('description', e.target.value)}
                          value={task.description}/>
            </div>
            <div className="input_part">
                <div className="input_desc">Correct answers</div>
                <textarea className={(task.choices ? 'correct' : "")}
                          onChange={e => inputHandler('choices', e.target.value)} value={task.choices}/>
            </div>
        </div>
    )
}
export default React.memo(SingleTask, (prevProps, nextProps) => {
    let prevTask = prevProps.task, nextTask = nextProps.task
    if (prevTask.name === nextTask.name && prevTask.description === nextTask.description && prevTask.choices === nextTask.choices) {
        return true;
    }
    return false;
})
