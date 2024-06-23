export interface LoginDetails {
    usernameOrEmail: string,
    password: string
}

export interface JwtToken {
    accessToken: string,
    tokenType: string
}