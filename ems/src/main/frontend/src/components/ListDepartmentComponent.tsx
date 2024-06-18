import React, {useContext, useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import {deleteDepartment, fetchAllDepartments} from "../services/DepartmentService.ts";
import {DepartmentContext} from "../App.tsx";
import {Department} from "../types/Department.ts";

const ListDepartmentComponent = () : React.ReactNode => {

    const [error, setError]: [string, (value: (((prevState: string) => string) | string)) => void] = useState<string>('');

    const {departments, setDepartments}: {
        departments: Department[];
        setDepartments: React.Dispatch<React.SetStateAction<Department[]>>
    } = useContext(DepartmentContext)

    const navigator = useNavigate();

    useEffect(() => {
        if (sessionStorage.getItem('token')) {
            void fetchAllDepartments(departments, setDepartments, setError);
        } else {
            navigator('/login')
        }
    }, []);

    const handleUpdateDepartment = (department: Department) => {
        navigator(`/edit-department/${department.id}`, {state: department});
    };


    const handleDeleteDepartment = async (department: Department) => {
        if (department.id != null) {
            try {
                const axiosResponse = await deleteDepartment(department.id);
                if (axiosResponse.status === 204) {
                    setDepartments(prevState => {
                        return prevState.filter(dept => dept.id !== department.id);
                    })
                    navigator(`/departments`);
                } else {
                    setError(`Error occurred while deleting department with ID: ${department.id}, status: ${axiosResponse.status}`);
                }
            } catch (error) {
                setError(`Error occurred during deleting department with ID: ${department.id}`);
            }
        }
    };

    let renderDepartments: React.ReactNode = <table className={`table table-bordered table-striped table-hover`}>
        <thead>
        <tr>
            <th className={`text-center`}>Department ID</th>
            <th>Department Name</th>
            <th>Department Description</th>
            <th className={`text-center`}>Actions</th>
        </tr>
        </thead>
        <tbody>
        {
            departments.map(department => {
                return <tr key={department.id}>
                    <td className={`text-center`}>{department.id}</td>
                    <td>{department.departmentName}</td>
                    <td>{department.departmentDescription}</td>
                    <td className={`text-center`}>
                        <button className={`btn btn-info m-3`}
                                onClick={() => handleUpdateDepartment(department)}>Update
                        </button>
                        <button className={`btn btn-danger m-3`}
                                onClick={() => handleDeleteDepartment(department)}>Delete
                        </button>
                    </td>
                </tr>
            })
        }
        </tbody>
    </table>;

    return (
        <div className={"container"}>
            <h2 className={`text-center`}>List of Departments</h2>
            <Link to={`/add-department`} className={`btn btn-primary mb-2`}>Add Department</Link>
            {error ? <div className={'invalid-feedback'}>{error}</div> : renderDepartments}
        </div>
    )
}

export default ListDepartmentComponent;