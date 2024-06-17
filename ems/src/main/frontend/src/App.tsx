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


export const DepartmentContext: React.Context<{
    departments: Department[];
    setDepartments: React.Dispatch<React.SetStateAction<Department[]>>
}> = React.createContext<{
    departments: Department[];
    setDepartments: React.Dispatch<React.SetStateAction<Department[]>>;
}>({
    departments: [],
    setDepartments: () => {
    }
});

function App() {

    const [departments, setDepartments]: [Department[], (value: (((prevState: Department[]) => Department[]) | Department[])) => void] = useState<Department[]>([]);


    return (
        <React.Fragment>
            <BrowserRouter>
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
                    </Routes>
                </DepartmentContext.Provider>
                <FooterComponent/>
            </BrowserRouter>
        </React.Fragment>
    );
}

export default App
