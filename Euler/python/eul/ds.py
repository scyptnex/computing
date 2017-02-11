"""
+-------------------------------------------------------------------------+
|                                  ds.py                                  |
|                                                                         |
| Author: nic                                                             |
| Date: 2017-Feb-11                                                       |
+-------------------------------------------------------------------------+
"""

__doc__ = __doc__.strip()

class disjoint_set:
    """the standard issue disjoint set data structure"""
    def __init__(self, length):
        self._p = [i for i in xrange(length)]
        self._r = [0]*length

    def union(self, x, y):
        """make x and y part of the same group"""
        xr = self.find(x)
        yr = self.find(y)
        if xr == yr:
            return
        if self._r[xr] < self._r[yr]:
            self._p[xr] = yr
        elif self._r[xr] > self._r[yr]:
            self._p[yr] = xr
        else:
            self._p[yr] = xr
            self._r[xr] += 1

    def find(self, i):
        """return the representative parent for this index (does path compression)"""
        if self._p[i] != i:
            self._p[i] = self.find(self._p[i])
        return self._p[i]

    def __str__(self):
        """its .toString() ..."""
        groups = {}
        for i in xrange(len(self._p)):
            p = self.find(i)
            if not groups.has_key(p):
                groups[p] = []
            groups[p].append(i)
        return "[" + "|".join(",".join([str(v) for v in groups[k]]) for k in groups.keys()) + "]"
