import subprocess
from pathlib import Path
import platform
from build_backend_image import check_if_docker_is_running

'''
Spins up postgres server only from compose.yaml file
'''

root = Path.cwd()
isOSWindows = platform.system() == "Windows"

def spin_up_postgres():
    print("Spinning up postgres server...\n\n")
    process = subprocess.run(
        ["docker", "start", "b729429d10321034ecbfb1f24edaba15254014e16e478f1fc4835ca349601e4a"],
        cwd=root,
        shell=isOSWindows,
        capture_output=True,
        text=True
    )
    print(process)

def is_postgres_running():
    print("Checking if postgres server is running...\n\n")
    process = subprocess.run(
        ["docker", "ps", "-q", "-f", "name=b729429d10321034ecbfb1f24edaba15254014e16e478f1fc4835ca349601e4a"],
        cwd=root,
        shell=isOSWindows,
        capture_output=True,
        text=True
    )
    print(process)
    output = process.stdout.strip()
    # Return True if output is non-empty (container is running)
    return bool(output)

def main():
    check_if_docker_is_running()

    if not is_postgres_running():
        spin_up_postgres()

    print("Postgres server is running...\n\n")

def verify_db_status():
    main()

if __name__ == "__main__":
    main()