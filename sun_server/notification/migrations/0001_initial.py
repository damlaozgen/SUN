# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        ('login', '0001_initial'),
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Notification',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('type', models.CharField(max_length=30)),
                ('date', models.DateTimeField(auto_now_add=True)),
                ('data', models.TextField()),
                ('source', models.ForeignKey(to=settings.AUTH_USER_MODEL)),
                ('target', models.ForeignKey(to='login.Student')),
            ],
        ),
    ]
