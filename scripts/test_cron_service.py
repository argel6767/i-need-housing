import subprocess
from run_cron_service import isOSWindows, cron_job, load_env_file

'''
loads env variables to run cron job service test suite
'''

def main():
    load_env_file()
    process = subprocess.run(["quarkus", "test"], cwd=str(cron_job), shell=isOSWindows)
    print(process)
    
if __name__ == "__main__":
    main()

