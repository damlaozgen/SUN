package me.sunapp.view;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import me.sunapp.ContextManager;
import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.client.SUNResponseHandler;
import me.sunapp.helper.EventListViewAdapter;
import me.sunapp.model.Student;


public class FutureEventsPage extends ActionBarActivity {
    private ListView listView;
    private TextView points;
    private TextView name;
    private ImageView avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.futureevents_page);
        listView = (ListView)findViewById(R.id.future_events_list);
        points = (TextView)findViewById(R.id.future_events_points);
        name = (TextView)findViewById(R.id.future_events_name);
        avatar = (ImageView)findViewById(R.id.future_events_avatar);
        ContextManager.getInstance().setCurrentActivity(this);
        fetchEvents();
        fillUserDetails();
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
        Student s = ContextManager.getInstance().getUserForDetail();
        points.setText(s.getPoints() + " points");
        name.setText(s.getName());
        Log.d("asdf",s.getAvatar());
        ImageLoader.getInstance().displayImage(s.getAvatar(), avatar);
    }



    private void fetchEvents(){
        SUNClient.getInstance().fetchStudentEvents(ContextManager.getInstance().getUserForDetail(), new SUNResponseHandler.SUNBooleanResponseHandler() {
            @Override
            public void actionCompleted() {
                EventListViewAdapter adapter = new EventListViewAdapter(SUNClient.getInstance().getCurrentUser().getEvents());
                listView.setAdapter(adapter);
            }

            @Override
            public void actionFailed(Error error) {
                Toast.makeText(getApplicationContext(), "Failed to fetch events", Toast.LENGTH_LONG).show();
            }
        });
    }
}
