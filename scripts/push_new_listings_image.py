import os
from pathlib import Path
import platform
import subprocess
import tempfile
import base64
from build_backend_image import check_if_docker_is_running, make_azure_image_name
from push_backend_image import build_and_push_with_unique_tag, is_cicd_pipeline, load_env_file, sign_in_to_azure, \
    sign_in_to_acr

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
    return f"{registry_name}/{project_id}/{repo_name}/image-{tag}:{tag}"

def deploy_new_image(image_name, service_name):
    print("Deploying new image to Cloud Run instance\n\n")
    subprocess.run([
        "gcloud", "run", "deploy", service_name,
        f"--image={image_name}",
        "--region=us-central1",
        "--platform=managed"
    ], check=True)


def update_container_app(image_name, container_app, resource_group="INeedHousing", environment_name=None):
    print(f"Updating {container_app} Container App with new image: {image_name}\n")
    update_cmd = [
        "az", "containerapp", "update",
        "--name", container_app,
        "--resource-group", resource_group,
        "--image", image_name
    ]

    update = subprocess.run(update_cmd, shell=isOSWindows, capture_output=True, text=True)
    print(update)
    print("Container app update completed\n\n")


def restart_container_app(container_app, resource_group="INeedHousing"):
    print(f"Creating new revision for {container_app} Container App\n")
    restart_cmd = [
        "az", "containerapp", "revision", "restart",
        "--name", container_app,
        "--resource-group", resource_group
    ]
    restart = subprocess.run(restart_cmd, shell=isOSWindows, capture_output=True, text=True)
    print(restart)
    print("Container app restarted\n\n")

def main():
    check_if_docker_is_running()
    if not is_cicd_pipeline():
        load_env_file()

    sign_in_to_azure()
    sign_in_to_acr()
    image_name = build_and_push_with_unique_tag(repo_name="images/new_listings_service", service=app_service_name, directory=new_listings_service, image_name_creator=make_azure_image_name)
    update_container_app(image_name, app_service_name)
    restart_container_app(app_service_name)


if __name__ == "__main__":
    main()