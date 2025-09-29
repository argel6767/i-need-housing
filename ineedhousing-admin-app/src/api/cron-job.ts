import {Job, JobName, Status} from "@/lib/models";
import {apiClient} from "@/api/apiConfig";

export const getJobs = async (numberOfJobs: number, status: Status): Promise<Job[]> => {
    try {
        const response = await apiClient.get(`/admin/cron-jobs/jobs`, {
            params: {
                jobStatus: status,
                quantity: numberOfJobs,
            }
        });
        return response.data;
    }
    catch (error) {
        console.log(error);
        return [];
    }
}

export const triggerJob = async (jobName: JobName): Promise<string> => {
    try {
        await apiClient.post(`/admin/cron-jobs/jobs/${jobName}`);
        return "Cron job was triggered. Check Cron Job Service live logs for job outcome"
    }
    catch (error) {
        return "Failed to trigger cron job. View Cron Job Service live logs for job outcome";
    }
}