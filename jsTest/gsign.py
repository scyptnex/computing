#! /usr/bin/env python

import sys
import base64
import json
import hmac
import hashlib
import urllib
import urllib2

code = """eyJhbGciOiJSUzI1NiIsImtpZCI6Ijg2ODcwYTA0ZGNkNzFhYzEyYWM3NDQ1NGE2NmQ4M2RmYWU2NTQ5YjIifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwic3ViIjoiMTEyNDAwMTUwNjc3ODQzNzUwMzU3IiwiYXpwIjoiNTU0NzU3MjY4MjI2LWFtZHJzb2NlY2czaDN0dGprYmIxbjU3ZjQ3bmludGtxLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiYXRfaGFzaCI6InNBV3hiUFBqeGZpWTdyZDVtRWphZnciLCJhdWQiOiI1NTQ3NTcyNjgyMjYtYW1kcnNvY2VjZzNoM3R0amtiYjFuNTdmNDduaW50a3EuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJpYXQiOjE0MzQ2MTA3NzgsImV4cCI6MTQzNDYxNDM3OH0.iFyGzvrQxve2Psf68PNlTWctVEkQGuy-9N78sNAu5zxB8EY1LICeNDxKieJ2Jz_HLwMAX30kRTmFMOkEcFK12MgHF3S20WBS9CHZqL05BJkrY_L_zIBa6YVNRa9kPYGDKLWileNoDqxTol_0n71ET5KCRNqzUAcr14RIuauSnebqNuFVnE9I0W6i0S3v1hHho-Ky3N7DHs8p9QuHqbCPCKYlQQI2rBGlu1zWlC9dFccPYf4Ccdb1Z5PEF0gaO5T98FHK2V4ZpBX-ZrAnwHmbTcTosFIpbmC1taHMtq7f6NjDlz68ykj_VY6abAlvd-IBiWN2xCY6_95DD4Cq1y5HfQ"""

def deco(data):
    data += '=' * (4 - len(data)%4)
    return base64.urlsafe_b64decode(data.encode('utf-8'))

def parse(val):
    data = urllib.urlencode({"id_token" : val})
    req = urllib2.Request("https://www.googleapis.com/oauth2/v3/tokeninfo" + "?" + data)
    try:
        response = urllib2.urlopen(req)
        res = json.loads(response.read())
        print res["aud"]
        print response.info()
        print response.code
    except urllib2.HTTPError as e:
        print e.code
        print e.read()


parse(code)
