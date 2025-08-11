import os
import subprocess
from pathlib import Path
import platform

from build_backend_image import build_image, check_if_docker_is_running

backend = Path.cwd()/"backend"
isOSWindows = platform.system() == "Windows"
app_service_name = "i-need-housing-backend"

def load_env_file():
    # Your existing env loading code
    file_path = Path.cwd()/"azure.env"
    
    with open(file_path) as f:
        for line in f:
            if line.startswith("#") or not line.strip():
                continue
            key,_,value = line.strip().partition("=")
            os.environ[key] = value
            print(f"Set Environment variable: {key}={value}")

def sign_in_to_azure():
    print("Signing In to Azure via CLI\n\n")
    sign_in = subprocess.run(["az", "login", "--tenant", os.environ.get("TENANT_ID"), "-u", os.environ.get("USERNAME"), "-p", os.environ.get("PASSWORD")], shell=isOSWindows)
    print("Signed in to Azure\n\n")
    
def sign_in_to_acr():
    print("Logging into Azure Container Registry\n\n")
    acr_login = subprocess.run(["az", "acr", "login", "--name", "ineedhousing"])
    print("Logged into Azure Container Registry\n\n")

def build_and_push_with_unique_tag(repo_name, service, directory):
    image_name = build_image(repo_name, service, directory)
    
    print(f"Pushing image {image_name} to Azure Registry\n\n")
    push_image = subprocess.run(["docker", "push", image_name], shell=isOSWindows)
    print(push_image)
    print("Image pushed\n\n")
    
    return image_name

def update_app_service(image_name, app_service):
    print(f"Updating {app_service} App Service container settings\n\n")
    update = subprocess.run([
        "az", "webapp", "config", "container", "set",
        "--name", app_service,
        "--resource-group", "INeedHousing",
        "--container-image-name", image_name,
        "--container-registry-url", "https://ineedhousing.azurecr.io"
    ], shell=isOSWindows)
    print(update)
    print("Container settings updated\n\n")
    
    # Increase the startup timeout
    print("Configuring longer startup timeout\n\n")
    config = subprocess.run([
        "az", "webapp", "config", "set",
        "--name", app_service,
        "--resource-group", "INeedHousing",
        "--generic-configurations", '{"startupTimeLimit": 500}'
    ], shell=isOSWindows)
    print(config)
    print("Startup timeout configured\n\n")

def restart_app_service(app_service):
    print(f"Restarting {app_service} App Service\n\n")
    restart = subprocess.run([
        "az", "webapp", "restart",
        "--name", app_service,
        "--resource-group", "INeedHousing"
    ], shell=isOSWindows)
    print(restart)
    print("App Service restarted\n\n")

def main():
    check_if_docker_is_running()
    load_env_file()
    sign_in_to_azure()
    sign_in_to_acr()
    image_name = build_and_push_with_unique_tag("images/backend", "backend", backend)
    update_app_service(image_name, app_service_name)
    restart_app_service(app_service_name)
    
if __name__ == "__main__":
    main()
