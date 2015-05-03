package me.sunapp.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import me.sunapp.ContextManager;
import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.client.SUNResponseHandler;
import me.sunapp.model.Event;
import me.sunapp.model.Student;


public class MainPage extends ActionBarActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        fetchFeed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchFeed(){

    }
    public void showFindFriends(View v){
        Intent i = new Intent(this, FindFriendsPage.class);
        startActivity(i);
    }

    public void showFutureEvents(View v){
        if(SUNClient.getInstance().getCurrentUser() == null){
            SUNClient.getInstance().waitCurrentUser(new SUNResponseHandler.SUNBooleanResponseHandler() {
                @Override
                public void actionCompleted() {
                    ContextManager.getInstance().setStudentForDetail(SUNClient.getInstance().getCurrentUser());
                    Intent i = new Intent(MainPage.this, FutureEventsPage.class);
                    startActivity(i);
                }

                @Override
                public void actionFailed(Error error) {
                    Toast.makeText(getApplicationContext(), "Unable to fetch user info", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            ContextManager.getInstance().setStudentForDetail(SUNClient.getInstance().getCurrentUser());
            Intent i = new Intent(MainPage.this, FutureEventsPage.class);
            startActivity(i);
        }

    }
}
