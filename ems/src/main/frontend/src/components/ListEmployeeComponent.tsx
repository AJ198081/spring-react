import {useEffect, useState} from "react";
import {deleteEmployeeById, listEmployees} from "../services/EmployeeService.ts";
import {useNavigate} from "react-router-dom";
import {Employee} from "../types/Employee.ts";

export function ListEmployeeComponent() {

    const [employees, setEmployees]: [Employee[], (value: (((prevState: Employee[]) => Employee[]) | Employee[])) => void] = useState<Employee[]>([]);
    const [loading, setLoading]: [boolean, (value: ((prevState: boolean) => boolean) | boolean) => void] = useState(false);

    const navigator = useNavigate();

    useEffect( () => {
        fetchEmployees();
    }, [])

    const fetchEmployees = async () => {
        try {
            setLoading(true)
            const response = await listEmployees()
            if (response.status === 200) {
                setEmployees(response.data);
            }
        } catch (error) {
            console.log(error)
        } finally {
            setLoading(false);
        }
    };

    const deleteEmployee = async (id: number | undefined): Promise<void> => {
        const axiosResponse = await deleteEmployeeById(`${id}`);
        if (axiosResponse.status === 204) {
            setEmployees(prevState => {
                return prevState.filter(employee => employee.id !== id);
            })
            alert(`Deleted employee with ID: ${id}`);
        } else {
            alert(`Unable to delete employee ID: ${id}`);
        }
    };

    let renderEmpployeesTable = <table className={"table table-striped table-hover table-bordered"}>
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
            employees.map((emp) => {
                    const employeeNameArray = emp.fullName.split(" ");

                    return <tr key={emp.id}>
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
            {loading ? <h2>Loading....</h2> : renderEmpployeesTable}
        </div>
    );
}