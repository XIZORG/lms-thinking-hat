import React from 'react';

const Material = (props) => {

    return(
        <div className={"material"}>
            <div className={'name'}>
                {props.name}
            </div>

        </div>
    );
}
export default Material;