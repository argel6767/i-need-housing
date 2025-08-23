import os
from pathlib import Path
import platform
import subprocess
import tempfile
import base64
from build_backend_image import check_if_docker_is_running
from push_backend_image import build_and_push_with_unique_tag, is_cicd_pipeline, load_env_file

new_listings_service = Path.cwd()/"new_listings_service"
isOSWindows = platform.system() == "Windows"
app_service_name = "new-listings-service"

def decode_gcp_service_account_key():
    print("Decoding Service Account Key base64 to JSON\n\n")
    key = os.environ["SERVICE_ACCOUNT_KEY"]
    return base64.b64decode(key).decode("utf-8")

def create_tmp_service_acc_key_file(key):
    print("Creating temp file to hold json\n\n")
    with tempfile.NamedTemporaryFile(mode="w", suffix=".json", delete=False) as temp_file:
        temp_file.write(key)
        return temp_file.name
    
def sign_in_to_gcp(file_path, project_id):
    print("Authenticating with GCP")
    subprocess.run([
                'gcloud', 'auth', 'activate-service-account', 
                '--key-file', file_path
            ], check=True, capture_output=True, text=True)
    
    print("Setting Project ID\n\n")
    subprocess.run([
                'gcloud', 'config', 'set', 'project', project_id
            ], check=True, capture_output=True, text=True)

def configure_docker_for_gcr():
    registry_name = os.environ["REGISTRY_NAME"]
    print("Configuring Docker to access Artifact Registry\n\n")
    subprocess.run(["gcloud", "auth", "configure-docker", registry_name])

def make_gcp_image(repo_name, tag):
    registry_name = os.environ["REGISTRY_NAME"]
    project_id = os.environ["PROJECT_ID"]
    return f"{registry_name}/{project_id}/{repo_name}/image-{tag}:tag"

def main():
    project_id = os.environ["PROJECT_ID"]
    check_if_docker_is_running()
    
    if not is_cicd_pipeline():
        load_env_file()

    key = decode_gcp_service_account_key()
    file_path = create_tmp_service_acc_key_file(key=key)
    sign_in_to_gcp(file_path=file_path, project_id=project_id)
    configure_docker_for_gcr()
    build_and_push_with_unique_tag(repo_name="new-listings-service-repo", service="new-listings-service", directory=new_listings_service, image_name_creator=make_gcp_image)


if __name__ == "__main__":
    main()