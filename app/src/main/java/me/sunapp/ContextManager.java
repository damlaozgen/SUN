package me.sunapp;
import android.app.Activity;
import android.content.Context;

import me.sunapp.model.Student;

public class ContextManager {
    private static ContextManager instance;
    private Context appContext;

    private Activity currentActivity;

    private ContextManager(){

    }

    public Context getAppContext(){
        return appContext;
    }
    private void setAppContext(Context appContext){
        this.appContext = appContext;
    }

    public static ContextManager getInstance() {
        return instance;
    }

    public static void prepare(Activity activity){
        if(instance != null){
            return;
        }
        instance = new ContextManager();
        instance.setAppContext(activity.getApplicationContext());
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }
}

