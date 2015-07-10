#
# DJANGO SETUP
#
import os
os.environ['DJANGO_SETTINGS_MODULE'] = "djangotest.settings"
import django
django.setup()

from polls.models import Question, Choice

for q in Question.objects.all():
    print q.question_text, q.pub_date.strftime("%Y-%m-%d")
    for c in q.choice_set.all():
        print "\t", c.votes, c.choice_text

