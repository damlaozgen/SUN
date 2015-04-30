from django.conf.urls import include, url
from django.contrib import admin
from api import router as api_router

urlpatterns = [
    # Examples:
    # url(r'^$', 'sun.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    url(r'^api/v1', include(api_router.get_urls())),
    url(r'^api/v1/login', api_router.get_login_view()),
]
