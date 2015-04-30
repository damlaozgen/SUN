from django.contrib import admin
from models import Event
from models import Joinable


@admin.register(Event)
class EventAdmin(admin.ModelAdmin):
    pass


@admin.register(Joinable)
class JoinableAdmin(admin.ModelAdmin):
    pass