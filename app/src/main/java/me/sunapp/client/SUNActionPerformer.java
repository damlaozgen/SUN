package me.sunapp.client;

import me.sunapp.model.Event;
import me.sunapp.model.Joinable;
import me.sunapp.model.Location;
import me.sunapp.model.Student;

import java.util.Date;

public interface SUNActionPerformer {
    public void createEvent(final String name, final Date date, final Joinable joinable, final String eventInfo, Location location, final SUNResponseHandler.SUNBooleanResponseHandler handler);
    public void deleteEvent(Event event, SUNResponseHandler.SUNBooleanResponseHandler handler);
    public void getJoinable(int id, SUNResponseHandler.SUNJoinableDetailHandler handler);
    public void addInterest(Joinable joinable, SUNResponseHandler.SUNBooleanResponseHandler handler);
    public void removeInterest(Joinable joinable, SUNResponseHandler.SUNBooleanResponseHandler handler);
    public void addFriend(Student friend, SUNResponseHandler.SUNBooleanResponseHandler handler);
    public void removeFriend(Student friend, SUNResponseHandler.SUNBooleanResponseHandler handler);
    public void searchUser(String keyword, SUNResponseHandler.SUNStudentListHandler handler);
    public void fetchJoinableEvents(Joinable j, SUNResponseHandler.SUNEventListHandler handler);
    public void fetchNews(SUNResponseHandler.SUNNewsItemListHandler handler);
}
