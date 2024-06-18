import {NavLink} from "react-router-dom";
import React from "react";

const HeaderComponent = (): React.ReactNode => {

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
                            <li className={"nav-item"}>
                                <NavLink to={`/login`} className={`nav-link`}>Login</NavLink>
                            </li>
                            <li className={"nav-item"}>
                                <NavLink to={`/logout`} className={`nav-link`}>Logout</NavLink>
                            </li>
                        </ul>
                    </div>
                </nav>
            </header>
        </div>
    )
}

export default HeaderComponent;