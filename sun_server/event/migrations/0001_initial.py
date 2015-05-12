# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
        ('login', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='CheckIn',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('date', models.DateTimeField(auto_now_add=True)),
                ('rewarded_points', models.PositiveSmallIntegerField()),
            ],
        ),
        migrations.CreateModel(
            name='Event',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=50)),
                ('date', models.DateTimeField()),
                ('info', models.TextField()),
            ],
        ),
        migrations.CreateModel(
            name='Joinable',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('type', models.CharField(max_length=2, choices=[(b'H', b'Hobby'), (b'SC', b'StudentClub'), (b'C', b'Class')])),
                ('name', models.CharField(max_length=50)),
                ('info', models.TextField()),
            ],
        ),
        migrations.CreateModel(
            name='StudentClub',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('joinable', models.ForeignKey(to='event.Joinable')),
            ],
        ),
        migrations.AddField(
            model_name='event',
            name='joinable',
            field=models.ForeignKey(to='event.Joinable'),
        ),
        migrations.AddField(
            model_name='event',
            name='owner',
            field=models.ForeignKey(blank=True, to=settings.AUTH_USER_MODEL, null=True),
        ),
        migrations.AddField(
            model_name='event',
            name='students',
            field=models.ManyToManyField(to='login.Student'),
        ),
        migrations.AddField(
            model_name='checkin',
            name='event',
            field=models.ForeignKey(to='event.Event'),
        ),
        migrations.AddField(
            model_name='checkin',
            name='student',
            field=models.ForeignKey(to='login.Student'),
        ),
    ]
