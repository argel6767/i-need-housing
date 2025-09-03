import os
import subprocess
from pathlib import Path
import platform
from build_backend_image import check_if_docker_is_running
import time

'''
Spins up postgres server only from compose.yaml file
'''

root = Path.cwd()
isOSWindows = platform.system() == "Windows"

def spin_up_postgres():
    process = subprocess.run(["docker", "compose", "up", "-d", "postgres"], cwd=root, shell=isOSWindows, capture_output=True)
    print(process)

def is_postgres_running():
    process = subprocess.run(["docker", "compose", "exec", "-T" , "postgres", "pg_isready", "-U", "myuser"], cwd=root, shell=isOSWindows, capture_output=True)
    return process.returncode == 0

def verify_postgres_db_status():
    check_if_docker_is_running()
    if not is_postgres_running():
        spin_up_postgres()
        
        while not is_postgres_running():
            print("Still waiting on postgres DB")
            time.sleep(2)