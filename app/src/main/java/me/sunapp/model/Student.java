package me.sunapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Student extends User{
    private ArrayList<Student> friends;
    private ArrayList<Event> events;
    private ArrayList<Joinable> interests;
    private Date lastFetchDate;
    private int points;

    public Student(int id, String email, String password, String name, String avatar, String contactInfo) {
        super(id, email, password, name, avatar, contactInfo);
        friends = new ArrayList<Student>();
        events = new ArrayList<Event>();
        interests = new ArrayList<Joinable>();
    }

    public ArrayList<Student> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Student> friends) {
        this.friends = friends;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Joinable> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<Joinable> interests) {
        this.interests = interests;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getLastFetchDate() {
        return lastFetchDate;
    }

    @Override
    public String toString() {
        return "Student{" + super.toString() +
                ", interests=" + interests +
                ", points=" + points +
                "} ";
    }

    public void updateStudent(Student newStudent){
        this.name = newStudent.getName();
        this.password = newStudent.getPassword();
        this.points = newStudent.getPoints();
        this.email = newStudent.getEmail();
        this.avatar = newStudent.getAvatar();
        this.contactInfo = newStudent.getContactInfo();
    }

    public static Student createStudentWithId(int id){
        return new Student(id, null, null, "Loading..", null, null);
    }

    public static Student parseJSONObject(JSONObject obj){
        try {
            int id = obj.getInt("id");
            String avatar = obj.getString("avatar");
            String name = obj.getString("name");
            String contact_info = null;
            if(obj.has("contact_info"))
                contact_info = obj.getString("contact_info");

            String email = null;
            if(obj.has("email"))
                email = obj.getString("email");

            Student s = new Student(id, email, null, name, avatar, contact_info);
            s.lastFetchDate = new Date();
            return s;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
