package me.sunapp.client;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.sunapp.ContextManager;
import me.sunapp.model.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;


public class SUNClient implements SUNActionPerformer {
    private final static String BASE_URL = "http://128.199.57.186:8000/api/v1/";

    private static SUNClient instance;
    private boolean loggedIn;
    private ArrayList<Notification> currentUserNotifications;
    private AsyncHttpClient httpClient;
    private int dummyEventIdCounter;
    private Student currentUser;
    private Joinable dummyJoinable;
    private ArrayList<Joinable> dummyJoinableList;
    private ArrayList<Student> dummyUserList;
    private ArrayList<SUNResponseHandler.SUNBooleanResponseHandler> userWaitList;
    private SUNClient(){
        // Private constructor for singleton
        currentUserNotifications = new ArrayList<Notification>();
        userWaitList = new ArrayList<SUNResponseHandler.SUNBooleanResponseHandler>();
        httpClient = new AsyncHttpClient();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ContextManager.getInstance().getAppContext());
        String accessToken = sharedPref.getString("accessToken", "");
        if(accessToken != null && accessToken.length() > 0){
            httpClient.addHeader("Authorization", "Token "+accessToken);
            loggedIn = true;
            fetchCurrentUser();
        }
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

    private void fetchCurrentUser(){
        get("student/self/", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    currentUser = Student.parseJSONObject(new JSONObject(new String(responseBody)));
                    Log.d("Client", "User fetched:"+currentUser.toString());
                    Log.d("Client", "Size of waitlist:"+ userWaitList.size());
                    while(userWaitList.size() != 0){
                        SUNResponseHandler.SUNBooleanResponseHandler handler = userWaitList.remove(userWaitList.size()-1);
                        handler.actionCompleted();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    while(userWaitList.size() != 0){
                        SUNResponseHandler.SUNBooleanResponseHandler handler = userWaitList.remove(userWaitList.size()-1);
                        handler.actionFailed(new Error(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Client", "Fetch user failed", error);
            }
        });
    }

    public void waitCurrentUser(SUNResponseHandler.SUNBooleanResponseHandler handler){
        userWaitList.add(handler);
    }

    public void login(String email, String password, final SUNResponseHandler.SUNBooleanResponseHandler handler){
        if(email == null || email.length() == 0){
            handler.actionFailed(new Error("Please enter your email address"));
            return;
        }
        if(password == null || password.length() == 0){
            handler.actionFailed(new Error("Please enter your password"));
            return;
        }
        RequestParams params = new RequestParams();
        params.put("username", email);
        params.put("password", password);
        post("login", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Client", new String(responseBody));
                try {
                    JSONObject token = new JSONObject(new String(responseBody));
                    String accessToken = token.getString("token");
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ContextManager.getInstance().getAppContext());
                    sharedPref.edit().putString("accessToken", accessToken).apply();
                    httpClient.addHeader("Authorization", "Token " + accessToken);
                    loggedIn = true;
                    fetchCurrentUser();
                    //new PushManager().handlePushRegistration();
                } catch (JSONException e) {
                    handler.actionFailed(new Error(new String(responseBody)));
                }
                handler.actionCompleted();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.actionFailed(new Error(error.getMessage()));
            }
        });
    }
    @Override
    public void createEvent(final String name, final Date date, final Joinable joinable, final String eventInfo, final SUNResponseHandler.SUNEventResponseHandler handler) {
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
        RequestParams params = new RequestParams();
        params.put("joinable", joinable.getId());
        params.put("name", name);
        params.put("date", date.toString());
        params.put("info", eventInfo);
        post("event/", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Event e;
                try {
                    e = Event.parseJSONObject(new JSONObject(new String(responseBody)));
                    e.getJoinedStudents().add(currentUser);
                    currentUser.getEvents().add(e);
                    handler.actionCompleted(e);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                    handler.actionFailed(new Error(e1.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.actionFailed(new Error(error.getMessage()));
            }
        });
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

    public boolean isLoggedIn(){
        return loggedIn;
    }

    public void fetchStudentEvents(final Student s, final SUNResponseHandler.SUNBooleanResponseHandler handler){
        String url = "student/"+s.getId()+"/events";
        get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray array = new JSONArray(new String(responseBody));
                    s.getEvents().clear();
                    for(int i = 0; i<array.length(); i++){
                        s.getEvents().add(Event.parseJSONObject(array.getJSONObject(i)));
                    }
                    handler.actionCompleted();
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.actionFailed(new Error(e.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                handler.actionFailed(new Error(error.getMessage()));
            }
        });
    }

    private void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        httpClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        httpClient.post(getAbsoluteUrl(url), params, responseHandler);
    }
    private void post(String url, JSONObject entity, AsyncHttpResponseHandler responseHandler){
        try {
            httpClient.post(ContextManager.getInstance().getAppContext(), getAbsoluteUrl(url), new StringEntity(entity.toString()), "application/json", responseHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    protected static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
