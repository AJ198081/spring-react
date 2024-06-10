import axios, {AxiosRequestConfig, AxiosResponse, InternalAxiosRequestConfig} from "axios";
import {Employee} from "../types/Employee.ts";

const REST_EMPLOYEE_API_URL = "http://localhost:8080/employees";

export const listEmployees = async ()    => {
    return axios.get(REST_EMPLOYEE_API_URL.concat("/all"))
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

export const deleteEmployee = async (id: string): Promise<AxiosResponse> => {
    return await axios.delete(REST_EMPLOYEE_API_URL.concat(`/${id}`));
};

export const emailExists = async (email: string): Promise<AxiosResponse<boolean, AxiosRequestConfig>> => {
    return await axios.get(REST_EMPLOYEE_API_URL.concat(`/email/${email}`))
}

export const emailExistsSync = async (email: string) => {
   await axios.get(REST_EMPLOYEE_API_URL.concat(`/email/${email}`))
        .then(response => {
            if (response.status === 200) {
                return response.data ? 'Duplicated Email' : '';
            } else {
                return '';
            }
        }).catch(error => {
        console.log(error.message);
    })
}