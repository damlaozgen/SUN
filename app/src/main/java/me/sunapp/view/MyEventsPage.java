package me.sunapp.view;

import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ListActivity;
//import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;

import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.client.SUNResponseHandler;
import me.sunapp.helper.EventListViewAdapter;
import me.sunapp.model.Event;
import me.sunapp.model.Student;


// myevents de  eventlerlistview şeklinde olucak. her bir event create edildiğinde listede
// oluşturalacak ve ismi verilecek. o eventin rowuna tıklandığında o evente gidecek.

public class MyEventsPage extends Activity {

    private ListView listView;
    private TextView points;
    private TextView name;
    private ImageView avatar;
    private Student selectedStudent;
    private ArrayList<Event> selectedEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myevents_page);
        listView = (ListView)findViewById(R.id.events_list);
        points = (TextView)findViewById(R.id.events_points);
        name = (TextView)findViewById(R.id.events_name);
        avatar = (ImageView)findViewById(R.id.events_avatar);
        selectedStudent = Student.createStudentWithId(getIntent().getExtras().getInt("student_id"));
        SUNClient.getInstance().fetchStudentInfo(selectedStudent, new SUNResponseHandler.SUNBooleanResponseHandler() {
            @Override
            public void actionCompleted() {
                fetchEvents();
                fillUserDetails();
            }

            @Override
            public void actionFailed(Error error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  Inflate  the   menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity7, menu);
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

    private void fillUserDetails(){
        points.setText(selectedStudent.getPoints() + " points");
        name.setText(selectedStudent.getName());
        ImageLoader.getInstance().displayImage(selectedStudent.getAvatar(), avatar);
    }



    private void fetchEvents(){
        SUNClient.getInstance().fetchStudentEvents(selectedStudent, new SUNResponseHandler.SUNBooleanResponseHandler() {
            @Override
            public void actionCompleted() {
                selectedEvents = new ArrayList<Event>();
                for(Event e : selectedStudent.getEvents()){
                    if(e.getCreatorId() == selectedStudent.getId()){
                        selectedEvents.add(e);
                    }
                }
                EventListViewAdapter adapter = new EventListViewAdapter(selectedEvents);
                listView.setAdapter(adapter);
            }

            @Override
            public void actionFailed(Error error) {
                Toast.makeText(getApplicationContext(), "Failed to fetch events", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showFriends(View v){
        Intent i = new Intent(this, FriendListPage.class);
        i.putExtra("student_id", selectedStudent.getId());
        startActivity(i);
    }



}
