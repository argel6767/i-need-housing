import os
from pathlib import Path
import platform
from build_backend_image import check_if_docker_is_running
from push_backend_image import build_and_push_with_unique_tag, is_cicd_pipeline, load_env_file
from push_new_listings_image import decode_gcp_service_account_key, create_tmp_service_acc_key_file, sign_in_to_gcp, \
    configure_docker_for_gcr, make_gcp_image, deploy_new_image

email_service = Path.cwd() / "email_service"
isOsWindows = platform.system() == "Windows"
app_service_name = "email-service"
repo_name = "email-service-repo"

def main():
    project_id = os.environ["PROJECT_ID"]
    check_if_docker_is_running()

    if not is_cicd_pipeline():
        load_env_file()

    key = decode_gcp_service_account_key()
    file_path = create_tmp_service_acc_key_file(key)
    sign_in_to_gcp(file_path, project_id)
    configure_docker_for_gcr()
    image_name = build_and_push_with_unique_tag(repo_name, app_service_name, email_service, make_gcp_image)
    deploy_new_image(image_name=image_name, service_name=app_service_name)

if __name__ == "__main__":
    main()