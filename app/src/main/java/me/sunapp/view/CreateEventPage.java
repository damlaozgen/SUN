package me.sunapp.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.client.SUNResponseHandler;
import me.sunapp.model.Course;
import me.sunapp.model.Event;
import me.sunapp.model.Hobby;
import me.sunapp.model.Joinable;
import me.sunapp.model.Location;


public class CreateEventPage extends ActionBarActivity {
    Joinable joinable;
    TextView type;
    EditText title;
    EditText info;
    Button date;
    Button time;
    Calendar selectedTime;
    Spinner spinner;
    boolean dateSelected, timeSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_page);
        joinable = Joinable.getFromCache(getIntent().getExtras().getInt("joinable_id"));
        title = (EditText)findViewById(R.id.editText4);
        info = (EditText)findViewById(R.id.event_info);
        date = (Button)findViewById(R.id.select_date);
        time = (Button)findViewById(R.id.select_time);
        type = (TextView)findViewById(R.id.event_type);
        spinner = (Spinner)findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        for(Location l : SUNClient.getInstance().getLocations()){
            list.add(l.getName());
        }
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list));
        if(joinable instanceof Course){
            type.setText("Course - " + joinable.getName());
        }else if(joinable instanceof Hobby){
            type.setText("Hobby - " + joinable.getName());
        }
        selectedTime = Calendar.getInstance();
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

    public void save(View v){
        if(!dateSelected){
            Toast.makeText(this, "Please select the event date", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!timeSelected){
            Toast.makeText(this, "Please select the event time", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Creating");
        pd.setCancelable(false);
        pd.show();
        String locationName = (String)spinner.getSelectedItem();
        Location selectedLocation = null;
        for(Location l : SUNClient.getInstance().getLocations()){
            if(l.getName().equals(locationName)){
                selectedLocation = l;
                break;
            }
        }
        SUNClient.getInstance().createEvent(title.getText().toString(),selectedTime.getTime(), joinable, info.getText().toString(), selectedLocation, new SUNResponseHandler.SUNBooleanResponseHandler() {
            @Override
            public void actionCompleted() {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Event Created!", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void actionFailed(Error error) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void selectDate(View v){
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateSelected = true;
                selectedTime.set(year, monthOfYear, dayOfMonth);
                date.setText(year+"/"+monthOfYear+"/"+dayOfMonth);
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void selectTime(View v){
        Calendar cal = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeSelected = true;
                selectedTime.set(Calendar.HOUR, hourOfDay);
                selectedTime.set(Calendar.MINUTE, minute);
                time.setText(hourOfDay+":"+minute);
            }
        }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true);
        dialog.show();
    }
}
