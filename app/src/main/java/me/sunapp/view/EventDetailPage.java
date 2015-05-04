package me.sunapp.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.client.SUNResponseHandler;
import me.sunapp.model.Course;
import me.sunapp.model.Event;
import me.sunapp.model.Hobby;
import me.sunapp.model.Student;

public class EventDetailPage extends ActionBarActivity {
    private static final String JOIN_TEXT = "Join";
    private static final String LEAVE_TEXT = "Leave";
    TextView status;
    TextView date;
    TextView time;
    TextView name;
    TextView info;
    TextView type;
    Button owner;
    TextView place;
    TextView participants;
    Event event;
    Student eventOwner;
    Button joinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail_page);
        status = (TextView)findViewById(R.id.event_status);
        date = (TextView)findViewById(R.id.event_date);
        time = (TextView)findViewById(R.id.event_time);
        name = (TextView)findViewById(R.id.event_name);
        type = (TextView)findViewById(R.id.event_type);
        info = (TextView)findViewById(R.id.event_info);
        owner = (Button)findViewById(R.id.event_creator_button);
        place = (TextView)findViewById(R.id.event_place);
        participants = (TextView)findViewById(R.id.event_participants);
        event = Event.getEventFromCache(getIntent().getExtras().getInt("event_id"));
        joinButton = (Button)findViewById(R.id.event_join_button);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (joinButton.getText() == JOIN_TEXT){
                }
            }
        });
        owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventDetailPage.this, ProfilePage.class);
                i.putExtra("student_id", event.getCreatorId());
                startActivity(i);
            }
        });
        fillDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_detail_page, menu);
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

    private void fillDetails(){
        name.setText(event.getName());
        info.setText(event.getEventInfo());
        if(event.getJoinable() instanceof Course){
            type.setText("Course");
        }else if(event.getJoinable() instanceof Hobby){
            type.setText("Hobby");
        }
        participants.setText(""+event.getJoinedStudents().size());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date.setText(dateFormat.format(event.getDate()));
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
        time.setText(timeFormat.format(event.getDate()));
        eventOwner = Student.createStudentWithId(event.getCreatorId());
        SUNClient.getInstance().fetchStudentInfo(eventOwner, new SUNResponseHandler.SUNBooleanResponseHandler() {
            @Override
            public void actionCompleted() {
                owner.setText(eventOwner.getName());
            }

            @Override
            public void actionFailed(Error error) {
                owner.setText("Club");
                owner.setEnabled(false);
            }
        });
        Student currentUser = SUNClient.getInstance().getCurrentUser();
        if(event.getCreatorId() == currentUser.getId()){
            status.setText("This is your event!");
        }else if(event.getJoinedStudents().contains(currentUser)){
            status.setText("You are participating!");
        }else{
            status.setText("You can participate!");
        }

        updateJoiningStatus();
    }

    private void updateJoiningStatus(){
        if(event.getCreatorId() == SUNClient.getInstance().getCurrentUser().getId()){
            joinButton.setText("Your Event");
            joinButton.setEnabled(false);
        }else{
            if(event.getJoinedStudents().contains(SUNClient.getInstance().getCurrentUser())){
                joinButton.setText(LEAVE_TEXT);
            }else{
                joinButton.setText(JOIN_TEXT);
            }
        }
    }
}
