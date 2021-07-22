import {useState} from "react";

export const SingleMaterial = (props) => {

    const [selected, setSelected] = useState(props.selected);
    console.log("SELECTED IDS", props.selectedMaterialsIds.current);
    const toggleId = (id) => {
        if (props.selectedMaterialsIds.current.find(materialId => id === materialId) === undefined) {
            props.selectedMaterialsIds.current.push(id);
        } else {
            props.selectedMaterialsIds.current = props.selectedMaterialsIds.current.filter(materialsId => materialsId !== id);
        }
        setSelected(!selected);
    }
    console.log("Rendered!");
    return (
        <div className={'material ' + (selected ? "selected" : "")} onClick={e => toggleId(props.materialId)}>
            <div className="material_name">
                {props.material}
            </div>
            <div className="material_creator">
                Created by {props.creator}
            </div>
        </div>
    );
}