const CourseSection = ({handleChange, values}) => {
    /*
                name: "",
                description: "",
                passingScore: "",
                endDate: "",
                startDate: ""
     */
    return (
        <section className="course">
            <div className="title">
                Create Course
            </div>
            <div className={'course_create_form'}>
                <div className="input_part">
                    <div className="input_desc">Enter course name</div>
                    <input name={'name'}
                           value={values.name}
                           onChange={handleChange}
                           type="text" placeholder={"My awesome course"}/>
                </div>
                <div className="input_part">
                    <div className="input_desc">Enter course description</div>
                    <textarea name={'description'}
                              value={values.description}
                              onChange={handleChange}
                              placeholder={"Description"}/>
                </div>
                <div className="input_part">
                    <div className="input_desc">Enter passing score</div>
                    <input name={'passingScore'}
                           value={values.passingScore}
                           onChange={handleChange}
                           type="number"/>
                </div>
                <div className="input_part">
                    <div className="input_desc">Enter course duration</div>
                    <div className="dates_inputs_wrapper">
                        <div className="dates_el">
                            Start date:
                            <input type="date" name={'startDate'}
                                   value={values.startDate}
                                   onChange={handleChange}/>
                        </div>
                        <div className="dates_el">
                            End date:
                            <input type="date" name={'endDate'}
                                   value={values.endDate}
                                   onChange={handleChange}/>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    )
}
export default CourseSection