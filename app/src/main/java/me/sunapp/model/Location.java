package me.sunapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Location {
    int id;
    String name;

    public Location(int id, String name) {
        this.id = id;
        this.name = name;
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

    public static Location parseFromJSONObject(JSONObject obj){
        try {
            int id = obj.getInt("id");
            String name = obj.getString("name");
            return new Location(id, name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
