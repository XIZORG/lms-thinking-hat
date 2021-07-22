import React from 'react'
const CourseMaterial = ({name,id,status,toggleMaterialStatus,link}) => {

    const materialClickHandler = () => {
        window.open(link, '')
        toggleMaterialStatus(id);
    }

    return (
        <div className={'material' + (status ? " passed" : "")} onClick={materialClickHandler}>
            <div className="material_name">
                {name}
            </div>
        </div>
    );
}
export default React.memo(CourseMaterial) ;