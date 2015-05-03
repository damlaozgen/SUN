# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('event', '0002_auto_20150423_1558'),
    ]

    operations = [
        migrations.AlterField(
            model_name='event',
            name='students',
            field=models.ManyToManyField(related_name='joined_events', null=True, to='login.Student', blank=True),
        ),
    ]
