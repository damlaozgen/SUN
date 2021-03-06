package me.sunapp.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import me.sunapp.ContextManager;
import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.client.SUNResponseHandler;
import me.sunapp.helper.EventListViewAdapter;
import me.sunapp.model.Event;
import me.sunapp.model.Student;


public class FutureEventsPage extends ActionBarActivity {
    private ListView listView;
    private TextView points;
    private TextView name;
    private ImageView avatar;
    private Student selectedStudent;
    private ArrayList<Event> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.futureevents_page);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = items.get(position);
                Intent i = new Intent(FutureEventsPage.this, EventDetailPage.class);
                i.putExtra("event_id", e.getId());
                startActivity(i);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SUNClient.getInstance().logout();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }if(id == R.id.action_profile){
            Intent i = new Intent(this, ProfilePage.class);
            i.putExtra("student_id", SUNClient.getInstance().getCurrentUser().getId());
            startActivity(i);
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
                items = selectedStudent.getEvents();
                EventListViewAdapter adapter = new EventListViewAdapter(selectedStudent.getEvents());
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
