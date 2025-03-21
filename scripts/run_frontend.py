import os
import subprocess
from pathlib import Path
import platform

'''
Runs frontend dev server for development
'''

frontend = Path.cwd()/"frontend"
isWindows = platform.system() == "Windows"

def run_sever():
    process = subprocess.run(["npm", "run", "dev"], cwd=str(frontend), shell=isWindows)
    print(process)
    
def main():
    run_sever();

if __name__ == '__main__':
    main()