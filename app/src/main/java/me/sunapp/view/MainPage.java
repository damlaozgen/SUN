package me.sunapp.view;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import me.sunapp.R;


public class MainPage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        Button button14 = (Button) findViewById(R.id.button14);
        button14.setOnClickListener(new View.OnClickListener() {

         public void onClick(View v) {
         // Burada find friends tuşuna basılıyor ve SignUpPage activitisine gidiliyor.

          Intent intent = new Intent(v.getContext(), FindFriendsPage.class);
          startActivityForResult(intent,0);
                                       }
                                   }
        );

        String[] events = {"event1", "event2", "event3", "event4", "event5", "event6", "event7", "event8", "ebent9", "event10"};

        ListView listview = (ListView) findViewById(R.id.listView5);
        listview.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.events_name, events));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
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
