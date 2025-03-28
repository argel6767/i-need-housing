import os
import subprocess
from pathlib import Path
import platform
import time

'''
Automates the process of signing into Azure, logging into an Azure Container Registry (ACR),
building a Docker image, and pushing it to the ACR. It uses Azure CLI and Docker commands.
This script also turns off and back on the App Service to guarantee a fresh instance
'''

backend = Path.cwd()/"backend"
isOSWindows = platform.system() == "Windows"

def load_env_file(): ## azure account details
    file_path = Path.cwd()/"azure.env"
    
    with open(file_path) as f:
        for line in f:
            if line.startswith("#") or not line.strip(): ##comments and empty lines
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

def build_image():
    print("Building Image\n\n")
    build_image = subprocess.run(["docker", "build", "-t", "ineedhousing.azurecr.io/images/backend:latest", "."], cwd=str(backend), shell=isOSWindows)
    print(build_image)
    print("Image built\n\n")
    
def push_image():
    print("Pushing backend image to Azure Registry\n\n")
    push_image = subprocess.run(["docker", "push", "ineedhousing.azurecr.io/images/backend"], cwd=backend, shell=isOSWindows)
    print(push_image)
    print("Image pushed\n\n")
    
def stop_start_app_service():
    print("Stopping App Service\n\n")
    stop = subprocess.run(["az", "webapp", "stop", "--name", "i-need-housing-backend", "--resource-group", "i-need-housing"], shell=isOSWindows)
    print(stop)
    print("App Service stopped\n\n")
    
    # Give it a moment to fully stop
    time.sleep(60)
    
    print("Starting App Service\n\n")
    start = subprocess.run(["az", "webapp", "start", "--name", "i-need-housing-backend", "--resource-group", "i-need-housing"], shell=isOSWindows)
    print(start)
    print("App Service started\n\n")
    
def main():
    load_env_file()
    sign_in_to_azure()
    sign_in_to_acr()
    build_image()
    push_image()
    stop_start_app_service()
    
if __name__ == "__main__":
    main()