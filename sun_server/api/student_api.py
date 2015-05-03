from django.contrib.auth.models import User
from rest_framework import mixins
from rest_framework.relations import HyperlinkedIdentityField
from rest_framework.response import Response
from rest_framework.serializers import HyperlinkedModelSerializer
from rest_framework.serializers import SerializerMethodField
from rest_framework.views import APIView
from login.models import Student
from django.db.models import Q
from rest_framework.viewsets import GenericViewSet


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