const CHANGE_ALL_MATERIALS = "CHANGE_ALL_MATERIALS";
let initialState = {
    allMaterials: []
}

export const materialsReducer = (state = initialState, action) => {
    switch(action.type) {
        case CHANGE_ALL_MATERIALS: {
            return {...state, allMaterials: action.materials}
        }
    }
    return state;
}

export const changeAllMaterials = (materials) => {
    return {type: CHANGE_ALL_MATERIALS, materials}
}