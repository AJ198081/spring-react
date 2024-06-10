import axios from "axios";

const REST_EMPLOYEE_API_URL = "http://localhost:8080/EmployerPortal/Reporting/employees";

export const listEmployees = async ()    => {
    return axios.get(REST_EMPLOYEE_API_URL.concat("/all"))
}