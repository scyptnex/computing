#!/usr/bin/python

import sys

argc = len(sys.argv)
print "Passed %i args to script %s" % (argc-1, sys.argv[0])
for i in xrange(1,argc):
	print "\t[%2i] - %s" % (i, sys.argv[i])
