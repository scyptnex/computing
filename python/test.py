#!/usr/bin/env python

import sys

argc = len(sys.argv)
print ("Passed %i args to script %s" % (argc-1, sys.argv[0]))
for i in range(1,argc):
	print( "\t[%2i] - %s" % (i, sys.argv[i]))

print( ''' super triple
commented thingo !#excape \0 fail

manycommas''',)
print("neat trick")
