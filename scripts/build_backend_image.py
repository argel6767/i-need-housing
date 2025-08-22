from pathlib import Path
import platform
import subprocess
import sys
import time

# Your existing setup code
backend = Path.cwd()/"backend"
isOSWindows = platform.system() == "Windows"

#check if the docker daemon is running before anything is done
def check_if_docker_is_running():
    docker_check = subprocess.run(["docker", "info"], capture_output=True, shell=isOSWindows)
    if docker_check.returncode != 0:
        print("ERROR: Docker is not running. Please start Docker Desktop.")
        sys.exit(1)
        
def make_azure_image_name(repo_name, tag):
    return f"ineedhousing.azurecr.io/{repo_name}:v{tag}"

def build_image(repo_name, service, directory, image_name_creator):
    print(f'Building {service} image\n\n')
    
    # Generate a unique timestamp tag
    tag = str(int(time.time()))
    image_name = image_name_creator(repo_name, tag)
    
    print(f"Building Image with tag: {tag}\n\n")
    building_process = subprocess.run(["docker", "build", "-t", image_name, "."], cwd=str(directory), shell=isOSWindows)
    print(building_process)
    if building_process.returncode != 0:
        print("ERROR: Image build failed.")
        sys.exit(1)
    print("Image built\n\n")
    
    return image_name

def main():
    check_if_docker_is_running()
    build_image("images/backend", 'backend', backend)
    
if __name__ == '__main__':
    main()