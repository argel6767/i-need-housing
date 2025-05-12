
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

def build_image():
    print('Build image')
    
    # Generate a unique timestamp tag
    tag = str(int(time.time()))
    image_name = f"ineedhousing.azurecr.io/images/backend:v{tag}"
    
    print(f"Building Image with tag: {tag}\n\n")
    build_image = subprocess.run(["docker", "build", "-t", image_name, "."], cwd=str(backend), shell=isOSWindows)
    print(build_image)
    print("Image built\n\n")
    
    return image_name

def main():
    check_if_docker_is_running()
    build_image()
    
if __name__ == '__main__':
    main()