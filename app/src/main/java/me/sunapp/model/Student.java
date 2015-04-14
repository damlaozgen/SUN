package me.sunapp.model;

import java.util.ArrayList;

public class Student extends User{
    private ArrayList<Student> friends;
    private ArrayList<Event> events;
    private ArrayList<Joinable> interests;
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

    @Override
    public String toString() {
        return "Student{" + super.toString() +
                ", interests=" + interests +
                ", points=" + points +
                "} ";
    }
}
