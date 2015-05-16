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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.client.SUNResponseHandler;
import me.sunapp.helper.LeaderBoardListAdapter;
import me.sunapp.model.Student;

public class LeaderBoardActivity extends ActionBarActivity {
    ListView listView;
    ArrayList<Student> students;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        listView = (ListView)findViewById(R.id.listView);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Fetching Leader Board");
        pd.show();
        SUNClient.getInstance().fetchLeaderBoard(new SUNResponseHandler.SUNStudentListHandler() {
            @Override
            public void actionCompleted(ArrayList<Student> students) {
                Log.d("Leader", students.toString());
                pd.dismiss();
                LeaderBoardActivity.this.students = students;
                listView.setAdapter(new LeaderBoardListAdapter(students));

            }

            @Override
            public void actionFailed(Error error) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student selected = students.get(position);
                Intent i = new Intent(LeaderBoardActivity.this, ProfilePage.class);
                i.putExtra("student_id", selected.getId());
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leader_board, menu);
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
}
