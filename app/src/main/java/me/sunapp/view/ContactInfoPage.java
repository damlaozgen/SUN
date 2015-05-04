package me.sunapp.view;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.client.SUNResponseHandler;
import me.sunapp.model.Student;


public class ContactInfoPage extends ActionBarActivity {
    Student selectedStudent;
    TextView name;
    TextView point;
    TextView email;
    TextView contactInfo;
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactinfo_page);
        name = (TextView)findViewById(R.id.name);
        point = (TextView)findViewById(R.id.points);
        email = (TextView)findViewById(R.id.email_text);
        contactInfo = (TextView)findViewById(R.id.contact_info);
        avatar = (ImageView)findViewById(R.id.avatar);

        selectedStudent = Student.createStudentWithId(getIntent().getExtras().getInt("student_id"));
        SUNClient.getInstance().fetchStudentInfo(selectedStudent, new SUNResponseHandler.SUNBooleanResponseHandler() {
            @Override
            public void actionCompleted() {
                fillInfo();
            }

            @Override
            public void actionFailed(Error error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  Inflate  the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity8, menu);
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

    private void fillInfo(){
        name.setText(selectedStudent.getName());
        point.setText(""+selectedStudent.getPoints());
        email.setText(selectedStudent.getEmail());
        contactInfo.setText(selectedStudent.getContactInfo());
        ImageLoader.getInstance().displayImage(selectedStudent.getAvatar(), avatar);
    }
}
