from django.db import models
from django.contrib.auth.models import User


class Joinable(models.Model):
    JOINABLE_TYPES = {'Class': 'C', 'StudentClub': 'SC', 'Hobby': 'H'}
    type = models.CharField(max_length=2, choices=tuple(zip(JOINABLE_TYPES.values(),
                                                            JOINABLE_TYPES.keys())))
    name = models.CharField(max_length=50)
    info = models.TextField()

    def __str__(self):
        return self.name


class StudentClub(models.Model):
    joinable = models.ForeignKey(Joinable)


class Event(models.Model):
    joinable = models.ForeignKey(Joinable)
    owner = models.ForeignKey(User, null=True, blank=True)
    name = models.CharField(max_length=50)
    date = models.DateTimeField()
    info = models.TextField()
    students = models.ManyToManyField('login.Student', null=True, blank=True, related_name='joined_events')
    location = models.ForeignKey('Location', null=True, blank=True)


class Location(models.Model):
    name = models.CharField(max_length=255)
    token = models.TextField()


class CheckIn(models.Model):
    student = models.ForeignKey('login.Student')
    event = models.ForeignKey(Event)
    date = models.DateTimeField(auto_now_add=True)
    rewarded_points = models.PositiveSmallIntegerField()