import os
import subprocess
from pathlib import Path
import platform

'''
loads env variables to run cron job service dev server
'''

cron_job = Path.cwd()/"cron_job_service"
isOSWindows = platform.system() == "Windows"

def load_env_file():
    file_path = cron_job/".env"
    
    with open(file=file_path) as f:
        for line in f:
            if line.startswith("#") or not line.strip():
                continue
            key, _, value = line.strip().partition("=")
            os.environ[key] = value
            print(f"Set environment variable: {key}={value}")
            
def main():
    load_env_file()
    process = subprocess.run(["quarkus", "dev"], cwd=str(cron_job), shell=isOSWindows)
    print(process)
    
if __name__ == "__main__":
    main()
