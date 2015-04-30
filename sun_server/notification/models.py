from django.db import models
from django.contrib.auth.models import User


class Notification(models.Model):
    source = models.ForeignKey(User)
    target = models.ForeignKey('login.Student')
    type = models.CharField(max_length=30)
    date = models.DateTimeField(auto_now_add=True)
    data = models.TextField()
