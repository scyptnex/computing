"""
+-------------------------------------------------------------------------+
|                           e103_specsubsum.py                            |
|                                                                         |
| Author: nic                                                             |
| Date: 2017-Feb-11                                                       |
+-------------------------------------------------------------------------+
"""

__doc__ = __doc__.strip()

def incr(arr, low, hi):
    i = 0
    arr[i] += 1
    while arr[i] > hi:
        arr[i] = low
        i += 1
        arr[i] += 1

def partitionize(cur, parts):
    a=[]
    b=[]
    for i, x in enumerate(parts):
        if x == 1:
            a.append(cur[i])
        elif x == 2:
            if not a:
                return None
            else:
                b.append(cur[i])
    if not a or not b:
        return None
    return (a, b)

def passes(cur):
    partition = [0 for i in cur]
    bound = len(partition)*2
    while sum(partition) < bound:
        incr(partition, 0, 2)
        prts = partitionize(cur, partition)
        if prts:
            (b, c) = prts
            sb = sum(b)
            sc = sum(c)
            if sb == sc:
                return False
            if len(b) > len(c) and sb <= sc:
                return False
            if len(c) > len(b) and sc <= sb:
                return False
    return True

#ss = [11, 17, 20, 22, 23, 24]
ss = [20, 31, 38, 39, 40, 42, 45]
perturb = [-5 for i in ss]

best = ss if passes(ss) else [99999 for i in ss]
count = 0
while sum(perturb) < len(ss)*5:
    incr(perturb, -5, 5)
    cur = [ss[i] + perturb[i] for i in xrange(0, len(ss))]
    if sum(cur) >= sum(best):
        continue
    brk = False
    for i in xrange(0, len(ss)-1):
        if cur[i] > cur[i+1]:
            brk = True
            break
    if brk:
        continue
    if passes(cur):
        best = cur
    if count%10000 == 0:
        print perturb, count, best
    count += 1
print best
print sum(best)
print "".join([str(i) for i in best])

