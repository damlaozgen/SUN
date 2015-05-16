package me.sunapp.model;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.sunapp.client.SUNClient;

public class Event {
    private int id;
    private String name;
    private Date date;
    private Joinable joinable;
    private ArrayList<Student> joinedStudents;
    private String eventInfo;
    private int creatorId;
    private Location location;
    private static HashMap<Integer, Event> cache = new HashMap<Integer, Event>();

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

    public Event(int id, int creatorId, String name, Date date, Joinable joinable, String eventInfo) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.creatorId = creatorId;

        this.joinable = joinable;
        this.eventInfo = eventInfo;
        joinedStudents = new ArrayList<Student>();

    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
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
            int creatorId = obj.getInt("creator");
            Date d;
            try {
                SimpleDateFormat parserSDF=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                d = parserSDF.parse(dStr);
            } catch (ParseException e) {
                e.printStackTrace();
                d = new Date();
            }
            Event e = new Event(id, creatorId, name, d, j, info);
            if(obj.has("location_id")){
                int loc_id = obj.getInt("location_id");
                for(Location l : SUNClient.getInstance().getLocations()){
                    if(l.getId() == loc_id){
                        e.setLocation(l);
                    }
                }
            }
            cache.put(id, e);
            JSONArray students = obj.getJSONArray("students");
            ArrayList<Student> studentArrayList = new ArrayList<>(students.length());
            for(int i = 0; i<students.length(); i++){
                studentArrayList.add(Student.parseJSONObject(students.getJSONObject(i)));
            }
            e.setJoinedStudents(studentArrayList);
            return e;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Event getEventFromCache(int id){
        return cache.get(id);
    }
}
