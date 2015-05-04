package me.sunapp.view;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.client.SUNResponseHandler;
import me.sunapp.helper.FriendListAdapter;
import me.sunapp.model.Student;

public class FindFriendsPage extends Activity{
    EditText searchBar;
    ListView listView;
    ArrayList<Student> currentItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findfriends_page);
        searchBar = (EditText)findViewById(R.id.find_friends_search_bar);
        listView = (ListView)findViewById(R.id.find_friends_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student s = currentItems.get(position);
                Intent i = new Intent(FindFriendsPage.this, ProfilePage.class);
                i.putExtra("student_id", s.getId());
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity5, menu);
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

    public void search(View v){
        String searchStr = searchBar.getText().toString();
        if(searchStr.length() == 0){
            listView.setAdapter(new FriendListAdapter(new ArrayList<Student>()));
        }else{
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Searching...");
            pd.setCancelable(false);
            pd.show();
            SUNClient.getInstance().searchUser(searchStr, new SUNResponseHandler.SUNStudentListHandler() {
                @Override
                public void actionCompleted(ArrayList<Student> students) {
                    currentItems = students;
                    listView.setAdapter(new FriendListAdapter(students));
                    pd.dismiss();
                }

                @Override
                public void actionFailed(Error error) {
                    listView.setAdapter(new FriendListAdapter(new ArrayList<Student>()));
                    Toast.makeText(FindFriendsPage.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            });
        }
    }
}
