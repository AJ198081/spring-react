import axios, {AxiosRequestConfig, AxiosResponse, CancelTokenSource, InternalAxiosRequestConfig} from "axios";
import {Employee} from "../types/Employee.ts";
import {useNavigate} from "react-router-dom";

const REST_EMPLOYEE_API_URL = "http://localhost:8080/employees";

export const listEmployees = async (cancelTokenSource: CancelTokenSource): Promise<AxiosResponse<Employee[], InternalAxiosRequestConfig>> => {
    return axios.get(REST_EMPLOYEE_API_URL.concat("/all"), {cancelToken: cancelTokenSource.token})
}

export const addEmployee = async (employee: Employee): Promise<AxiosResponse<Employee, InternalAxiosRequestConfig>> => {
    return await axios.post(REST_EMPLOYEE_API_URL, employee);
};

export const getEmployeeById = async (id: string): Promise<AxiosResponse<Employee, InternalAxiosRequestConfig>> => {
    return await axios.get(REST_EMPLOYEE_API_URL.concat(`/${id}`));
}

export const updateEmployee = async (id: string, employee: Employee) => {
    return await axios.put(REST_EMPLOYEE_API_URL.concat(`/${id}`), employee);
}

export const deleteEmployeeById = async (id: string): Promise<AxiosResponse> => {
    const navigateFunction = useNavigate();
    navigateFunction('/login');
    return await axios.delete(REST_EMPLOYEE_API_URL.concat(`/${id}`));
};

export const emailExists = async (email: string): Promise<AxiosResponse<boolean, AxiosRequestConfig>> => {
    return await axios.get(REST_EMPLOYEE_API_URL.concat(`/email/${email}`))
}
