# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('login', '0002_auto_20150423_1558'),
    ]

    operations = [
        migrations.AddField(
            model_name='student',
            name='friends',
            field=models.ManyToManyField(to='login.Student'),
        ),
    ]
