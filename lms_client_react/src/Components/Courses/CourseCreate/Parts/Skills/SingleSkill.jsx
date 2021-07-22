import {useState} from "react"

export const SingleSkill = (props) => {

    const [selected, setSelected] = useState(props.selected)
    const [skillLevel, setSkillLevel] = useState(1)

    console.log("SELECTED SKILLS", props.selectedSkills.current)

    const changeSelected = () => {
        setSelected(!selected)
        props.toggleSkill({name: props.skillName, level: +skillLevel})
    }

    const changeSkillLevel = (e) => {
        let value = +e.target.value
        if (value > 10) setSkillLevel(10)
        else if (value < 1) setSkillLevel(1)
        else setSkillLevel(value)

        props.changeSkillLevel(props.skillName, value)
    }
    console.log("Rendered!")
    return (
        <div className={'skill' + (selected ? " selected" : "")}
             onClick={changeSelected}>
            <img src={props.skillImageUrl} alt={props.skillName} className="skill_image"/>
            <div className="skill_name">
                {props.skillName}
            </div>
            <input type="number" value={skillLevel} placeholder={'Skill level'}
                   className="skill_level_input focus_disable" onClick={e => e.stopPropagation()}
                   onChange={changeSkillLevel}/>
        </div>
    )
}