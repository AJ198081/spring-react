import React from 'react';
import ReactDOM from 'react-dom/client';
// import App from './App.tsx';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import UserSearch from "./components/practice/state/UserSearch.tsx";
// import GuestList from "./components/practice/state/GuestList.tsx";
// import {Parent} from "./components/practice/Parent.tsx";

ReactDOM.createRoot(document.getElementById('root')!).render(
    <React.StrictMode>
        <div className={"container"}>
                {/*<GuestList/>*/}
                <UserSearch />

        </div>
        {/*<App />*/}
    </React.StrictMode>,
)
