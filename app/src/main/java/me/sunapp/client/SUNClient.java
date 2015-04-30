package me.sunapp.client;

import com.loopj.android.http.AsyncHttpClient;

import me.sunapp.model.*;

import java.util.ArrayList;
import java.util.Date;


public class SUNClient implements SUNActionPerformer {
    private static SUNClient instance;
    private ArrayList<Notification> currentUserNotifications;
    private AsyncHttpClient httpClient
    private int dummyEventIdCounter;
    private Student currentUser;
    private Joinable dummyJoinable;
    private ArrayList<Joinable> dummyJoinableList;
    private ArrayList<Student> dummyUserList;
    private SUNClient(){
        // Private constructor for singleton
        currentUserNotifications = new ArrayList<Notification>();

        dummyEventIdCounter = 1;
        dummyJoinable = new Course(1, "CS 101", "Introduction to Programming");

        dummyJoinableList = new ArrayList<Joinable>();
        dummyJoinableList.add(dummyJoinable);

        dummyUserList = new ArrayList<Student>();
    }
    public static SUNClient getInstance(){
        if(instance == null){
            instance = new SUNClient();
        }
        return instance;
    }

    public void login(String email, String password, SUNResponseHandler.SUNBooleanResponseHandler handler){
        if(email == null || email.length() == 0){
            handler.actionFailed(new Error("Please enter your email address"));
            return;
        }
        if(password == null || password.length() == 0){
            handler.actionFailed(new Error("Please enter your password"));
            return;
        }
        if(email.equals(password)){
            // Generating dummy data
            currentUser = new Student(1, "test@mail.com", password, "Test User",
                    "https://forum.codoh.com/images/avatars/avatar-blank.jpg", "Contact Info");

            Event dummyEvent = new Event(dummyEventIdCounter++, "First Test Event", new Date(), dummyJoinable, "Test Event Information");
            currentUser.getEvents().add(dummyEvent);
            dummyEvent.getJoinedStudents().add(currentUser);

            Student dummyEventUser = new Student(3, "dummy@mail.com", null, "Event Goer",
                    "http://tux.crystalxp.net/png/caporal-tux-capo-5832.png", "Goes to Events");
            dummyUserList.add(dummyEventUser);
            dummyEventUser.getEvents().add(dummyEvent);
            dummyEvent.getJoinedStudents().add(dummyEventUser);

            Student dummyFriend = new Student(2, "friend@mail.com", null, "Dummy Friend",
                    "http://gravatar.com/avatar/c71c38340fc94601738c4d0c794cca36?s=512", "Friend contact info");
            dummyUserList.add(dummyFriend);
            dummyFriend.getFriends().add(currentUser);
            currentUser.getFriends().add(dummyFriend);

            handler.actionCompleted();
        }else{
            handler.actionFailed(new Error("Login failed! If you enter the same string on both username and password, then I can login"));
        }
    }
    @Override
    public void createEvent(String name, Date date, Joinable joinable, String eventInfo, SUNResponseHandler.SUNEventResponseHandler handler) {
        if(name == null || name.length() == 0){
            handler.actionFailed(new Error("Name of the Event cannot be null"));
            return;
        }
        Date now = new Date();
        if(date == null || now.after(date)){
            handler.actionFailed(new Error("Please specify a correct date for the event"));
            return;
        }
        if(joinable == null){
            handler.actionFailed(new Error("A Joinable must be given to create an event"));
            return;
        }
        Event e = new Event(dummyEventIdCounter++, name, date, joinable, eventInfo);
        e.getJoinedStudents().add(currentUser);
        currentUser.getEvents().add(e);
        handler.actionCompleted(e);
    }

    @Override
    public void deleteEvent(Event event, SUNResponseHandler.SUNBooleanResponseHandler handler) {
        if(!currentUser.getEvents().contains(event)){
            handler.actionFailed(new Error("Given Event does not belong to the current user"));
            return;
        }
        currentUser.getEvents().remove(event);
        handler.actionCompleted();
    }

    @Override
    public void getJoinable(int id, SUNResponseHandler.SUNJoinableDetailHandler handler) {
        for(Joinable j : dummyJoinableList){
            if(j.getId() == id){
                handler.actionCompleted(j);
                return;
            }
        }
        handler.actionFailed(new Error("Could not find a Joinable with the given ID"));
    }

    @Override
    public void addInterest(Joinable joinable, SUNResponseHandler.SUNBooleanResponseHandler handler) {
        if(currentUser.getInterests().contains(joinable)){
            handler.actionFailed(new Error("User is already following the interest"));
            return;
        }
        currentUser.getInterests().add(joinable);
        handler.actionCompleted();
    }

    @Override
    public void removeInterest(Joinable joinable, SUNResponseHandler.SUNBooleanResponseHandler handler) {
        if(!currentUser.getInterests().contains(joinable)){
            handler.actionFailed(new Error("User is not following the interest"));
            return;
        }
        currentUser.getInterests().remove(joinable);
        handler.actionCompleted();
    }

    @Override
    public void addFriend(Student friend, SUNResponseHandler.SUNBooleanResponseHandler handler) {
        if(friend.getId() == currentUser.getId()){
            handler.actionFailed(new Error("You cannot add yourself as a friend"));
            return;
        }
        if(currentUser.getFriends().contains(friend)){
            handler.actionFailed(new Error("You are already friends with " + friend.getName()));
            return;
        }
        currentUser.getFriends().add(currentUser);
        currentUser.getFriends().add(friend);
        handler.actionCompleted();
    }

    @Override
    public void searchUser(String keyword, SUNResponseHandler.SUNStudentListHandler handler) {
        System.out.println("Warning! Search does not use the given keyword");
        handler.actionCompleted(dummyUserList);
    }

    public void fetchJoinableList(SUNResponseHandler.SUNJoinableListHandler handler){
        handler.actionCompleted(dummyJoinableList);
    }

    public Student getCurrentUser(){
        return currentUser;
    }

    public Joinable getDummyJoinable() {
        return dummyJoinable;
    }
}
