from login.models import Student
from notification.models import Notification
from rest_framework import status
from rest_framework.response import Response
from rest_framework.serializers import ModelSerializer
from rest_framework.views import APIView


class NotificationSerializer(ModelSerializer):
    class Meta:
        model = Notification
        fields = ('id', 'source', 'target', 'type', 'date', 'data')


class NotificationsView(APIView):
    def get(self, request, **kwargs):
        if request.user.is_anonymous():
            return Response(status.HTTP_401_UNAUTHORIZED)
        student = Student.objects.get(user=request.user)
        notifications = Notification.objects.get(target=student)[:20]
        serializer = NotificationSerializer(notifications, many=True)
        return Response(serializer.data)