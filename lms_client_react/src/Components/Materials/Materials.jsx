import {getAllMaterials} from "../../DAL/ServerDAL/materials-api";
import {connect} from "react-redux";
import {changeAllMaterials} from '../../Redux/Reducers/materials-reducer'
import Material from "./Material";
import {useEffect} from "react";

const Materials = (props) => {
    useEffect(() => {
        getAllMaterialsLocal()
    }, []);

    const getAllMaterialsLocal = async () => {
        let materials;
        try {
            materials = await getAllMaterials();
            if (!materials) throw new Error("MATERIALS ARE EMPTY!");
        } catch (e) {
            console.log(e.message);
        }

        props.changeAllMaterials(materials.data);
        console.log(materials);
    }

    return (
        <div className={"materials_page"}>
            {props && props.allMaterials && props.allMaterials.map(material => {
                return <Material name={material.name} key={material.name}/>
            })}
        </div>
    );
}
export default connect((state) => ({
    allMaterials: state.materials.allMaterials
}), {
    changeAllMaterials
})(Materials);