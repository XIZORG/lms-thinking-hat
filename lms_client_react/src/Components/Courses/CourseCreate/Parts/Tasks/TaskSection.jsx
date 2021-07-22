import SingleTask from "./SingleTask"

const TaskSection = (props) => {

    return (
        <section className={"tasks"}>
            <div className="title">Add Tasks</div>
            <div className="tasks_wrapper">
                {props.tasks.map((task, i) => <SingleTask key={i} index={i} changeTaskByIndex={props.changeTaskByIndex}
                                                          task={task} removeTaskByIndex={props.removeTaskByIndex}/>)}
            </div>
            <button className={'btn_add_task'} onClick={(e) => {
                e.preventDefault();
                props.addTask();
            }}>Add new task</button>
        </section>
    )
}
export default TaskSection
