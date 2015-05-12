from django.contrib.auth.models import User
from event.models import Joinable
from rest_framework import mixins
from rest_framework.decorators import detail_route
from rest_framework.relations import HyperlinkedIdentityField
from rest_framework.response import Response
from rest_framework.serializers import HyperlinkedModelSerializer, ModelSerializer
from rest_framework.serializers import SerializerMethodField
from rest_framework.views import APIView
from login.models import Student
from django.db.models import Q
from rest_framework.viewsets import GenericViewSet
from rest_framework import status


class LiteStudentSerializer(HyperlinkedModelSerializer):
    name = SerializerMethodField()

    class Meta:
        model = Student
        fields = ('id', 'avatar', 'name')

    def get_name(self, object, **kwargs):
        return object.user.get_full_name()


class StudentSerializer(HyperlinkedModelSerializer):
    events = HyperlinkedIdentityField(view_name='student-events',
                                      lookup_field='id')
    name = SerializerMethodField()
    email = SerializerMethodField()
    friends = LiteStudentSerializer(many=True)

    class Meta:
        model = Student
        fields = ('id', 'avatar', 'email', 'name', 'contact_info', 'events', 'friends', 'points')

    def get_name(self, object, **kwargs):
        return object.user.get_full_name()

    def get_email(self, object, **kwargs):
        return object.user.email


class StudentSearchView(APIView):
    def get(self, request, keyword, format=None):
        students = Student.objects.filter(Q(user__first_name__icontains=keyword) | Q(user__last_name__icontains=keyword))
        serializer = LiteStudentSerializer(students, many=True)
        return Response(serializer.data)


class StudentFriendsView(APIView):
    def get(self, request, **kwargs):
        pk = kwargs['id']
        s = Student.objects.get(pk=pk)
        serializer = LiteStudentSerializer(s.friends, many=True)
        return Response(serializer.data)


class StudentViewSet(GenericViewSet,
                     mixins.CreateModelMixin,
                     mixins.RetrieveModelMixin,
                     mixins.UpdateModelMixin):
    serializer_class = StudentSerializer
    queryset = Student.objects.all()

    def create(self, request, *args, **kwargs):
        print request.POST
        if User.objects.filter(email=request.POST['email']).count() > 0:
            return Response("Someone else is using this email address", status=status.HTTP_400_BAD_REQUEST)
        import re
        if not re.match(r'[^@]+@[^@]+\.[^@]+', request.POST['email']):
            return Response("Email is not valid", status=status.HTTP_400_BAD_REQUEST)
        if User.objects.filter(username=request.POST['username']).count() > 0:
            return Response("Username is in use", status=status.HTTP_400_BAD_REQUEST)
        try:
            user = User.objects.create_user(request.POST['username'], request.POST['email'], request.POST['password'])
            user.first_name = request.POST['first_name']
            user.last_name = request.POST['last_name']
            user.save()
        except Exception as e:
            return Response(e.message, status=status.HTTP_400_BAD_REQUEST)

        student = Student()
        student.user = user
        student.save()
        return Response("OK", status=status.HTTP_201_CREATED)

    def retrieve(self, request, *args, **kwargs):
        pk = kwargs['pk']
        if pk == 'self':
            student = Student.objects.get(user=request.user)
        else:
            student = Student.objects.get(pk=pk)

        serializer = self.serializer_class(student, context={'request': request})
        return Response(serializer.data)

    @detail_route(methods=['post', 'delete'])
    def friendship(self, request, **kwargs):
        if request.user.is_anonymous():
            return Response('You must log in to do that', status=status.HTTP_400_BAD_REQUEST)

        try:
            if 'friend' in request.query_params:
                friend = request.query_params['friend']
            else:
                friend = request.POST['friend']
            friend_student = Student.objects.get(pk=friend)
        except Exception as e:
            print e
            return Response('Please provide a valid student id using \'friend\' field: '+str(e))
        try:
            student = Student.objects.get(user=request.user)
        except:
            return Response('Only students can add or remove friends', status=status.HTTP_400_BAD_REQUEST)

        if request.method == 'POST':
            student.friends.add(friend_student)
        else:
            student.friends.remove(friend_student)

        return Response('OK')


class StudentInterestSerializer(ModelSerializer):
    class Meta:
        model = Joinable
        fields = ('id', 'type', 'name', 'info')


class StudentInterestsViewSet(GenericViewSet,
                              mixins.CreateModelMixin,
                              mixins.RetrieveModelMixin,
                              mixins.DestroyModelMixin,
                              mixins.ListModelMixin):
    serializer_class = StudentInterestSerializer

    def get_queryset(self):
        student = Student.objects.get(user=self.request.user)
        return student.interests

    def create(self, request, *args, **kwargs):
        if request.user.is_anonymous():
            return Response('Only logged-in students can add interest', status=status.HTTP_400_BAD_REQUEST)
        try:
            student = Student.objects.get(user=request.user)
        except:
            return Response('Only students can add interest', status=status.HTTP_400_BAD_REQUEST)

        joinable_pk = request.POST['joinable']
        try:
            joinable = Joinable.objects.get(pk=joinable_pk)
        except:
            return Response('Joinable could not be found', status=status.HTTP_400_BAD_REQUEST)

        if 'delete' in request.POST:
            student.interests.remove(joinable)
        else:
            student.interests.add(joinable)

        return Response(True)

    def list(self, request, *args, **kwargs):
        pk = kwargs['id']
        student = Student.objects.get(pk=pk)
        serializer = self.serializer_class(student.interests, many=True)
        return Response(serializer.data)


class LeaderBoardView(APIView):
    def get(self, request, **kwargs):
        students = Student.objects.order_by("-points")
        serializer = StudentSerializer(students, many=True)
        return Response(serializer.data)