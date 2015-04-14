package me.sunapp.model;

import java.util.ArrayList;
import java.util.Date;

public class Event {
    private int id;
    private String name;
    private Date date;
    private Joinable joinable;
    private ArrayList<Student> joinedStudents;
    private String eventInfo;

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", joinable=" + joinable +
                ", eventInfo='" + eventInfo + '\'' +
                '}';
    }

    public Event(int id, String name, Date date, Joinable joinable, String eventInfo) {
        this.id = id;
        this.name = name;
        this.date = date;

        this.joinable = joinable;
        this.eventInfo = eventInfo;
        joinedStudents = new ArrayList<Student>();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Joinable getJoinable() {
        return joinable;
    }

    public void setJoinable(Joinable joinable) {
        this.joinable = joinable;
    }

    public ArrayList<Student> getJoinedStudents() {
        return joinedStudents;
    }

    public void setJoinedStudents(ArrayList<Student> joinedStudents) {
        this.joinedStudents = joinedStudents;
    }

    public String getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(String eventInfo) {
        this.eventInfo = eventInfo;
    }
}
