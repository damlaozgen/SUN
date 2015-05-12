from django.conf.urls import url
from rest_framework import routers
from rest_framework.authtoken.views import obtain_auth_token
from event_api import EventViewSet
from event_api import JoinableViewSet
from event_api import JoinableEventListView
from event_api import StudentEventsView
from student_api import StudentSearchView
from student_api import StudentViewSet
from student_api import StudentFriendsView
from student_api import StudentInterestsViewSet
from feed_api import NewsFeedView
from notification_api import NotificationsView
from event_api import LocationListView
from event_api import CheckinView


def get_urls():
    router_v1 = routers.DefaultRouter()
    router_v1.register(r'/event', EventViewSet, base_name='event')
    router_v1.register(r'/joinable', JoinableViewSet, base_name='joinable')
    router_v1.register(r'/student', StudentViewSet, base_name='student')
    router_v1.register(r'/student/(?P<id>[^/.]+)/interests', StudentInterestsViewSet, base_name='student-interests')
    urls = router_v1.urls
    urls.append(url(r'^/joinable/(?P<id>[^/.]+)/events', JoinableEventListView.as_view(), name='joinable-events'))
    urls.append(url(r'^/student/(?P<id>[^/.]+)/events', StudentEventsView.as_view(), name='student-events'))
    urls.append(url(r'^/student/(?P<id>[^/.]+)/friends', StudentFriendsView.as_view(), name='student-friends'))
    urls.append(url(r'^/student/search/(?P<keyword>[^/.]+)', StudentSearchView.as_view(), name='search_student'))
    urls.append(url(r'^/news', NewsFeedView.as_view(), name='news-feed'))
    urls.append(url(r'^/notifications', NotificationsView.as_view(), name='notifications'))
    urls.append(url(r'^/locations', LocationListView.as_view(), name='locations'))
    urls.append(url(r'^/checkin', CheckinView.as_view(), name='checkim'))
    return urls


def get_login_view():
    return obtain_auth_token