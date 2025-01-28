import os
import subprocess
from pathlib import Path
import platform

backend = Path.cwd()/"backend"
isOSWindows = platform.system() == "Windows"
print(backend)
def load_env_file():
    environment = input("Is this Dev? Y/N\n")
    file_path="";
    
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

def main():
    load_env_file()
    mvn_path = find_maven_home()
    print(mvn_path)
    # Run the Spring Boot application
    process = subprocess.run([mvn_path, "spring-boot:run"], cwd=str(backend), shell=isOSWindows)
    
    print(process)

if __name__ == "__main__":
    main()