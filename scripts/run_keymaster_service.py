import os
import subprocess
from pathlib import Path
import platform
from run_postgres import verify_postgres_db_status

'''
loads env variables to run cron job service dev server
'''

keymaster = Path.cwd()/"keymaster_service"
isOSWindows = platform.system() == "Windows"

def load_env_file():
    file_path = keymaster/".env"
    
    with open(file=file_path) as f:
        for line in f:
            if line.startswith("#") or not line.strip():
                continue
            key, _, value = line.strip().partition("=")
            os.environ[key] = value
            print(f"Set environment variable: {key}={value}")
            
def main():
    #verify_postgres_db_status()
    load_env_file()
    process = subprocess.run(["quarkus", "dev"], cwd=str(keymaster), shell=isOSWindows)
    print(process)
    
if __name__ == "__main__":
    main()