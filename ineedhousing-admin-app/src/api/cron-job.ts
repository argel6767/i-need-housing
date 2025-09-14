import {Job, Status} from "@/lib/models";
import {apiClient} from "@/api/apiConfig";

export const getLastSuccessfulJobs = async (numberOfJobs: number, status: Status): Promise<Job[]> => {
    try {
        const response = await apiClient.get(`/cron-jobs/jobs`, {
            params: {
                status: status,
                jobs: numberOfJobs,
            }
        });
        return response.data;
    }
    catch (error) {
        console.log(error);
        return [];
    }
}