import React from 'react';
import ReactDOM from 'react-dom';
import App from './App.jsx';
import {BrowserRouter} from "react-router-dom";
import {Provider} from "react-redux";
import {store} from "./Redux/redux-store";
import './Assets/Styles/reset.css'

ReactDOM.render(
    <BrowserRouter>
        <Provider store={store}>
            <App/>
        </Provider>
    </BrowserRouter>,
    document.getElementById('root')
);

window.store = store;