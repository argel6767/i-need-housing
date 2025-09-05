from pathlib import Path
import platform
import subprocess
from concurrent.futures import ThreadPoolExecutor, as_completed
from build_backend_image import build_image, check_if_docker_is_running

rootDirectory = Path.cwd()
ineedhousing_api = rootDirectory / "backend"
new_listings_service = rootDirectory / "new_listings_service"
cron_job_service = rootDirectory / "cron_job_service"
keymaster_service = rootDirectory / "keymaster_service"
services = [("ineedhousing_api", ineedhousing_api), ("cron_job_service", cron_job_service), ("new_listings_service", new_listings_service), ("keymaster_service", keymaster_service)]
service_env_variables = {"ineedhousing_api": {}, "cron_job_service": {}, "new_listings_service": {}, "keymaster_service": {}}

isOsWindows = platform.system() == "Windows"

def create_image_name(repo_name, tag):
    return f"{repo_name}:{tag}"


def build_service_images():
    print("Building all service images")
    image_names = []

    with ThreadPoolExecutor() as executor:
        # Submit all build_image tasks (as a list)
        futures = [
            executor.submit(
                build_image,
                repo_name="prod_simulation",
                service=service[0],
                directory=service[1],
                image_name_creator=create_image_name
            )
            for service in services
        ]

        # Collect results as they complete
        for future in as_completed(futures):
            try:
                image_name = future.result()
                print(f"Successfully built image: {image_name}")
                image_names.append(image_name)
            except Exception as e:
                print(f"Build failed: {e}")

    return image_names

def fill_env_variable_for_service(service):
    service_name = service[0]
    directory = service[1]

    current_service_env = service_env_variables[service_name]

    file_path = directory/".env"

    with open(file_path, "r") as file:
        for line in file:
            if line.startswith("#") or not line.strip():
                continue
            key, _, value = line.strip().partition("=")
        current_service_env[key] = value
        print(f"Set environment variable: {key}={value} for service {service_name}")

def fill_service_env_variables():
    print("Filling environment variables for all services \n\n")
    for service in services:
        fill_env_variable_for_service(service)

def run_service_images(image_names):
    print("Running all service images \n\n")
    for image_name in image_names:
        service_name = image_name.split(":")[1]
        env_vars = service_env_variables.get(service_name)

        env_args = []
        for key, value in env_vars.items():
            env_args.extend(["-e", f"{key}={value}"])

        cmd = ["docker", "run"] + env_args + [image_name]
        current_image = subprocess.run(cmd, cwd=rootDirectory, shell=isOsWindows)
        print(current_image)


def main():
    check_if_docker_is_running()
    image_names = build_service_images()
    fill_service_env_variables()
    run_service_images(image_names)

if __name__ == "__main__":
    main()

