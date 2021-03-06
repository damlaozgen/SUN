from django.db import models
from django.contrib.auth.models import User


class Student(models.Model):
    user = models.ForeignKey(User)
    avatar = models.CharField(max_length=512, null=True, blank=True)
    contact_info = models.TextField(null=True, blank=True)
    friends = models.ManyToManyField('self', symmetrical=False, blank=True)
    interests = models.ManyToManyField('event.Joinable', blank=True)
    points = models.IntegerField(default=0)

    def __str__(self):
        full_name = self.user.get_full_name()
        if not full_name:
            return str(self.user)
        return full_name


class ClubRepresentative(models.Model):
    user = models.ForeignKey(User)
