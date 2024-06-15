import axios, {AxiosRequestConfig, AxiosResponse} from "axios";
import {Department} from "../types/Department.ts";
import React from "react";

const DEPARTMENT_REST_API_BASE_URL = `http://localhost:8080/departments`

export const getAllDepartments = (): Promise<AxiosResponse<Department[], AxiosRequestConfig>> => {
    return axios.get(DEPARTMENT_REST_API_BASE_URL.concat(`/all`));
};

export const addDepartment = (department: Department): Promise<AxiosResponse<Department, AxiosRequestConfig>> => {
    return axios.post(DEPARTMENT_REST_API_BASE_URL, department);
};

export const updateDepartment = (id: number, department: Department): Promise<AxiosResponse<Department, AxiosRequestConfig>> => {
    return axios.put(DEPARTMENT_REST_API_BASE_URL.concat(`/${id}`), department);
};

export const deleteDepartment = (id: number): Promise<AxiosResponse<void, AxiosRequestConfig>> => {
    return axios.delete(DEPARTMENT_REST_API_BASE_URL.concat(`/${id}`));
}

export const fetchAllDepartments = async (departments: Department[],
                                          setDepartments: React.Dispatch<React.SetStateAction<Department[]>>,
                                          setError: React.Dispatch<React.SetStateAction<string>>): Promise<void> => {
    if (departments.length === 0) {
        try {
            let response = await getAllDepartments();
            if (response.status === 200) {
                setDepartments(response.data);
            } else {
                setError(`Error occurred while fetching departments, Status Code ${response.status}, ${response.data}`)
            }
        } catch (error) {
            setError(`Error occurred during department fetch operation, please try again later!!`);
        }
    }
};