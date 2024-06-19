import axios, {AxiosRequestConfig, AxiosResponse} from "axios";
import {RegisterationDetails} from "../types/RegisterationDetails.ts";
import {LoginDetails} from "../types/LoginDetails.ts";

const AUTH_API_BASE_URL = "http://localhost:8080/user";

export const registerUserDetails = (registerUserDetails: RegisterationDetails): Promise<AxiosResponse<RegisterationDetails, AxiosRequestConfig>> => {
    return axios.post(AUTH_API_BASE_URL.concat("/register"), registerUserDetails);
};

export const login = (loginDetails: LoginDetails): Promise<AxiosResponse<void, AxiosRequestConfig>> => {
    return axios.post(AUTH_API_BASE_URL.concat("/login"), loginDetails);
}

export const storeToken = (token: string) => {
    localStorage.setItem("token", token);
}

export const getToken = () : string | null => {
    return localStorage.getItem("token");
}

export const removeToken = (): void => {
    localStorage.removeItem(`token`);
}