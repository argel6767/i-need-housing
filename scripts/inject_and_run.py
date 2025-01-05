import os
import subprocess
from pathlib import Path
import sys

backend = Path.cwd()/"backend"
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
        
    mvn_path = Path(maven_home) / 'bin' / 'mvn.cmd'
    return mvn_path

def main():
    load_env_file()
    print(sys.argv[1])
    mvn_path = os.path.join(sys.argv[1],"bin","mvn.cmd")
    print(mvn_path)
    # Run the Spring Boot application
    process = subprocess.run([mvn_path, "spring-boot:run"], cwd=str(backend))
    print(process)

if __name__ == "__main__":
    main()