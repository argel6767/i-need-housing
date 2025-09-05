import os
import subprocess
from pathlib import Path
import platform
from run_postgres import verify_postgres_db_status

'''
loads env variables for the desired environment of the backend Dev or Prod
'''

backend = Path.cwd()/"backend"
isOSWindows = platform.system() == "Windows"

def load_env_file():
    environment = input("Is this Dev? Y/N\n")
    file_path=""
    
    if (environment == "Y"):
        file_path = backend/"dev.env"
    else:
        file_path = backend/"prod.env"
    

    with open(file_path) as f:
        for line in f:
            if line.startswith("#") or not line.strip():
                continue
            key, _, value = line.strip().partition("=")
            os.environ[key] = value
            print(f"Set environment variable: {key}={value}")
            
def find_maven_home():
    maven_home = os.environ.get('MAVEN_HOME')
    if not maven_home:
        raise EnvironmentError("MAVEN_HOME environment variable is not set")
    
    if isOSWindows:
        mvn_path = Path(maven_home) / 'bin' / 'mvn.cmd'
    else:
        mvn_path = Path(maven_home) / 'bin' / 'mvn'
        
    return mvn_path

def run_application(mvn_path, curr_working_dir, isOSWindows):
    process = subprocess.run([mvn_path, "spring-boot:run"], cwd= curr_working_dir, shell=isOSWindows)
    print(process)

def main():
    #verify_postgres_db_status()
    load_env_file()
    mvn_path = find_maven_home()
    print(mvn_path)
    run_application(mvn_path=mvn_path, curr_working_dir=backend, isOSWindows=isOSWindows)

if __name__ == "__main__":
    main()