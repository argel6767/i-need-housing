from pathlib import Path
import platform
from push_backend_image import load_env_file, sign_in_to_azure, sign_in_to_acr, build_and_push_with_unique_tag, update_app_service, restart_app_service, is_cicd_pipeline

from build_backend_image import check_if_docker_is_running

cron_job_service = Path.cwd()/"cron_job_service"
isOSWindows = platform.system() == "Windows"
app_service_name = "cron-job-service"

def main():
    check_if_docker_is_running()
    if not is_cicd_pipeline():
        print("Skipping env file loading\nRunning locally\n\n")
        load_env_file()
    sign_in_to_azure()
    sign_in_to_acr()
    image_name = build_and_push_with_unique_tag("images/cron_job_service", "cron_job_service", cron_job_service)
    update_app_service(image_name, app_service_name)
    restart_app_service(app_service_name)

if __name__ == '__main__':
    main()


