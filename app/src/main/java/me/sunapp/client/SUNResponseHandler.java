package me.sunapp.client;

import me.sunapp.model.Event;
import me.sunapp.model.Joinable;
import me.sunapp.model.Student;

import java.util.ArrayList;

public abstract class SUNResponseHandler {
    public abstract void actionFailed(Error error);

    public static abstract class SUNBooleanResponseHandler extends SUNResponseHandler{
        public abstract void actionCompleted();
    }

    public static abstract class SUNEventResponseHandler extends SUNResponseHandler{
        public abstract void actionCompleted(Event event);
    }
    public static abstract class SUNEventListHandler extends SUNResponseHandler{
        public abstract void actionCompleted(ArrayList<Event> events);
    }

    public static abstract class SUNJoinableListHandler extends SUNResponseHandler{
        public abstract void actionCompleted(ArrayList<Joinable> joinables);
    }

    public static abstract class SUNJoinableDetailHandler extends SUNResponseHandler{
        public abstract void actionCompleted(Joinable joinable);
    }

    public static abstract class SUNStudentListHandler extends SUNResponseHandler{
        public abstract void actionCompleted(ArrayList<Student> students);
    }
}