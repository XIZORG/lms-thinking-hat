import {SingleSkill} from "./SingleSkill";
import * as React from "react";

const SkillsSection = (props) => {

    return (
        <section className={'skills'}>
            <div className="title">Select Skills</div>
            <div className="skills_list">
                {props.skills.map(skill => <SingleSkill key={skill.id} toggleSkill={props.toggleSkill}
                                                        skillId={skill.id} skillName={skill.name}
                                                        skillImageUrl={skill.imageUrl}
                                                        selectedSkills={props.selectedSkills}
                                                        changeSkillLevel={props.changeSkillLevel}
                />)}
            </div>
        </section>
    );
}
export default React.memo(SkillsSection);