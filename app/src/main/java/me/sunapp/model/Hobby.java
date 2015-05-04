package me.sunapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Hobby extends Joinable{
    public Hobby(int id, String name, String info) {
        super(id, name, info);
    }
    public static Hobby parseJSONObject(JSONObject obj){
        try {
            int id = obj.getInt("id");
            String name = obj.getString("name");
            String info = obj.getString("info");
            return new Hobby(id, name, info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
