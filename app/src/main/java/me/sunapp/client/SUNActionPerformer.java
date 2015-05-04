package me.sunapp.client;

import me.sunapp.model.Event;
import me.sunapp.model.Joinable;
import me.sunapp.model.Student;

import java.util.Date;

public interface SUNActionPerformer {
    public void createEvent(String name, Date date, Joinable joinable,  String eventInfo, SUNResponseHandler.SUNEventResponseHandler handler);
    public void deleteEvent(Event event, SUNResponseHandler.SUNBooleanResponseHandler handler);
    public void getJoinable(int id, SUNResponseHandler.SUNJoinableDetailHandler handler);
    public void addInterest(Joinable joinable, SUNResponseHandler.SUNBooleanResponseHandler handler);
    public void removeInterest(Joinable joinable, SUNResponseHandler.SUNBooleanResponseHandler handler);
    public void addFriend(Student friend, SUNResponseHandler.SUNBooleanResponseHandler handler);
    public void removeFriend(Student friend, SUNResponseHandler.SUNBooleanResponseHandler handler);
    public void searchUser(String keyword, SUNResponseHandler.SUNStudentListHandler handler);
}
