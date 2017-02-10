"""
+-------------------------------------------------------------------------+
|                                filer.py                                 |
|                                                                         |
| Author: nic                                                             |
| Date: 2017-Feb-10                                                       |
+-------------------------------------------------------------------------+
"""

__doc__ = __doc__.strip()

def locate(name):
    """Returns the expected location of the file (based on the location of this script)"""
    from os import path
    return path.realpath(path.join(path.join(path.join(path.dirname(__file__), path.pardir), path.pardir), name))

def stream_lines(name):
    """Generate all the lines (without trainling newline) in this file"""
    with open(locate(name)) as fil:
        for ln in fil.readlines():
            yield ln[:-1]

def stream_delimited_lines(name, delimiter):
    """Generate and split all the lines in the file"""
    for ln in stream_lines(name):
        yield ln.split(delimiter)
