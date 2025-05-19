import os
import subprocess
import time
from pathlib import Path
import platform

from build_backend_image import build_image, check_if_docker_is_running

# Your existing setup code
backend = Path.cwd()/"backend"
isOSWindows = platform.system() == "Windows"

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
    print(sign_in)
    
def sign_in_to_acr():
    print("Logging into Azure Container Registry\n\n")
    acr_login = subprocess.run(["az", "acr", "login", "--name", "ineedhousing"])
    print(acr_login)

def build_and_push_with_unique_tag():
    image_name = build_image()
    
    print(f"Pushing image {image_name} to Azure Registry\n\n")
    push_image = subprocess.run(["docker", "push", image_name], shell=isOSWindows)
    print(push_image)
    print("Image pushed\n\n")
    
    return image_name

def update_app_service(image_name):
    print("Updating App Service container settings\n\n")
    update = subprocess.run([
        "az", "webapp", "config", "container", "set",
        "--name", "i-need-housing-backend",
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
        "--name", "i-need-housing-backend",
        "--resource-group", "INeedHousing",
        "--generic-configurations", '{"startupTimeLimit": 500}'
    ], shell=isOSWindows)
    print(config)
    print("Startup timeout configured\n\n")

def restart_app_service():
    print("Restarting App Service\n\n")
    restart = subprocess.run([
        "az", "webapp", "restart",
        "--name", "i-need-housing-backend",
        "--resource-group", "INeedHousing"
    ], shell=isOSWindows)
    print(restart)
    print("App Service restarted\n\n")

def main():
    check_if_docker_is_running()
    load_env_file()
    sign_in_to_azure()
    sign_in_to_acr()
    image_name = build_and_push_with_unique_tag()
    update_app_service(image_name)
    restart_app_service()
    
if __name__ == "__main__":
    main()
