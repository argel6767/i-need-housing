import os
import subprocess
from pathlib import Path
import platform
from run_backend import find_maven_home, run_application
from run_postgres import verify_postgres_db_status

new_listings_service = Path.cwd()/"new_listings_service"
isOSWindows = platform.system() == "Windows"


def load_env_file():
    file_path= new_listings_service/".env"
    

    with open(file_path) as f:
        for line in f:
            if line.startswith("#") or not line.strip():
                continue
            key, _, value = line.strip().partition("=")
            os.environ[key] = value
            print(f"Set environment variable: {key}={value}")
            
def main():
    verify_postgres_db_status()
    load_env_file()
    mvn_path = find_maven_home()
    print(mvn_path)
    run_application(mvn_path=mvn_path, curr_working_dir=new_listings_service, isOSWindows=isOSWindows)
    
if __name__ == "__main__":
    main()

