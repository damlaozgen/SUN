from django.contrib import admin
from models import Event, Location
from models import Joinable


@admin.register(Event)
class EventAdmin(admin.ModelAdmin):
    pass


@admin.register(Joinable)
class JoinableAdmin(admin.ModelAdmin):
    pass


@admin.register(Location)
class LocationAdmin(admin.ModelAdmin):
    pass