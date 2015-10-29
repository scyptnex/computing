#! /usr/bin/env python

'''----------------------------------------------------------------------+ 
 |                            e113_bouncy.py                             |
 |                                                                       |
 | Author: Nic H.                                                        |
 | Date: 2015-Oct-27                                                     |
 +----------------------------------------------------------------------'''

def bf(rng):
    count = 0
    for i in xrange(1, rng):
        if i == int("".join(sorted(str(i)))) or i == int("".join(sorted(str(i))[::-1])):
            count += 1
    return count

def inc(k):
    if k == 1:
        return [1,1,1,1,1,1,1,1,1,1]
    sub = inc(k-1)
    ret = [reduce(lambda x, y: x+y, sub[i:]) for i in xrange(0, 10)]
    ret[0] = 0
    return ret

def dec(k):
    if k == 1:
        return [1,1,1,1,1,1,1,1,1,1]
    sub = dec(k-1)
    sub[0] = 0
    ret = [reduce(lambda x, y: x+y, sub[:i+1])+1 for i in xrange(0, 10)]
    ret[0] = 0
    return ret

def nonbouncy(digis):
    ret = reduce(lambda x, y: x+y, inc(digis)) + reduce(lambda x, y: x+y, dec(digis))
    if digis == 1:
        return ret-11
    return ret-9

def totnb(digis):
    return reduce(lambda x, y:x+y, [nonbouncy(i) for i in xrange(1, digis+1)])

print bf(10)
print bf(100)
print bf(1000)
print bf(10000)
print totnb(1)
print totnb(2)
print totnb(3)
print totnb(4)
print totnb(6)
print totnb(10)
print
print totnb(100)

