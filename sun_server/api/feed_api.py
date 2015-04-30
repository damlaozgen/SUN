from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework.serializers import ModelSerializer
from feed.models import News


class NewsSerializer(ModelSerializer):
    class Meta:
        model = News
        fields = ('id', 'date', 'creator', 'text')


class NewsFeedView(APIView):
    def get(self, request, **kwargs):
        news = News.objects.all()[:20]
        serializer = NewsSerializer(news, many=True)
        return Response(serializer.data)