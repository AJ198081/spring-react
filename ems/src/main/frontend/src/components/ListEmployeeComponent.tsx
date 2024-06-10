import {useEffect, useState} from "react";
import {listEmployees} from "../services/EmployeeService.ts";
import {useNavigate} from "react-router-dom";
import {Employee} from "../types/Employee.ts";

export function ListEmployeeComponent() {

    const [employees, setEmployees]: [Employee[], (value: (((prevState: Employee[]) => Employee[]) | Employee[])) => void] = useState<Employee[]>([]);

    const navigator = useNavigate();

    useEffect(() => {
        listEmployees().then((response) => {
            setEmployees(response.data);
        }).catch((error) => console.log(error));
    }, [])

    return (
        <div className={"container"}>
            <h2>List of Employees</h2>
            <button
                className={"btn btn-primary mb-2"}
                onClick={() => navigator('/add-employee')}
            >
                Add Employee
            </button>
            <table className={"table table-striped table-hover table-bordered"}>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {
                    employees.map((emp, index) => {

                            const employeeNameArray = emp.fullName.split(" ");

                            return <tr key={index}>
                                <td>{emp.id}</td>
                                <td>{employeeNameArray[0]}</td>
                                <td>{employeeNameArray[1]}</td>
                                <td>{emp.email}</td>
                                <td>
                                    <button
                                        className={`btn btn-info ms-2`}
                                        onClick={() => navigator(`/update-employee/${(emp.id)}`)}
                                    >
                                        Update
                                    </button>
                                    <button
                                        className={`btn btn-danger ms-2`}
                                        onClick={() => navigator(`/delete-employee/${(emp.id)}`)}
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        }
                    )
                }
                </tbody>
            </table>
        </div>
    );
}