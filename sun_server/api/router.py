from django.conf.urls import url
from rest_framework import routers
from rest_framework.authtoken.views import obtain_auth_token
from event_api import EventViewSet
from event_api import JoinableViewSet
from event_api import JoinableEventListView
from login_api import StudentSearchView
from feed_api import NewsFeedView
from notification_api import NotificationsView


def get_urls():
    router_v1 = routers.DefaultRouter()
    router_v1.register(r'/event', EventViewSet, base_name='event')
    router_v1.register(r'/joinable', JoinableViewSet, base_name='joinable')
    urls = router_v1.urls
    urls.append(url(r'^/joinable/(?P<id>[^/.]+)/events', JoinableEventListView.as_view(), name='joinable-events'))
    urls.append(url(r'^/student/search/(?P<keyword>[^/.]+)', StudentSearchView.as_view(), name='search_student'))
    urls.append(url(r'^/news', NewsFeedView.as_view(), name='news-feed'))
    urls.append(url(r'^/notifications', NotificationsView.as_view(), name='notifications'))
    return urls


def get_login_view():
    return obtain_auth_token