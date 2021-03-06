package me.sunapp.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.client.SUNResponseHandler;
import me.sunapp.helper.InterestListViewAdapter;
import me.sunapp.model.Joinable;
import me.sunapp.model.Student;

//  interests de  interestler listview şeklinde. interest oluşturulduğunda ismiyle beraber yeni bir
// interest  oluşturulcak.

public class InterestsPage extends ActionBarActivity implements InterestListViewAdapter.InterestListObserver{
    Student selectedStudent;
    ListView list;
    TextView name;
    TextView points;
    ImageView avatar;
    ArrayList<Joinable> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interests_page);
        list = (ListView)findViewById(R.id.listView);
        name = (TextView)findViewById(R.id.name);
        points = (TextView)findViewById(R.id.points);
        avatar = (ImageView)findViewById(R.id.avatar);
        selectedStudent = Student.createStudentWithId(getIntent().getExtras().getInt("student_id"));
        if(selectedStudent.getId() != SUNClient.getInstance().getCurrentUser().getId()){
            Button addInterestButton = (Button)findViewById(R.id.add_interest_button);
            addInterestButton.setVisibility(View.INVISIBLE);
        }
        SUNClient.getInstance().fetchStudentInfo(selectedStudent, new SUNResponseHandler.SUNBooleanResponseHandler() {
            @Override
            public void actionCompleted() {
                fetchInterests();
                fillInfo();
            }

            @Override
            public void actionFailed(Error error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ip", "OnItemClick");
                Joinable j = items.get(position);
                Intent i = new Intent(InterestsPage.this, InterestDetailPage.class);
                i.putExtra("joinable_id", j.getId());
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

    private void fillInfo(){
        name.setText(selectedStudent.getName());
        points.setText(""+selectedStudent.getPoints());
        ImageLoader.getInstance().displayImage(selectedStudent.getAvatar(), avatar);
    }
    public void showAddInterest(View v){
        Intent i = new Intent(this, AddInterestActivity.class);
        startActivity(i);
    }

    private void fetchInterests(){
        SUNClient.getInstance().fetchStudentInterests(selectedStudent, new SUNResponseHandler.SUNBooleanResponseHandler() {
            @Override
            public void actionCompleted() {
                items = selectedStudent.getInterests();
                list.setAdapter(new InterestListViewAdapter(selectedStudent.getInterests(), InterestsPage.this));
            }

            @Override
            public void actionFailed(Error error) {
                Toast.makeText(getApplicationContext(), "Unable to fetch user interests! Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void toggleInterest(Joinable j) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        boolean following = false;
        for(Joinable joinable : SUNClient.getInstance().getCurrentUser().getInterests()){
            if(joinable.getId() == j.getId()){
                following = true;
                break;
            }
        }
        if(following){
            pd.setMessage("Removing");
            pd.show();
            SUNClient.getInstance().removeInterest(j, new SUNResponseHandler.SUNBooleanResponseHandler() {
                @Override
                public void actionCompleted() {
                    pd.dismiss();
                    list.invalidateViews();
                }

                @Override
                public void actionFailed(Error error) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{
            pd.setMessage("Adding");
            pd.show();
            SUNClient.getInstance().addInterest(j, new SUNResponseHandler.SUNBooleanResponseHandler() {
                @Override
                public void actionCompleted() {
                    pd.dismiss();
                    list.invalidateViews();
                }

                @Override
                public void actionFailed(Error error) {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void showFriends(View v){
        Intent i = new Intent(this, FriendListPage.class);
        i.putExtra("student_id", selectedStudent.getId());
        startActivity(i);
    }
}
