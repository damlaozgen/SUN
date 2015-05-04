from django.contrib.auth.models import User
from event.models import Joinable
from rest_framework import mixins
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

    class Meta:
        model = Student
        fields = ('id', 'avatar', 'email', 'name', 'contact_info', 'events')

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

    def retrieve(self, request, *args, **kwargs):
        pk = kwargs['pk']
        if pk == 'self':
            student = Student.objects.get(user=request.user)
        else:
            student = Student.objects.get(pk=pk)

        serializer = self.serializer_class(student, context={'request': request})
        return Response(serializer.data)


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

