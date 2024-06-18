import React from 'react';
import {useNavigate} from 'react-router-dom';
import {removeToken} from "../services/AuthorisationService.ts";

const LogoutComponent = () : React.ReactNode => {

    const navigateFunction = useNavigate();

    const handleLogout = () => {

        removeToken();
        navigateFunction('/login');
    };

    return (
        <div className="d-flex justify-content-center align-items-center min-vh-100">
            <button className="btn btn-danger" onClick={handleLogout}>Logout</button>
        </div>
    );
}

export default LogoutComponent;