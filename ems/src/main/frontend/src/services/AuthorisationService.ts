import axios, {AxiosRequestConfig, AxiosResponse} from "axios";
import {RegisterationDetails} from "../types/RegisterationDetails.ts";
import {LoginDetails} from "../types/LoginDetails.ts";

const AUTH_API_BASE_URL = "http://localhost:8080";

export const registerUserDetails = (registerUserDetails: RegisterationDetails): Promise<AxiosResponse<RegisterationDetails, AxiosRequestConfig>> => {
    return axios.post(AUTH_API_BASE_URL.concat("/user/register"), registerUserDetails);
};

export const login = (loginDetails: LoginDetails): Promise<AxiosResponse<void, AxiosRequestConfig>> => {
    return axios.post(AUTH_API_BASE_URL.concat("/user/login"), loginDetails);
}

export const logout = (): Promise<AxiosResponse<void, AxiosRequestConfig>> => {
    return axios.post(AUTH_API_BASE_URL.concat("/logout"));
}

export const storeToken = (token: string) => {
    sessionStorage.setItem("token", token);
}

export const getToken = (): string | null => {
    return sessionStorage.getItem("token");
}

export const removeToken = (): void => {
    sessionStorage.removeItem(`token`);
}