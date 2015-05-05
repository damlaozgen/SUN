from rest_framework import status
from rest_framework import mixins
from rest_framework.decorators import detail_route
from rest_framework.fields import SerializerMethodField
from rest_framework.response import Response
from rest_framework.serializers import HyperlinkedModelSerializer
from rest_framework.serializers import ModelSerializer
from rest_framework.serializers import HyperlinkedIdentityField
from rest_framework.viewsets import ModelViewSet
from rest_framework.viewsets import GenericViewSet
from rest_framework.mixins import ListModelMixin
from rest_framework.mixins import RetrieveModelMixin
from rest_framework.views import APIView
from event.models import Event
from event.models import Joinable
from login.models import Student
from student_api import LiteStudentSerializer
from student_api import StudentSerializer


class LiteJoinableSerializer(ModelSerializer):
    class Meta:
        model = Joinable
        fields = ('id', 'type', 'name', 'info')


class JoinableDetailSerializer(ModelSerializer):
    events = HyperlinkedIdentityField(view_name='joinable-events',
                                      lookup_field='id')

    class Meta:
        model = Joinable
        fields = ('id', 'type', 'name', 'info', 'events')


class EventSerializer(HyperlinkedModelSerializer):
    students = LiteStudentSerializer(many=True)
    joinable = LiteJoinableSerializer()
    creator = SerializerMethodField()

    class Meta:
        model = Event
        fields = ('id', 'creator', 'name', 'info', 'date', 'students', 'joinable')

    def get_creator(self, object):
        return object.owner.pk


class EventViewSet(ModelViewSet):
    serializer_class = EventSerializer

    def get_queryset(self):
        if self.request.user.is_authenticated():
            print dir(Student.objects.get(user=self.request.user))
            return Student.objects.get(user=self.request.user).joined_events
        return None

    def create(self, request, **kwargs):
        if request.user.is_anonymous():
            return Response(status=status.HTTP_401_UNAUTHORIZED)
        joinable = Joinable.objects.get(pk=request.data['joinable'])
        event = Event(joinable=joinable, name=request.data['name'], date=request.data['date'], info=request.data['info'])
        event.owner = request.user
        try:
            student = Student.objects.get(user=request.user)
            event.students.add(student)
        except:
            pass
        event.save()
        return Response("OK", status=status.HTTP_201_CREATED)

    @detail_route(methods=['get'])
    def check_in(self, request, pk):
        try:
            student = Student.objects.get(user=request.user)
            event = Event.objects.get(pk=pk)
        except:
            return Response('Only students can check-in', status=status.HTTP_400_BAD_REQUEST)

    @detail_route(methods=['get'])
    def join(self, request, pk):
        try:
            student = Student.objects.get(user=request.user)
            try:
                event = Event.objects.get(pk=pk)
                if student in event.students:
                    return Response('You already joined this event', status=status.HTTP_400_BAD_REQUEST)
                event.students.add(student)
                event.save()
                return Response(status=status.HTTP_200_OK)
            except Exception as e:
                return Response('An error occured: '+str(e), status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        except:
            return Response('Only students can join events', status=status.HTTP_400_BAD_REQUEST)


    @detail_route(methods=['get'])
    def leave(self, request, pk):
        try:
            student = Student.objects.get(user=request.user)
            event = Event.objects.get(pk=pk)
            if student not in event.students:
                return Response('You are not joined to the event', status=status.HTTP_400_BAD_REQUEST)
            event.students.remove(student)
            event.save()
            return Response(status=status.HTTP_200_OK)
        except:
            return Response('Only students can leave events', status=status.HTTP_400_BAD_REQUEST)


class JoinableViewSet(GenericViewSet, ListModelMixin, RetrieveModelMixin):
    queryset = Joinable.objects.all()
    serializer_class = JoinableDetailSerializer


class JoinableEventListView(APIView):
    def get(self, request, id, format=None):
        joinable = Joinable.objects.get(pk=id)
        events = Event.objects.filter(joinable=joinable)
        serializer = EventSerializer(events, many=True)
        return Response(serializer.data)


class StudentEventsView(APIView):
    def get(self, request, **kwargs):
        events = Student.objects.get(pk=kwargs['id']).joined_events
        serializer = EventSerializer(events, many=True, context={'request': request})
        return Response(serializer.data)
