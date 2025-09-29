import {apiClient} from "@/api/apiConfig";
import {RegisteredServiceDto} from "@/lib/models";
import {AxiosError} from "axios";

export const registerService = async (serviceName: string): Promise<RegisteredServiceDto> => {
    serviceName = serviceName.replaceAll(" ", "_");
    try {
        const response = await apiClient.post(`/admin/keymaster-service/register-service/${serviceName}`)
        return response.data
    }
    catch (error) {
        console.error(error)
        if (error instanceof AxiosError) {
            return {apiToken: "Failed to register new service", message: error.message, serviceName: serviceName, timestamp: ""}
        }
        return  {apiToken: "Failed to register new service", message: error as string, serviceName: serviceName, timestamp: ""}
    }
}