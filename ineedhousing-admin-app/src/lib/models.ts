export interface UserDto {
    id: number,
    email: string,
    authorities: string[],
    lastLogin: string,
    createdAt: string,
}

export interface LogEvent {
    log: string,
    level: string,
    timeStamp: string,
}

export interface HealthCheckModel {
    name: string,
    status: string,
    data: object,
}

export type Status = "SUCCESS" | "FAILURE";

export interface Job {
    status: Status,
    jobName: string,
    timeStamp: string,
    failureReason?: string
}