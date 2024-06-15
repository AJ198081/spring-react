import {NavLink} from "react-router-dom";

const HeaderComponent = () => {

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
                        </ul>
                    </div>
                </nav>
            </header>
        </div>
    )
}

export default HeaderComponent;