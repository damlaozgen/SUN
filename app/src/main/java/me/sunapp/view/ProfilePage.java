package me.sunapp.view;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.widget.EditText;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.client.SUNResponseHandler;
import me.sunapp.model.Student;

// ilk sign up yapıldığında yani kayıt olunduğunda çıkan page. login yapıldığında bu değil main page gelecek
public class ProfilePage extends ActionBarActivity {
    private Student selectedStudent;
    private TextView name;
    private TextView point;
    private ImageView avatar;
    private Button notificationButton;
    private Button toggleFriendshipButton;
    private static final String addFriendText = "Add to Friends";
    private static final String removeFriendText = "Remove from Friends";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        name = (TextView)findViewById(R.id.profile_name);
        point = (TextView)findViewById(R.id.profile_points);
        avatar = (ImageView)findViewById(R.id.profile_avatar);
        notificationButton = (Button)findViewById(R.id.profile_notification_button);
        selectedStudent = Student.createStudentWithId(getIntent().getExtras().getInt("student_id"));
        toggleFriendshipButton = (Button)findViewById(R.id.toggleFriend);
        if(selectedStudent.getId() != SUNClient.getInstance().getCurrentUser().getId()){
            notificationButton.setVisibility(View.INVISIBLE);
            toggleFriendshipButton.setVisibility(View.VISIBLE);
            toggleFriendshipButton.setText(addFriendText);
            for(Student s : SUNClient.getInstance().getCurrentUser().getFriends()){
                if(s.getId() == selectedStudent.getId()){
                    toggleFriendshipButton.setText(removeFriendText);
                    break;
                }
            }
        }else{
            notificationButton.setVisibility(View.VISIBLE);
            toggleFriendshipButton.setVisibility(View.INVISIBLE);
        }

        toggleFriendshipButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(ProfilePage.this);
                pd.setCancelable(false);
                if(toggleFriendshipButton.getText().toString().equals(addFriendText)){
                    pd.setMessage("Adding...");
                    pd.show();
                    SUNClient.getInstance().addFriend(selectedStudent, new SUNResponseHandler.SUNBooleanResponseHandler() {
                        @Override
                        public void actionCompleted() {
                            pd.dismiss();
                            toggleFriendshipButton.setText(removeFriendText);
                        }

                        @Override
                        public void actionFailed(Error error) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    pd.setMessage("Removing...");
                    pd.show();
                    SUNClient.getInstance().removeFriend(selectedStudent, new SUNResponseHandler.SUNBooleanResponseHandler() {
                        @Override
                        public void actionCompleted() {
                            pd.dismiss();
                            toggleFriendshipButton.setText(addFriendText);
                        }

                        @Override
                        public void actionFailed(Error error) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        SUNClient.getInstance().fetchStudentInfo(selectedStudent, new SUNResponseHandler.SUNBooleanResponseHandler() {
            @Override
            public void actionCompleted() {
                fillProfile();
            }

            @Override
            public void actionFailed(Error error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Button own_events = (Button) findViewById(R.id.own_events_button);
        own_events.setOnClickListener( new OnClickListener() {

            public void onClick(View v) {
           // Burada my events butonuna basılıyor ve my events activitisine gidilliyor.

                Intent intent = new Intent(v.getContext(), MyEventsPage.class);
                intent.putExtra("student_id", selectedStudent.getId());

                startActivityForResult(intent,0);
                                        }
                                    }

        );

        Button button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener( new OnClickListener() {

         public void onClick(View v) {
        // Burada interests butonuna basılıyor ve interests activitisine gidilliyor.

            Intent intent = new Intent(v.getContext(), InterestsPage.class);
             intent.putExtra("student_id", selectedStudent.getId());

             startActivityForResult(intent,0);
                                        }
                                    }

        );

        Button button8 = (Button) findViewById(R.id.button8);
        button8.setOnClickListener( new OnClickListener() {

         public void onClick(View v) {
         // Burada joined events butonuna basılıyor ve joined events activitisine gidilliyor.

            Intent intent = new Intent(v.getContext(), JoinedEventsPage.class);
             intent.putExtra("student_id", selectedStudent.getId());

             startActivityForResult(intent,0);
                                        }
                                    }

        );

        Button button9 = (Button) findViewById(R.id.button9);
        button9.setOnClickListener( new OnClickListener() {

         public void onClick(View v) {
         // Burada future events butonuna basılıyor ve future events activitisine gidilliyor.

             Intent intent = new Intent(v.getContext(), FutureEventsPage.class);
             intent.putExtra("student_id", selectedStudent.getId());
             startActivityForResult(intent,0);
                 }
                                }

        );

        Button button10 = (Button) findViewById(R.id.button10);
        button10.setOnClickListener( new OnClickListener() {

        public void onClick(View v) {
        // Burada contact info butonuna basılıyor ve contact info activitisine gidilliyor.

            Intent intent = new Intent(v.getContext(), ContactInfoPage.class);
            intent.putExtra("student_id", selectedStudent.getId());

            startActivityForResult(intent,0);
                                        }
                                    }

        );

        notificationButton.setOnClickListener( new OnClickListener() {

         public void onClick(View v) {
           // Burada notifications butonuna basılıyor ve my events activitisine gidilliyor.

             Intent intent = new Intent(v.getContext(), MainPage.class);
             startActivityForResult(intent,0);
                                        }
                                    }

        );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity3, menu);
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

    private void fillProfile(){
        name.setText(selectedStudent.getName());
        point.setText(""+selectedStudent.getPoints());
        ImageLoader.getInstance().displayImage(selectedStudent.getAvatar(), avatar);
    }

    public void showFriends(View v){
        Intent i = new Intent(this, FriendListPage.class);
        i.putExtra("student_id", selectedStudent.getId());
        startActivity(i);
    }
}
