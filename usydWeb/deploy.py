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
from shutil import copyfile

MARKDOWN_MATCHER = re.compile("\.mdown$");
PRIVELAGE_DIRS = 0711 #stat.S_IRWXU | stat.S_IXGRP | stat.S_IXOTH
PRIVELAGE_FILS = 0744 #stat.S_IRWXU | stat.S_IRGRP | stat.S_IROTH
HOME_NAME = "home"
HTML_SRC = "./in"
HTML_DST = os.path.expanduser("~") + "/lib/html"
HTML_END = "html"
CSS_SRC = "bootstrapmod.css"

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
    return os.path.join(outDir, os.path.split(inp)[1][:-5] + HTML_END)

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
    return str(ret)

def _getNav(curDir):
    ret = {}
    for entry in os.listdir(curDir):
        fil = os.path.join(curDir, entry)
        if MARKDOWN_MATCHER.search(fil):
            ret[entry] = str(fil)[:-5] + HTML_END
        elif os.path.isdir(fil):
            ret[entry] = _getNav(fil)
    return ret

def getNav():
    return {HOME_NAME : _getNav(HTML_SRC)}

def sunderPath(path):
    """Return a reverse list of all the path entries"""
    ret = []
    while True:
        h, t = os.path.split(path)
        if t:
            ret.append(t)
        if not h:
            break
        path = h
    return ret

def findRelativePath(fromP, toP):
    f = sunderPath(fromP)
    t = sunderPath(toP)
    while len(f) > 0 and len(t) > 0 and f[-1] == t[-1]:
        f.pop()
        t.pop()
    if not f and not t:
        return None
    ret = ""
    while t:
        ret = os.path.join(ret, t.pop())
    for i in xrange(1, len(f)):
        ret = os.path.join("..", ret)
    return ret

def writeNav(pretty, nav, pagePath):
    pretty.write("<ul class=\"nav nav-list\">")
    for lin in sorted(nav.keys()):
        if lin == "index.mdown":
            pass
        elif isinstance(nav[lin], str):
            targ = findRelativePath(pagePath, nav[lin])
            name = pageName(lin)
            if targ:
                pretty.write("<li><a href=\"%s\">%s</a></li>" % (targ, name))
            else:
                pretty.write("<li class=\"active\"><a href=\"\">%s</a></li>" % name)
        else:
            if nav[lin].has_key("index.mdown"):
                if lin == HOME_NAME:
                    pretty.write("<li class=\"nav-header\">Navigation</li>")
                targ = findRelativePath(pagePath, nav[lin]["index.mdown"])
                if targ:
                    pretty.write("<li><a href=\"%s\">%s</a></li>" % (targ, lin))
                else:
                    pretty.write("<li class=\"active\"><a href=\"\">%s</a></li>" % lin)
            else:
                pretty.write("<li>%s</li>" % lin)
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
    page.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
    page.write("<link rel=\"stylesheet\" href=\"%s\" />" % findRelativePath(inp, os.path.join(HTML_SRC, CSS_SRC)))
    page.write("</head>")
    page.write("<body>")
    page.write("<div class=\"container-fluid\">")
    page.write("<div class=\"row-fluid\">")
    #nav
    page.write("")
    page.write("<div class=\"span2 offset1\">")
    page.write("<span class=\"h1\">&nbsp;</span> <!-- Hack to put whitespace above the nav -->")
    page.write("<div class=\"well sidebar-nav\">")
    writeNav(page, getNav(), inp[:-5] + HTML_END)
    page.write("</div>") # well sidebar-nav
    page.write("</div>") # span2 offset1
    #content
    page.write("<div class=\"span6\">")
    proc = subprocess.Popen(["./Markdown.pl", str(inp)], stdout=subprocess.PIPE, close_fds=True)
    page.writeAll(proc.stdout.readlines())
    page.write("</div>") # span6
    #finals
    page.write("</div>") # row-fluid
    page.write("</div>") # container-fluid
    page.write("</body>")
    page.write("</html>")
    page.close()
    os.chmod(outp, PRIVELAGE_FILS);

def makeDirSmart(path):
    if(not os.path.isdir(path)):
        os.makedirs(path)
        os.chmod(path, PRIVELAGE_DIRS)

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
        css = os.path.join(HTML_DST, CSS_SRC)
        copyfile(CSS_SRC, css)
        os.chmod(css, PRIVELAGE_FILS);

if __name__ == "__main__":
    deploy()
