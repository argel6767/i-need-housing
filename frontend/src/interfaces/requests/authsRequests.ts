export interface AuthenticateUserDto {
    username: string,
    password: string
}

export interface ChangePasswordDto {
    email: string,
    oldPassword: string,
    newPassword: string
}

export interface ForgotPasswordDto {
    email: string,
    password: string,
    verificationCode: string
}

export interface ResendEmailDto {
    email: string
}

export interface VerifyUserDto {
    email: string,
    verificationToken: string
}