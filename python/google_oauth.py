#============================================================================#
#                                   Oauth                                    #
#                                                                            #
# Author: nic                                                                #
# Date: 2017-Apr-29                                                          #
#============================================================================#

import re
import httplib2
import datetime
from apiclient import discovery
from oauth2client.client import OAuth2WebServerFlow
from oauth2client.file import Storage
from oauth2client import tools

def make_service():
    # Get the creds
    storage = Storage('/tmp/google_calendar_creds.json')
    creds = storage.get()
    if not creds or creds.access_token_expired:
        flow = OAuth2WebServerFlow(
            client_id='554757268226-r86ua14ce182icaga3ppa8e7q3i261r4.apps.googleusercontent.com',
            client_secret='E_N-5Qa3nMCFtIep00TswwjT',
            scope='https://www.googleapis.com/auth/calendar')
        creds = tools.run_flow(flow, storage)
    print "Credentials:", creds.access_token
    # Authorize and build the service
    http_auth = creds.authorize(httplib2.Http())
    return discovery.build('calendar', 'v3', http_auth)

def get_calendar_id(service, name):
    if not name:
        return 'primary'
    calendars = service.calendarList().list().execute()
    mat = re.compile(name)
    matches={}
    for c in calendars['items']:
        if mat.search(c['summary']):
            matches[c['summary']] = c['id']
    if len(matches) == 1:
        return matches.items()[0][1]
    else:
        raise Exception("Multiple calendars match \"%s\": %s" % (name, ", ".join(matches.keys())))

srv = make_service()
cid = get_calendar_id(srv, 'S.*N')

print "---------------------------"
evts = srv.events().list(calendarId=cid,timeMin=datetime.datetime.utcnow().isoformat('T') + "Z").execute()
for e in evts['items']:
    print e['start'], e['end'], e['summary']

new_event = {
        'summary' : 'Wacky2',
        'description': 'Test event',
        'start': {
            'date': '2017-04-30'
        },
        'end': {
            'date': '2017-05-01'
        }
    }

#print srv.events().insert(calendarId=cid,body=new_event).execute()
print
resp = srv.events().quickAdd(calendarId=cid,text='2017-04-28 04:17-15:58 Buy Shoes: this really should go without saying').execute()

for i in resp:
    print i, resp[i]
