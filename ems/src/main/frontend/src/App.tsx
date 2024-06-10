import {ListEmployeeComponent} from "./components/ListEmployeeComponent.tsx";
import HeaderComponent from "./components/HeaderComponent.tsx";
import FooterComponent from "./components/FooterComponent.tsx";
import React from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {ManageEmployeeComponent} from "./components/ManageEmployeeComponent.tsx";

function App() {

    return (
        <React.Fragment>
            <BrowserRouter>
                <HeaderComponent/>
                <Routes>
                    <Route path={"/"} element={<ListEmployeeComponent/>}/>
                    <Route path={"/employees"} element={<ListEmployeeComponent/>}/>
                    <Route path={"/add-employee"} element={<ManageEmployeeComponent/>}/>
                    <Route path={"/update-employee/:id"} element={<ManageEmployeeComponent/>}/>
                    <Route path={"/delete-employee/:id"} element={<ManageEmployeeComponent/>}/>
                </Routes>
                <FooterComponent/>
            </BrowserRouter>
        </React.Fragment>
    );
}

export default App
