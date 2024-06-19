import {NavLink} from "react-router-dom";
import React, {useContext, useEffect} from "react";
import {getToken} from "../services/AuthorisationService.ts";
import {LoginContext} from "../App.tsx";

const HeaderComponent = (): React.ReactNode => {

    // const [isUserLoggedIn, setIsUserLoggedIn] = useState<boolean>(false);

    const {isUserLoggedIn, setIsUserLoggedIn} = useContext(LoginContext)

    useEffect(() => {
        if (getToken() !== null && getToken()?.startsWith('Basic ')) {
            setIsUserLoggedIn(true);
        }
    }, [isUserLoggedIn]);

    let renderLoginLink = <li className={"nav-item"}>
        <NavLink to={`/login`} className={`nav-link`}>Login</NavLink>
    </li>;
    let renderLogoutLink = <li className={"nav-item"}>
        <NavLink to={`/logout`} className={`nav-link`}>Logout</NavLink>
    </li>;

    return (
        <div>
            <header>
                <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
                    <a className="navbar-brand m-1 ms-2" href="/">Employee Management System</a>
                    <div className={`collapse navbar-collapse`} id={"navbarNav"}>
                        <ul className={"navbar-nav"}>
                            <li className={"nav-item"}>
                                <NavLink to={`/employees`} className={`nav-link`}>Employees</NavLink>
                            </li>
                            <li className={"nav-item"}>
                                <NavLink to={"/departments"} className={"nav-link"}>Department</NavLink>
                            </li>
                            <li className={"nav-item"}>
                                <NavLink to={`/register`} className={`nav-link`}>Register</NavLink>
                            </li>
                            {isUserLoggedIn ? renderLogoutLink : renderLoginLink}
                        </ul>
                    </div>
                </nav>
            </header>
        </div>
    )
}

export default HeaderComponent;