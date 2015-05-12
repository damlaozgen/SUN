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
from event.models import Event, Location
from event.models import Joinable
from login.models import Student
from student_api import LiteStudentSerializer
from student_api import StudentSerializer
import datetime
from event.models import CheckIn


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
    location_id = SerializerMethodField()

    class Meta:
        model = Event
        fields = ('id', 'creator', 'name', 'info', 'date', 'students', 'joinable', 'location_id')

    def get_creator(self, object):
        return object.owner.pk

    def get_location_id(self, object):
        return object.location.pk


class LocationSerializer(ModelSerializer):
    class Meta:
        model = Location
        fields = ('id', 'name')


class LocationListView(APIView):
    def get(self, *args, **kwargs):
        locations = Location.objects.all()
        serializer = LocationSerializer(locations, many=True)
        return Response(serializer.data)


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
        if 'location' in request.POST:
            location = Location.objects.get(pk=request.POST['location'])
            event.location = location

        event.owner = request.user
        event.save()
        event.students.add(Student.objects.get(user=request.user))

        return Response("OK", status=status.HTTP_201_CREATED)

    @detail_route(methods=['get'])
    def join(self, request, pk):
        try:
            student = Student.objects.get(user=request.user)
            try:
                event = Event.objects.get(pk=pk)
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


class CheckinView(APIView):
    def post(self, request, **kwargs):
        try:
            student = Student.objects.get(user=request.user)
        except:
            return Response("Only logged in students can check-in", status=status.HTTP_400_BAD_REQUEST)
        try:
            location = Location.objects.get(token=request.POST['location_id'])
        except:
            return Response("Unknown location", status=status.HTTP_400_BAD_REQUEST)
        now_min30 = datetime.datetime.now() - datetime.timedelta(minutes=30)
        now_plus30 = datetime.datetime.now() + datetime.timedelta(minutes=30)
        possible_events = student.joined_events.filter(date__gt=now_min30).filter(date__lt=now_plus30)
        try:
            event = possible_events[0]
        except:
            return Response("There is no event that you can check in", status=status.HTTP_400_BAD_REQUEST)
        if CheckIn.objects.filter(event=event, student=student).count() > 0:
            return Response("You already checked in to the event", status=status.HTTP_400_BAD_REQUEST)
        checkin = CheckIn(event=event, student=student, rewarded_points=100)
        checkin.save()
        student.points += 100
        student.save()
        return Response("OK")

