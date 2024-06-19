import React, {useEffect, useState} from "react";
import {deleteEmployeeById, listEmployees} from "../services/EmployeeService.ts";
import {useNavigate} from "react-router-dom";
import {Employee} from "../types/Employee.ts";
import axios, {CancelTokenSource} from "axios";
import {getToken} from "../services/AuthorisationService.ts";

export function ListEmployeeComponent() : React.ReactNode {

    const [employees, setEmployees]: [Employee[], (value: (((prevState: Employee[]) => Employee[]) | Employee[])) => void] = useState<Employee[]>([]);
    const [loading, setLoading]: [boolean, (value: ((prevState: boolean) => boolean) | boolean) => void] = useState(false);

    const navigator = useNavigate();

    useEffect( () => {
        const cancelTokenSource = axios.CancelToken.source();

        if (getToken() !== null && getToken()?.startsWith('Basic ')) {
            void fetchEmployees(cancelTokenSource);
        } else {
            navigator('/login');
        }

        return () => {
            cancelTokenSource.cancel(`Browser initiated request cancellation actioned.`)
        }
    }, [])

    const fetchEmployees = async (cancelTokenSource: CancelTokenSource) => {
        try {
            setLoading(true);
            console.log(`IsLoading: ${loading}`);
            const response = await listEmployees(cancelTokenSource)
            if (response.status === 200) {
                setEmployees(response.data);
            } else {
                console.log(`Error occurred whilst fetching employees`);
                setLoading(false);
            }
        } catch (error) {
            console.log(error);
        } finally {
            setLoading(false);
        }
    };

    const deleteEmployee = async (id: number | undefined): Promise<void> => {
        const axiosResponse = await deleteEmployeeById(`${id}`);
        if (axiosResponse.status === 204) {
            setEmployees(prevState => {
                return prevState.filter(employee => employee.id !== id);
            });
            alert(`Deleted employee with ID: ${id}`);
        } else {
            alert(`Unable to delete employee ID: ${id}`);
        }
    };

    let renderEmployeesTable = <table className={"table table-striped table-hover table-bordered"}>
        <thead>
        <tr className={`text-center`}>
            <th>ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
            <th>Department ID</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        {
           employees !== null && employees.length !== 0 && employees.map((emp) => {
                    const employeeNameArray = emp.fullName.split(" ");

                    return <tr className={`text-center`} key={emp.id}>
                        <td>{emp.id}</td>
                        <td>{employeeNameArray[0]}</td>
                        <td>{employeeNameArray[1]}</td>
                        <td>{emp.email}</td>
                        <td>{emp.departmentId}</td>
                        <td>
                            <button
                                className={`btn btn-info ms-2`}
                                onClick={() => navigator(`/update-employee/${(emp.id)}`)}
                            >
                                Update
                            </button>

                            <button
                                className={`btn btn-danger ms-2`}
                                onClick={() => deleteEmployee(emp.id)}
                            >
                                Delete
                            </button>
                        </td>
                    </tr>
                }
            )
        }
        </tbody>
    </table>;

    return (
        <div className={"container"}>
            <h2>List of Employees</h2>
            <button
                className={"btn btn-primary mb-2"}
                onClick={() => navigator('/add-employee')}
            >
                Add Employee
            </button>
            {loading ? <h2>Loading....</h2> : renderEmployeesTable}
        </div>
    );
}