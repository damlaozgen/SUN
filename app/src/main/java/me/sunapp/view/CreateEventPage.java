package me.sunapp.view;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.client.SUNResponseHandler;
import me.sunapp.model.Course;
import me.sunapp.model.Event;
import me.sunapp.model.Hobby;
import me.sunapp.model.Joinable;


public class CreateEventPage extends ActionBarActivity {
    Joinable joinable;
    TextView type;
    EditText title;
    EditText info;
    Button date;
    Button time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_page);
        joinable = Joinable.getFromCache(getIntent().getExtras().getInt("joinable_id"));
        if(joinable instanceof Course){
            type.setText("Course - " + joinable.getName());
        }else if(joinable instanceof Hobby){
            type.setText("Hobby - " + joinable.getName());
        }
        title = (EditText)findViewById(R.id.editText4);
        info = (EditText)findViewById(R.id.event_info);
        date = (Button)findViewById(R.id.select_date);
        time = (Button)findViewById(R.id.select_time);
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

    public void save(View v){
        SUNClient.getInstance().createEvent(title.getText().toString(),new Date(), joinable, info.getText().toString(), new SUNResponseHandler.SUNEventResponseHandler() {
            @Override
            public void actionCompleted(Event event) {

            }

            @Override
            public void actionFailed(Error error) {

            }
        });
    }
}
