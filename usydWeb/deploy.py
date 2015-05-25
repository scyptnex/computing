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

MARKDOWN_MATCHER = re.compile("\.mdown$");
PRIVELAGE_DIRS = stat.S_IRWXU | stat.S_IXGRP | stat.S_IXOTH
PRIVELAGE_FILS = stat.S_IRWXU | stat.S_IRGRP | stat.S_IROTH
HOME_NAME = "home"
HTML_SRC = "./in"
HTML_DST = os.path.expanduser("~") + "/lib/html"

class htmlPretty:
    def __init__(self, fi, tab):
        self.tabs = str(tab)
        self.curTab = 0
        self.f = open(fi, "w")

    def writeAll(self, lines):
        for line in lines:
            self.write(line)

    def write(self, line):
        self.f.write(line.strip() + "\n")

    def close(self):
        self.f.close()

def htmlName(inp, outDir):
    return os.path.join(outDir, os.path.split(inp)[1][:-5] + "html")

def pageName(inp):
    path, name = os.path.split(inp)
    ret = path
    if name == "index.mdown":
        if path == HTML_SRC:
            ret = HOME_NAME
        else:
            ret = str(os.path.split(path)[-1])
    else:
        ret = name[:-6]
    return str(ret[0:1]).upper() + str(ret[1:])

def _getNav(curDir):
    ret = {}
    for entry in os.listdir(curDir):
        fil = os.path.join(curDir, entry)
        if MARKDOWN_MATCHER.search(fil):
            ret[pageName(fil)] = "index.html"
        elif os.path.isdir(fil):
            ret[pageName(os.path.join(fil, "index.mdown"))] = _getNav(fil)
    return ret

def getNav():
    return {HOME_NAME : _getNav(HTML_SRC)}

def writeNav(pretty, nav, pagePath):
    pretty.write("<ul>")
    for lin in sorted(nav.keys()):
        pretty.write("<li>%s</li>" % lin)
        if not isinstance(nav[lin], str):
            writeNav(pretty, nav[lin], pagePath)
    pretty.write("</ul>")

def makeHtml(inp, outDir):
    outp = htmlName(inp, outDir)
    page = htmlPretty(outp, "  ")
    #headers
    page.write("<!DOCTYPE html>")
    page.write("<html>")
    page.write("<head>")
    page.write("<title>%s</title>" % pageName(inp))
    page.write("</head>")
    page.write("<body>")
    #nav
    page.write("<div class=\"nav\">")
    writeNav(page, getNav(), inp)
    page.write("</div>")
    #content
    page.write("<div class=\"content\">")
    proc = subprocess.Popen(["./Markdown.pl", str(inp)], stdout=subprocess.PIPE, close_fds=True)
    page.writeAll(proc.stdout.readlines())
    page.write("</div>")
    #finals
    page.write("</body>")
    page.write("</html>")
    page.close()

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
    global HTML_SRC, HTML_DST
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
            HTML_SRC = a
        elif o in ("-d"):
            HTML_DST = a
    if (not os.path.isdir(HTML_SRC)):
        print "Cannot locate input directory:", HTML_SRC
        sys.exit(3)
    elif (not os.path.isdir(HTML_DST)):
        print "Cannot locate output directory:", HTML_DST
        sys.exit(4)
    else:
        generate(HTML_SRC, HTML_DST)

if __name__ == "__main__":
    deploy()
