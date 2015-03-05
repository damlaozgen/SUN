package com.example.basri.sunapp;

import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ListActivity;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;




// myevents de eventlerlistview şeklinde olucak. her bir event create edildiğinde listede
// oluşturalacak ve ismi verilecek. o eventin rowuna tıklandığında o evente gidecek.

public class MyEventsPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myevents_page);

        String[] events = {"event1", "event2", "event3", "event4", "event5", "event6", "event7", "event8", "ebent9", "event10"};

        ListView listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.events_name, events));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity4, menu);
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
