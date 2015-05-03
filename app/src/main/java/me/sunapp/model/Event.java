package me.sunapp.model;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static Event parseJSONObject(JSONObject obj){
        try {
            int id = obj.getInt("id");
            String name = obj.getString("name");
            String info = obj.getString("info");
            Joinable j = Joinable.parseJSONObject(obj.getJSONObject("joinable"));
            String dStr = obj.getString("date");
            Date d;
            try {
                d = DateUtils.parseDate(dStr);
            } catch (DateParseException e) {
                e.printStackTrace();
                d = new Date();
            }
            return new Event(id, name, d, j, info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
