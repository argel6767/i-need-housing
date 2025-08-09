from pathlib import Path
import platform
from push_backend_image import load_env_file, sign_in_to_azure, sign_in_to_acr, build_and_push_with_unique_tag

from build_backend_image import check_if_docker_is_running

cron_job_service = Path.cwd()/"cron_job_service"
isOSWindows = platform.system() == "Windows"

def main():
    check_if_docker_is_running()
    load_env_file()
    sign_in_to_azure()
    sign_in_to_acr()
    build_and_push_with_unique_tag("images/cron_job_service", "cron_job_service", cron_job_service)


if __name__ == '__main__':
    main()


