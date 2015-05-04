package me.sunapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsItem {
    private Date date;
    private String text;

    public NewsItem(Date date, String text) {
        this.date = date;
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static NewsItem parseFromJSONObject(JSONObject obj){
        Date d;
        try{
            try {
                SimpleDateFormat parserSDF=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                d = parserSDF.parse(obj.getString("date"));
            } catch (ParseException e) {
                e.printStackTrace();
                d = new Date();
            }
            String text = obj.getString("text");
            return new NewsItem(d, text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
