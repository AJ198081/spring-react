import React, {useContext, useEffect, useState} from "react";
import {addDepartment, updateDepartment} from "../services/DepartmentService.ts";
import {Params, useLocation, useNavigate, useParams} from "react-router-dom";
import {Department} from "../types/Department.ts";
import {DepartmentContext} from "../App.tsx";

export default function ManageDepartmentComponent(): React.ReactNode {

    const {setDepartments} = useContext(DepartmentContext)

    const [departmentName, setDepartmentName]: [string, (value: (((prevState: (string)) => (string)) | string)) => void] = useState<string>('');
    const [departmentDescription, setDepartmentDescription]: [string, (value: (((prevState: (string)) => (string)) | string)) => void] = useState<string>('');
    const [errorMessage, setErrorMessage]: [string, (value: (((prevState: (string)) => (string)) | string)) => void] = useState<string>('');

    const navigateFunction = useNavigate();

    const {id} = useParams<Readonly<Params>>();
    const {state} = useLocation();

    const saveDepartmentHandler = async (e: React.MouseEvent<HTMLButtonElement> | React.ChangeEvent<HTMLButtonElement>) => {
        e.preventDefault();
        if (!departmentName || !departmentDescription) {
            return;
        } else {
            const department: Department = {
                departmentName,
                departmentDescription
            }

            try {
                const response = await addDepartment(department);
                if (response.status === 201) {
                    setDepartments((prevState) => {
                        return [...prevState, response.data];
                    })
                    navigateFunction(`/departments`)
                }
            } catch (error) {
                console.log(errorMessage, error);
                setErrorMessage(`Error encountered during save operation, ${error}`)
            }
        }
    };

    useEffect(() => {
        if (state !== null) {
            setDepartmentName(state.departmentName);
            setDepartmentDescription(state.departmentDescription);
        }
    }, [state])

    const updateThisDepartment = async (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();

        if (!departmentName || !departmentDescription) {
            setErrorMessage(`Department details are mandatory.`)
        } else {
            try {
                const response = await updateDepartment(Number(id), {
                    departmentName,
                    departmentDescription
                });
                if (response.status === 200) {
                    setDepartments((prevState) => {
                        const updatedDepartment = response.data;
                        const allDepartments = prevState.filter(department => department.id !== updatedDepartment.id);
                        return [...allDepartments, updatedDepartment];
                    });
                    navigateFunction(`/departments`);
                }
            } catch (error) {
                setErrorMessage(`Error encountered during save operation, ${error}`)
            }
        }
    };

    return (
        <div className={'container mt-3'}>
            <div className={`card col-md-6 offset-md-3 offset-md-3`}>
                <h2 className={`text-center`}>{(id !== undefined) ? `Update Department` : `Add Department`}</h2>
                <div className={`card-body`}>
                    <form>
                        <div className={`form-group mb-2`}>
                            <label className={`form-label`}>Department name</label>
                            <input
                                type={`text`}
                                className={`form-control`}
                                name={`departmentName`}
                                placeholder={`Enter department name`}
                                value={departmentName}
                                onChange={(e: React.ChangeEvent<HTMLInputElement>) => setDepartmentName(e.target.value)}
                            />
                        </div>
                        <div className={`form-group mb-2`}>
                            <label className={`form-label`}>Department description</label>
                            <input
                                type={`text`}
                                className={`form-control`}
                                name={`departmentDescription`}
                                placeholder={`Enter department description`}
                                value={departmentDescription}
                                onChange={(e: React.ChangeEvent<HTMLInputElement>) => setDepartmentDescription(e.target.value)}
                            />
                        </div>
                        {
                            (id !== undefined
                                ? <button className={`btn btn-info m-3`}
                                          onClick={(e: React.MouseEvent<HTMLButtonElement>) => updateThisDepartment(e)}>Update
                                    Department
                                </button>
                                : <button className={`btn btn-success m-3`}
                                          onClick={(e: React.MouseEvent<HTMLButtonElement>) => saveDepartmentHandler(e)}>Persist
                                    Department
                                </button>
                        )}
                    </form>
                </div>
            </div>
        </div>
    );
}
