# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('event', '0003_auto_20150503_1428'),
        ('login', '0003_student_friends'),
    ]

    operations = [
        migrations.AddField(
            model_name='student',
            name='interests',
            field=models.ManyToManyField(to='event.Joinable'),
        ),
    ]
