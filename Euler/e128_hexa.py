#! /usr/bin/env python

'''----------------------------------------------------------------------+ 
 |                             e128_hexa.py                              |
 |                                                                       |
 | Author: Nic H.                                                        |
 | Date: 2015-Oct-23                                                     |
 +----------------------------------------------------------------------'''

def c(i):
    if i == 0:
        return 1
    i -= 1
    return 3*i*i + 3*i + 2

def isPrime(n):
    if n<2:
        return False
    i=2
    while i*i <= n:
        if n%i == 0:
            return False
        i += 1
    return True

def chk(l, ns):
    ns = map(lambda x: abs(x-l), ns)
    ns2 = [i for i in ns if isPrime(i)]
    return len(ns2) == 3

i = 1
cur = 1
print 1, 1
ans = 0
while cur <= 2000:
    if chk(c(i), [c(i+1), c(i-1), c(i+1)+1, c(i+1)-1, c(i)+1, c(i+2)-1]):
        cur += 1
        print cur, c(i)
        if cur == 2000:
            ans = c(i)
    if chk(c(i+1)-1, [c(i+2)-1, c(i), c(i-1), c(i)-1, c(i+1)-2, c(i+2)-2]):
        cur += 1
        print cur, c(i+1)-1
        if cur == 2000:
            ans = c(i+1)-1
    i += 1

print "\n%d" % ans
