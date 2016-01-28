def rise(h):
    cur = 0
    done = 0
    rise = 2
    while done < h:
        while cur+2 < h and rise < 3:
            cur += 1
            rise += 1
        rise = 0
        print cur, # do the dig
        done = cur+2
    print ""

for i in xrange(1, 12):
    rise(i)
