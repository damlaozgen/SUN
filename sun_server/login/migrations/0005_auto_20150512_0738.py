# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('login', '0004_student_interests'),
    ]

    operations = [
        migrations.AddField(
            model_name='student',
            name='points',
            field=models.IntegerField(default=0),
        ),
    ]
