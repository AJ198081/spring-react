import React, {ChangeEvent, MouseEvent, useContext, useEffect, useState} from "react";
import {
    addEmployee, emailExists,
    getEmployeeById,
    updateEmployee
} from '../services/EmployeeService.ts';
import {Params, useNavigate, useParams} from "react-router-dom";
import {Employee, EmployeeError} from "../types/Employee.ts";
import {AxiosRequestConfig, AxiosResponse} from "axios";
import {DepartmentContext} from "../App.tsx";
import {fetchAllDepartments} from "../services/DepartmentService.ts";
import {Department} from "../types/Department.ts";


export const ManageEmployeeComponent = () => {

    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');

    const [departmentId, setDepartmentId]: [number, React.Dispatch<React.SetStateAction<number>>] = useState<number>(0);
    const {departments, setDepartments} = useContext(DepartmentContext);

    const [employeeError, setEmployeeError] = useState<EmployeeError>({
        firstName: '',
        lastName: '',
        email: '',
        departmentId: ''
    });

    const [_, setApiError] = useState<string>('')

    const {id} = useParams<Readonly<Params>>();

    const navigateFunction = useNavigate();

    useEffect(() => {
        if (id) {
            getEmployeeById(id).then((response) => {
                const employee = response.data;
                setFirstName(employee.fullName.split(' ')[0]);
                setLastName(employee.fullName.split(' ')[1]);
                setEmail(employee.email);
            })
        }
    }, [id])

    useEffect(() => {
        void fetchAllDepartments(departments, setDepartments, setApiError);
    }, [])


    const saveOrUpdateEmployee = async (employee: Employee, id?: string) => {
        try {
            const response: AxiosResponse<Employee, AxiosRequestConfig> = id ? await updateEmployee(id, employee) : await addEmployee(employee);
            const expectedStatus: 200 | 201 = id ? 200 : 201;
            const errorMessage: string = id ? `Error occurred during update employee, ${id}: ${JSON.stringify(employee)}` : `Error occurred in saving employee, ${JSON.stringify(employee)}`;

            if (response.status === expectedStatus) {
                navigateFunction('/employees');
            } else {
                console.log(errorMessage);
            }
        } catch (axiosError) {
            console.log(`Network error during persistence operation ${axiosError}`);
        }
    }

    let validateEmailUniquenessAndSaveOrUpdateEmployee = async function (employee: Employee) {
        try {

            if (id) {
                const response = await getEmployeeById(id)
                const employeeFromDatabase = response.data;
                if (employee.email === employeeFromDatabase.email) {
                    await saveOrUpdateEmployee(employee, id);
                } else {
                    const response = await emailExists(employee.email);
                    if (response.status === 200 && !response.data) {
                        await saveOrUpdateEmployee(employee, id);
                    } else if (response.status === 200 && response.data) {
                        setEmployeeError({
                            ...employeeError,
                            email: 'Duplicated email, ${email} already exists in our database.'
                        })
                    }
                }
            } else {
                emailExists(employee.email)
                    .then(response => {
                        if (response.status === 200 && !response.data) {
                            saveOrUpdateEmployee(employee);
                        } else if (response.status === 200 && response.data) {
                            let errorCopy = {...employeeError};
                            errorCopy.email = `Duplicated email, ${email} already exists in our database.`;
                            setEmployeeError(errorCopy);
                        }
                    });
            }
        } catch
            (axiosEmail) {
            setEmployeeError(error => {
                error.email = `Error occurred during validation for email: ${axiosEmail}, try again later`;
                return error;
            })
        }
    }

    const handleEmployeePersistence = async (e: MouseEvent<HTMLButtonElement> | ChangeEvent<HTMLButtonElement>) => {
        e.preventDefault();

        if (validateForm()) {
            const employee: Employee = {
                fullName: firstName.concat(' ').concat(lastName),
                email: email,
                departmentId: departmentId
            };

            await validateEmailUniquenessAndSaveOrUpdateEmployee(employee);
        }
    };

    const pageTitle = () => {
        if (id) {
            return <h2 className={'text-center mt-3'}>Edit Employee</h2>;
        } else {
            return <h2 className={'text-center mt-3'}>Add Employee</h2>;
        }
    }

    const validateForm = () => {
        const validateFormField = (fieldValue: string, fieldName: string) => {
            let errorMessage = '';
            if (!fieldValue.trim()) {
                errorMessage = `${fieldName} is required`;
            }
            return errorMessage;
        };

        const errorsCopy = {...employeeError};

        errorsCopy.firstName = validateFormField(firstName, 'First name');
        errorsCopy.lastName = validateFormField(lastName, 'Last name');
        errorsCopy.email = validateFormField(email, 'Email');
        errorsCopy.departmentId = validateFormField(String(departmentId === 0 ? '' : departmentId), 'Department Id');
       
        const validateEmailSyntax = (email: string) => {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return emailRegex.test(email) ? '' : 'Invalid email format';
        }

        if (!errorsCopy.email) {
            errorsCopy.email = validateEmailSyntax(email);
        }

        setEmployeeError(errorsCopy);

        return !(errorsCopy.firstName || errorsCopy.lastName || errorsCopy.email || errorsCopy.departmentId);
    };

    return (
        <div className={'container mt-3'}>
            <div className={'row'}>
                <div className={'card col-md-6 offset-md-3 offset-md-3'}>
                    {pageTitle()}
                    <div className={'card-body align-content-around ms-5 mt-3'}>
                        <form>
                            <div className={'form-group mb-2'}>
                                <label className="form-label">First Name</label>
                                <input
                                    className={`form-control ${employeeError.firstName ? 'is-invalid' : ''}`}
                                    type={'text'}
                                    placeholder={'Enter first name, max 50 character'}
                                    value={firstName}
                                    maxLength={50}
                                    onChange={(e: ChangeEvent<HTMLInputElement>) => setFirstName(e.target.value)}
                                />
                                {employeeError.firstName &&
                                    <div className={'invalid-feedback'}>{employeeError.firstName}</div>}
                            </div>
                            <div className={'form-group mb-2'}>
                                <label className="form-label">Last Name</label>
                                <input
                                    className={`form-control ${employeeError.lastName ? 'is-invalid' : ''}`}
                                    type={'text'}
                                    placeholder={'Enter last name, max 60 character)'}
                                    maxLength={60}
                                    value={lastName}
                                    onChange={(e: ChangeEvent<HTMLInputElement>) => setLastName(e.target.value)}
                                />
                                {employeeError.lastName &&
                                    <div className={'invalid-feedback'}>{employeeError.lastName}</div>}
                            </div>
                            <div className={'form-group mb-2'}>
                                <label className="form-label">Email</label>
                                <input
                                    className={`form-control ${employeeError.email ? 'is-invalid' : ''}`}
                                    type={'text'}
                                    maxLength={80}
                                    placeholder={'Enter email, must be unique, max 80 character'}
                                    value={email}
                                    onChange={(e: ChangeEvent<HTMLInputElement>) => setEmail(e.target.value)}
                                />
                                {employeeError.email && <div className={'invalid-feedback'}>{employeeError.email}</div>}
                            </div>
                            <div className={'form-group mb-2'}>
                                <label className="form-label">Select Department</label>
                                <select
                                    className={`form-control ${employeeError.departmentId ? 'is-invalid' : ''}`}
                                    value={departmentId}
                                    onChange={(e) =>
                                        !isNaN(Number(e.target.value))
                                            ? setDepartmentId(Number(e.target.value))
                                            : setDepartmentId(0)
                                    }
                                >
                                    <option value={"Select Department"}>Select Department</option>
                                    {
                                        [...new Set(departments)]
                                            .map((department: Department) => {
                                                return <option key={department.id}
                                                               value={department.id}>{department.departmentName}</option>
                                            })
                                    }
                                </select>
                                {employeeError.departmentId && <div className={'invalid-feedback'}>{employeeError.departmentId}</div>}
                            </div>
                            <button className={'btn btn-success m-4 align-content-center'}
                                    onClick={handleEmployeePersistence}>
                                Persist Employee
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}
