#! /usr/bin/env python

import sys
import base64
import json
import hmac
import hashlib

code = """utFPpYmnPEUO7xi934G7J9drYjkoMjuxtqRnBGJ8gvI.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImNvZGUiOiJBUURpWENxblZNWW5YWkp2eDkyWDdHRmlTUnZhM3JzS2VZdW1LS3k3eFd4b2JZdHpsaEYwcW8tSWdBX1c5U09aUnlwZ3NFbjJOQmJPSnhIRDRrOFNvTUo1NjVTTjRleFNFMUhqR3ZuSnl3TTFiTTVacUwzMTVPZHVzcVktUm1sa3VHekdyUGZPblNkUjU4dTB3OVF4VV9ubC1DVnY3OUZ4THhnM2ttN3dGckh1SEhEaDRxbWtlZVBLNnNhcU5LNDhPdjA4NFNpN0lHeC1valJEYXdJc21uencyYkZBU3Nhdm9yZVBRNUkxWENOTGljazQ0R2NSRGNjUVZ0LUljbmtQOEpsSWo4N05kZnRMVUhMR2YxNlZZZzd3YjJmdDdSbEpic05FdlhGWDFER2ZidUZvYzVXVW5DNkw0dTNHcmItWENvNGNGMFI1OTlzVzRkYTBNTkdIVDBpSCIsImlzc3VlZF9hdCI6MTQzNDU5OTQ4OCwidXNlcl9pZCI6IjE0MDgwNjI5NDI4NTY1NTEifQ"""

def fbdecode(data):
    data += '=' * (4 - len(data)%4)
    return base64.urlsafe_b64decode(data.encode('utf-8'))

def parse(val):
    (check, payload) = val.split(".")

    secret = "7b8396744fecb8b8008a4a5395e1d730"

    print check
    print json.loads(fbdecode(payload))

    print hmac.new(secret, payload, hashlib.sha256).digest() == fbdecode(check)

    #print fbdecode(check)
    #dec = json.JSONDecoder()
    #deco = dec.decode(fbdecode(payload))
    #for k in deco.keys():
        #print str(k) + " - " + str(deco[k])
    #print deco["code"]
    #dd = fbdecode(str(deco["code"]))
    #print dd

parse(code)
