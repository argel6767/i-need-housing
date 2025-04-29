
from pathlib import Path
import platform
import subprocess
import time

# Your existing setup code
backend = Path.cwd()/"backend"
isOSWindows = platform.system() == "Windows"

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
    build_image()
    
if '__name__' == '__main__':
    main()