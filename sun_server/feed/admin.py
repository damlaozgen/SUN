from django.contrib import admin
from feed.models import News


@admin.register(News)
class NewsAdmin(admin.ModelAdmin):
    pass
