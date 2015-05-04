package me.sunapp.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.client.SUNResponseHandler;
import me.sunapp.helper.EventListViewAdapter;
import me.sunapp.model.Event;
import me.sunapp.model.Joinable;

public class InterestDetailPage extends ActionBarActivity {
    TextView name;
    TextView info;
    ListView listView;
    Joinable joinable;
    ArrayList<Event> events;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_detail_page);
        name = (TextView)findViewById(R.id.joinable_name);
        info = (TextView)findViewById(R.id.joinable_info);
        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = events.get(position);
                Intent i = new Intent(InterestDetailPage.this, EventDetailPage.class);
                i.putExtra("event_id", e.getId());
                startActivity(i);
            }
        });
        SUNClient.getInstance().getJoinable(getIntent().getExtras().getInt("joinable_id"), new SUNResponseHandler.SUNJoinableDetailHandler() {
            @Override
            public void actionCompleted(Joinable joinable) {
                InterestDetailPage.this.joinable = joinable;
                fillInfo();
                fetchEvents();
            }

            @Override
            public void actionFailed(Error error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fillInfo(){
        name.setText(joinable.getName());
        info.setText(joinable.getInfo());
    }
    private void fetchEvents(){
        SUNClient.getInstance().fetchJoinableEvents(joinable, new SUNResponseHandler.SUNEventListHandler() {
            @Override
            public void actionCompleted(ArrayList<Event> events) {
                InterestDetailPage.this.events = events;
                listView.setAdapter(new EventListViewAdapter(events));
            }

            @Override
            public void actionFailed(Error error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_interest_detail_page, menu);
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

    public void newEvent(View v){
        Intent i = new Intent(this, CreateEventPage.class);
        i.putExtra("joinable_id", joinable.getId());
        startActivity(i);
    }
}
