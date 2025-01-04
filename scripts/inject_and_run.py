import os
import subprocess

def load_env_file(file_path=".env"):
    """Load environment variables from a .env file."""
    if not os.path.exists(file_path):
        print(f"File {file_path} does not exist.")
        return

    with open(file_path) as f:
        for line in f:
            if line.startswith("#") or not line.strip():
                continue
            key, _, value = line.strip().partition("=")
            os.environ[key] = value
            print(f"Set environment variable: {key}={value}")

def main():
    load_env_file()
    # Run the Spring Boot application
    subprocess.run(["./mvnw", "spring-boot:run"])  # Adjust for your OS

if __name__ == "__main__":
    main()