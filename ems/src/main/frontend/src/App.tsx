import {ListEmployeeComponent} from "./components/ListEmployeeComponent.tsx";
import HeaderComponent from "./components/HeaderComponent.tsx";
import FooterComponent from "./components/FooterComponent.tsx";
import React, {useState} from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {ManageEmployeeComponent} from "./components/ManageEmployeeComponent.tsx";
import ListDepartmentComponent from "./components/ListDepartmentComponent.tsx";
import ManageDepartmentComponent from "./components/ManageDepartmentComponent.tsx";
import {Department} from "./types/Department.ts";
import RegisterComponent from "./components/RegisterComponent.tsx";
import LoginComponent from "./components/LoginComponent.tsx";
import LogoutComponent from "./components/LogoutComponent.tsx";


interface DepartmentContextType {
    departments: Department[];
    setDepartments: React.Dispatch<React.SetStateAction<Department[]>>;
}

export const DepartmentContext: React.Context<DepartmentContextType> = React.createContext<DepartmentContextType>({
    departments: [],
    setDepartments: () => {
    }
});

export const LoginContext: React.Context<{
    isUserLoggedIn: boolean;
    setIsUserLoggedIn: React.Dispatch<React.SetStateAction<boolean>>
}> = React.createContext<{
    isUserLoggedIn: boolean;
    setIsUserLoggedIn: React.Dispatch<React.SetStateAction<boolean>>
}>({
    isUserLoggedIn: false,
    setIsUserLoggedIn: () => {
    }
})

function App() {

    const [departments, setDepartments]: [Department[], (value: (((prevState: Department[]) => Department[]) | Department[])) => void] = useState<Department[]>([]);
    const [isUserLoggedIn, setIsUserLoggedIn]: [boolean, (value: (((prevState: boolean) => boolean) | boolean)) => void] = useState<boolean>(false);

    return (
        <React.Fragment>
            <BrowserRouter>
                <LoginContext.Provider value={{isUserLoggedIn, setIsUserLoggedIn}}>
                    <HeaderComponent/>
                    <DepartmentContext.Provider value={{departments, setDepartments}}>
                        <Routes>
                            <Route path={"/"} element={<ListEmployeeComponent/>}/>
                            <Route path={"/employees"} element={<ListEmployeeComponent/>}/>
                            <Route path={"/add-employee"} element={<ManageEmployeeComponent/>}/>
                            <Route path={"/update-employee/:id"} element={<ManageEmployeeComponent/>}/>
                            <Route path={"/delete-employee/:id"} element={<ListEmployeeComponent/>}/>
                            <Route path={"/departments"} element={<ListDepartmentComponent/>}/>
                            <Route path={"/add-department"} element={<ManageDepartmentComponent/>}/>
                            <Route path={"/edit-department/:id"} element={<ManageDepartmentComponent/>}/>
                            <Route path={"/register"} element={<RegisterComponent/>}/>
                            <Route path={"/login"} element={<LoginComponent/>}/>
                            <Route path={"/logout"} element={<LogoutComponent/>}/>
                        </Routes>
                    </DepartmentContext.Provider>
                </LoginContext.Provider>
                <FooterComponent/>
            </BrowserRouter>
        </React.Fragment>
    );
}

export default App
