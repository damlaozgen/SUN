package me.sunapp.model;

import android.graphics.Paint;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public abstract class Joinable {
    private int id;
    private String name;
    private String info;
    private static HashMap<Integer, Joinable> cache = new HashMap<>();

    public Joinable(int id, String name, String info) {
        this.id = id;
        this.name = name;
        this.info = info;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Joinable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                '}';
    }

    public static Joinable getFromCache(int id){
        if(cache.containsKey(id)){
            return cache.get(id);
        }
        return null;
    }

    public static Joinable parseJSONObject(JSONObject obj){
        try {
            int id = obj.getInt("id");
            Joinable j = getFromCache(id);
            if(j != null){
                return j;
            }
            String type = obj.getString("type");
            if(type.equalsIgnoreCase("c")){
                j = Course.parseJSONObject(obj);
                cache.put(id, j);
                return j;
            }else if(type.equalsIgnoreCase("h")){
                Log.e("Joinable", "Hobbies not implemented yet");
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
