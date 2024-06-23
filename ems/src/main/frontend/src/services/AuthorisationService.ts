import axios, {AxiosRequestConfig, AxiosResponse} from "axios";
import {RegisterationDetails} from "../types/RegisterationDetails.ts";
import {JwtToken, LoginDetails} from "../types/LoginDetails.ts";

const AUTH_API_BASE_URL = "http://localhost:8080";

axios.defaults.withCredentials = true

axios.interceptors.request.use((config) => {
    if (getToken()) {
        config.headers['Authorization'] = getToken();
    }
    return config;
}, (error) => {
    return Promise.reject(error)
})

export const registerUserDetails = (registerUserDetails: RegisterationDetails): Promise<AxiosResponse<RegisterationDetails, AxiosRequestConfig>> => {
    return axios.post(AUTH_API_BASE_URL.concat("/user/register"), registerUserDetails);
};

export const jwtLogin = (loginDetails: LoginDetails): Promise<AxiosResponse<JwtToken, AxiosRequestConfig>> => {
    console.log(`LoginDetails ${loginDetails}`);
    return axios.post(AUTH_API_BASE_URL.concat("/user/jwt_login"), loginDetails);
}

export const basicLogin = (loginDetails: LoginDetails): Promise<AxiosResponse<void, AxiosRequestConfig>> => {
    return axios.post(AUTH_API_BASE_URL.concat("/user/login"), loginDetails);
}

export const logout = (): Promise<AxiosResponse<void, AxiosRequestConfig>> => {
    return axios.post(AUTH_API_BASE_URL.concat("/logout"));
}

export const storeToken = (token: string) => {
    localStorage.setItem("token", token);
}

export const getToken = (): string | null => {
    return localStorage.getItem("token");
}

export const removeToken = (): void => {
    sessionStorage.removeItem(`token`);
}