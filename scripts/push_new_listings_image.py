from pathlib import Path
import platform
from build_backend_image import check_if_docker_is_running
from push_backend_image import build_and_push_with_unique_tag, is_cicd_pipeline, load_env_file, make_azure_image_name, sign_in_to_acr, sign_in_to_azure

new_listings_service = Path.cwd()/"new_listings_service"
isOSWindows = platform.system() == "Windows"
app_service_name = "new-listings-service"

def main():
    check_if_docker_is_running()
    
    if not is_cicd_pipeline():
        load_env_file()

    sign_in_to_azure()
    sign_in_to_acr()
    image_name = build_and_push_with_unique_tag(repo_name="images/new_listings_service", service="new-listings-service", directory=new_listings_service, image_name_creator=make_azure_image_name)
    

if __name__ == "__main__":
    main()