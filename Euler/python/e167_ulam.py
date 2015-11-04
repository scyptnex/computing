#! /usr/bin/env python

'''----------------------------------------------------------------------+ 
 |                             e167_ulam.py                              |
 |                                                                       |
 | Author: Nic H.                                                        |
 | Date: 2015-Oct-27                                                     |
 +----------------------------------------------------------------------'''
def progress(ulam, sml, big):
    bi = 0
    while ulam[-1]-ulam[-2] != big:
        nxt = ulam[-1]+sml
        while big + ulam[bi] < nxt:
            bi += 1
        if big + ulam[bi] == nxt:
            bi += 1
            ulam += [ulam[bi] + big]
        else:
            ulam += [nxt]
    return ulam

def findPeriodStart(ulam):
    sml = ulam[0];
    big = ulam[1]*2 + ulam[0];
    ulam = ulam + [i for i in xrange(ulam[1]+sml, big, sml)] + [big, big+1]
    return progress(ulam, sml, big)

def findPeriodCycle(ulam):
    sml = ulam[0]
    big = ulam[1]*2 + ulam[0]
    return progress([0, sml], sml, big)


def hugeu(ulam, huge):
    cycl = findPeriodCycle(ulam)[1:]
    ulam = findPeriodStart(ulam)
    if huge < len(ulam):
        return ulam[huge]
    huge -= len(ulam)
    print len(ulam)#, ulam
    print len(cycl)#, cycl
    return ulam[-1] + cycl[-1]*(huge/len(cycl)) + cycl[huge%len(cycl)]

summ = 0
for i in xrange(2, 11):
    num = hugeu([2, 2*i + 1], 10**11)
    print [2, 2*i + 1], num
    summ += num
print summ
