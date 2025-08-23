import os
from pathlib import Path
import platform
import subprocess
from build_backend_image import check_if_docker_is_running
from push_backend_image import build_and_push_with_unique_tag, is_cicd_pipeline, load_env_file, make_azure_image_name, sign_in_to_acr, sign_in_to_azure

new_listings_service = Path.cwd()/"new_listings_service"
isOSWindows = platform.system() == "Windows"
app_service_name = "new-listings-service"

def configure_docker_for_gcr():
    subprocess.run(["gcloud", "auth", "configure-docker"])

def make_gcp_image(repo_name, tag):
    registry_name = os.environ["REGISTRY_NAME"]
    project_id = os.environ["PROJECT_ID"]
    return f"{registry_name}/{project_id}/{repo_name}:{tag}"

def main():
    check_if_docker_is_running()
    
    if not is_cicd_pipeline():
        load_env_file()

    image_name = build_and_push_with_unique_tag(repo_name="new-listings-service-repo", service="new-listings-service", directory=new_listings_service, image_name_creator=make_gcp_image)

if __name__ == "__main__":
    main()