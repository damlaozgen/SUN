package me.sunapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Course extends Joinable{
    public Course(int id, String name, String info) {
        super(id, name, info);
    }
    public static Course parseJSONObject(JSONObject obj){
        try {
            int id = obj.getInt("id");
            String name = obj.getString("name");
            String info = obj.getString("info");
            return new Course(id, name, info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
