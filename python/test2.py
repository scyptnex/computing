
import os
import sys

bindir = os.path.abspath(os.path.join(os.path.dirname(sys.argv[0]), "../bin"))
batdir = os.path.abspath(os.path.join(os.path.dirname(sys.argv[0]), "../batch"))

def batchlink(fpath):
    print(fpath, os.path.relpath(fpath, batdir))

for path,dirs,files in os.walk(bindir):
    for f in files:
        batchlink(os.path.join(path,f))
