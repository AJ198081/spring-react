export interface Employee {
    fullName: string;
    id?: number;
    email: string;
    departmentId: number
}

export interface EmployeeError {
    firstName: string,
    lastName: string,
    email: string,
    departmentId: string
}