"""
+-------------------------------------------------------------------------+
|                               e107_mst.py                               |
|                                                                         |
| Author: nic                                                             |
| Date: 2017-Feb-11                                                       |
+-------------------------------------------------------------------------+
"""

__doc__ = __doc__.strip()

import eul.filer as filer
import eul.ds as ds

adjacency = [[int(a) if a != "-" else -1 for a in ln] for ln in filer.stream_delimited_lines("E107.txt", ",")]
vs = len(adjacency)
connected = ds.disjoint_set(vs)
edjs = []
total = 0
for s in xrange(vs):
    for t in xrange(s):
        if adjacency[s][t] != -1:
            edjs.append((adjacency[s][t], s, t))
            total += adjacency[s][t]
edjs = sorted(edjs)
mstc = 0
for (w, s, t) in edjs:
    if connected.find(s) != connected.find(t):
        mstc += w
        connected.union(s, t)

print total-mstc
