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
    service: string,
    timeStamp: string,
}

export interface HealthCheckModel {
    name: string,
    status: string,
    data: object,
}

export type Status = "SUCCESS" | "FAILED" | "TRIGGERED";

export interface Job {
    status: Status,
    jobName: string,
    timeStamp: string,
    failureReason?: string
    id: number
}

export type JobName = "i-need-housing_image" | "cron-job_image" | "new-listings_image" | "old-listings" | "key-rotation";

export interface RegisteredServiceDto {
    message: string,
    apiToken: string,
    serviceName: string,
    timestamp: string,
}

export interface TemplateDto {
    templateName: string,
    templateContent: string,
}

export interface EmailTemplate {
    templateName: string,
    templateContent: string,
    id: number
    createdAt: string,
    updatedAt: string,
}