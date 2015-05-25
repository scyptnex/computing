#! /usr/bin/env python

'''----------------------------------------------------------------------+ 
 |                               deploy.py                               |
 |                                                                       |
 | Author: Nic H.                                                        |
 | Date: 2015-May-22                                                     |
 |                                                                       |
 | Deploy the usydweb website to lib/html directory, Python version      |
 +----------------------------------------------------------------------'''

__doc__ = """deploy
This is the Docstring"""

import sys
import os
import getopt
import re
import stat
import subprocess

TAB = "  "
MARKDOWN_MATCHER = re.compile("\.mdown$");
PRIVELAGE_DIRS = stat.S_IRWXU | stat.S_IXGRP | stat.S_IXOTH
PRIVELAGE_FILS = stat.S_IRWXU | stat.S_IRGRP | stat.S_IROTH

def htmlName(inp, outDir):
    return os.path.join(outDir, os.path.split(inp)[1][:-5] + "html")

def makeHtml(inp, outDir):
    outp = htmlName(inp, outDir)
    fi = open(outp, "w")
    fi.writelines(["<!DOCTYPE html>\n", "<html>\n", TAB + "<body>\n"])
    proc = subprocess.Popen(["./Markdown.pl", str(inp)], stdout=subprocess.PIPE, close_fds=True)
    tbs = 2
    for line in proc.stdout.readlines():
        fi.write(TAB*tbs + line)
    fi.writelines([TAB + "</body>\n", "</html>\n"])
    fi.close()

def makeDirSmart(path):
    if(not os.path.isdir(path)):
        os.makedirs(path, mode=PRIVELAGE_DIRS)

def generate(src, dst):
    for entry in os.listdir(src):
        inFi = os.path.join(src, entry);
        if os.path.isdir(inFi):
            outFi = os.path.join(dst, entry)
            makeDirSmart(outFi)
            generate(inFi, outFi)
        elif MARKDOWN_MATCHER.search(inFi):
            makeHtml(inFi, dst)

def deploy():
    source = "./in"
    dest = os.path.expanduser("~") + "/lib/html"
    try:
        opts, args = getopt.getopt(sys.argv[1:], "hs:d:", ["help"])
    except getopt.error, msg:
        print msg
        print "for help use --help"
        sys.exit(2)
    for o, a in opts:
        if o in ("-h", "--help"):
            print __doc__
            sys.exit(0)
        elif o in ("-s"):
            source = a
        elif o in ("-d"):
            dest = a
    if (not os.path.isdir(source)):
        print "Cannot locate input directory:", source
        sys.exit(3)
    elif (not os.path.isdir(dest)):
        print "Cannot locate output directory:", dest
        sys.exit(4)
    else:
        generate(source, dest)

if __name__ == "__main__":
    deploy()
