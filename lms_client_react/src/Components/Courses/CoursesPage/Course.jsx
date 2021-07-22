import React, {useState} from 'react'
import dayjs from "dayjs"

const Course = ({name, creator, description, startDate, id, courseStatus, subscribeToCourseLocal}) => {
    const [subscribed, setSubscribed] = useState(courseStatus === "SUBSCRIBED")

    const subscribeButtonHandler = () => {
        subscribeToCourseLocal(id)
        setSubscribed(true)
    }

    console.log(new Date(startDate))
    let trueDate = dayjs(startDate).format('YYYY.MM.DD');
    return (
        <div className={"material"}>
            <div className={'name'}>
                {name}
            </div>
            <div className="description">
                {description}
            </div>
            <div className="bottom_row">
                <div className="creator">
                    {creator}
                </div>
                <button onClick={subscribeButtonHandler} className="btn_course_subscribe" disabled={subscribed}>
                    {subscribed ? "Subscribed" : "Subscribe"}
                </button>
                <div className="date">
                    {trueDate}
                </div>
            </div>
        </div>
    )
}
export default Course