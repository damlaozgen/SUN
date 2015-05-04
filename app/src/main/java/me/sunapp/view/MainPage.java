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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
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
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                    .cacheOnDisk(true)
                                    .cacheInMemory(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                                            .defaultDisplayImageOptions(options).build();

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
        }if(id == R.id.action_profile){
            Intent i = new Intent(this, ProfilePage.class);
            i.putExtra("student_id", SUNClient.getInstance().getCurrentUser().getId());
            startActivity(i);
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
                    Intent i = new Intent(MainPage.this, FutureEventsPage.class);
                    i.putExtra("student_id", SUNClient.getInstance().getCurrentUser().getId());
                    startActivity(i);
                }

                @Override
                public void actionFailed(Error error) {
                    Toast.makeText(getApplicationContext(), "Unable to fetch user info", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Intent i = new Intent(MainPage.this, FutureEventsPage.class);
            i.putExtra("student_id", SUNClient.getInstance().getCurrentUser().getId());
            startActivity(i);
        }

    }
    public void bumpPressed(View v){
        Intent i = new Intent(this, BumpActivity.class);
        i.putExtra("student_id", SUNClient.getInstance().getCurrentUser().getId());
        startActivity(i);
    }
}
