from django.db import models
from django.contrib.auth.models import User


class FeedItem(models.Model):
    date = models.DateTimeField(auto_now_add=True)
    creator = models.ForeignKey(User)


class News(FeedItem):
    text = models.TextField()