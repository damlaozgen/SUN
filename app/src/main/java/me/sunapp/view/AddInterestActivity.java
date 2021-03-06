package me.sunapp.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import me.sunapp.helper.InterestListViewAdapter;
import me.sunapp.model.Joinable;

public class AddInterestActivity extends ActionBarActivity implements InterestListViewAdapter.InterestListObserver {
    private ListView list;
    private ArrayList<Joinable> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_interest);
        list = (ListView)findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Joinable j = items.get(position);
                Intent i = new Intent(AddInterestActivity.this, InterestDetailPage.class);
                i.putExtra("joinable_id", j.getId());
                startActivity(i);
            }
        });
        fetchInterests();
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

    private void fetchInterests(){
        SUNClient.getInstance().fetchJoinableList(new SUNResponseHandler.SUNJoinableListHandler() {
            @Override
            public void actionCompleted(ArrayList<Joinable> joinables) {
                items = joinables;
                list.setAdapter(new InterestListViewAdapter(joinables, AddInterestActivity.this));
            }

            @Override
            public void actionFailed(Error error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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
}
