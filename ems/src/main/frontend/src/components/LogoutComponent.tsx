import React, {useContext} from 'react';
import {useNavigate} from 'react-router-dom';
import {removeToken} from "../services/AuthorisationService.ts";
import {LoginContext} from "../App.tsx";

const LogoutComponent = () : React.ReactNode => {

    const navigateFunction = useNavigate();
    const {isUserLoggedIn, setIsUserLoggedIn} = useContext(LoginContext)

    const handleLogout = () => {
        if (isUserLoggedIn) {
            setIsUserLoggedIn(prevState => !prevState);
            removeToken();
        }
        navigateFunction('/login');
    };

    return (
        <div className="d-flex justify-content-center align-items-center min-vh-100">
            <button className="btn btn-danger" onClick={handleLogout}>Logout</button>
        </div>
    );
}

export default LogoutComponent;