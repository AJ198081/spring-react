import {useEffect, useState} from "react";
import {listEmployees} from "../services/EmployeeService.ts";

interface Employee {
    fullName: string;
    id: number;
    email: string;
}

export function ListEmployeeComponent() {

    const [employees, setEmployees]: [Employee[], (value: (((prevState: Employee[]) => Employee[]) | Employee[])) => void] = useState<Employee[]>([]);

    useEffect(() => {
        listEmployees().then((response) => {
            setEmployees(response.data);
        }).catch((error) => console.log(error));
    }, [employees.length])

    return (
        <div className={"container"}>
            <h2>List of Employees</h2>
            <table className={"table table-striped table-hover table-bordered"}>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
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
                            </tr>
                        }
                    )
                }
                </tbody>
            </table>
        </div>
    );
}