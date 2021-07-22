import React, {useState} from "react";
import {createNewMaterial} from "../../DAL/ServerDAL/materials-api";

const MaterialCreate = () => {
    const [name, setName] = useState("");
    const [link, setLink] = useState("");


    const sendDataToServer = (e) => {
        e.preventDefault();
        createNewMaterial(link,name).then(response => {
            console.log(response);
        }).catch(e => console.log(e.response));
    }

    const changeNameString = (e) => {
        setName(e.target.value);
    }

    const changeLinkString = (e) => {
        setLink(e.target.value);
    }

    return(
        <form className={"createMaterial"} onSubmit={sendDataToServer}>
            <input value={name} onChange={changeNameString}/>
            <input value={link} onChange={changeLinkString}/>
            <button>Login</button>
        </form>
    );
}
export default MaterialCreate;