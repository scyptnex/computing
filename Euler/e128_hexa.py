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
    print "ip", n
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
    ns = [i for i in ns if isPrime(i)]
    print ns

i = 1
cur = 0
while cur < 3:
    if chk(c(i), [c(i+1), c(i-1), c(i+1)+1, c(i+1)-1, c(i)+1, c(i+2)-1]):
        print i
        cur += 1
    if chk(c(i+1)-1, [c(i), c(i+2)-1, c(i+2)-2, c(i+1)-2, ]):
        print i
        cur += 1
    i += 1
