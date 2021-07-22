import {SingleMaterial} from "./SingleMaterial";
import * as React from "react";

const MaterialsSection = (props) => {

    return (
        <section className={'materials'}>
            <div className="title">
                Select Materials
            </div>
            <div className="materials_list">
                {props.materials && props.materials.map(item =>
                    <SingleMaterial key={item.id} material={item.name}
                        materialId={item.id}
                        selected={item.selected}
                        selectedMaterialsIds={props.selectedMaterialsIds}
                        creator={item.creator}
                    />)}
            </div>
        </section>
    );
}
export default React.memo(MaterialsSection);