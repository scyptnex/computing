
import httplib2
from apiclient import discovery
from oauth2client.client import OAuth2WebServerFlow
from oauth2client.file import Storage
from oauth2client import tools

storage = Storage('creds.json')

creds = storage.get()
if not creds :
    flow = OAuth2WebServerFlow(
            client_id='554757268226-r86ua14ce182icaga3ppa8e7q3i261r4.apps.googleusercontent.com',
            client_secret='E_N-5Qa3nMCFtIep00TswwjT',
            scope='https://www.googleapis.com/auth/calendar')
            #scope='https://www.googleapis.com/auth/drive.metadata.readonly')
    creds = tools.run_flow(flow, storage)

print creds
print creds.access_token

http_auth = creds.authorize(httplib2.Http())

drive_service = discovery.build('calendar', 'v3', http_auth)
calendars = drive_service.calendarList().list().execute()
for c in calendars['items']:
    print c['accessRole']
