package com.example.basri.sunapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.widget.EditText;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;

// ilk sign up yapıldığında yani kayıt olunduğunda çıkan page. login yapıldığında bu değil main page gelecek
public class ProfilePage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        Button button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener( new OnClickListener() {

                                        public void onClick(View v) {
           // Burada my events butonuna basılıyor ve my events activitisine gidilliyor.

            Intent intent = new Intent(v.getContext(), MyEventsPage.class);
            startActivityForResult(intent,0);
                                        }
                                    }

        );

        Button button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener( new OnClickListener() {

                                        public void onClick(View v) {
        // Burada interests butonuna basılıyor ve interests activitisine gidilliyor.

         Intent intent = new Intent(v.getContext(), InterestsPage.class);
         startActivityForResult(intent,0);
                                        }
                                    }

        );

        Button button8 = (Button) findViewById(R.id.button8);
        button8.setOnClickListener( new OnClickListener() {

                                        public void onClick(View v) {
         // Burada joined events butonuna basılıyor ve joined events activitisine gidilliyor.

         Intent intent = new Intent(v.getContext(), JoinedEventsPage.class);
         startActivityForResult(intent,0);
                                        }
                                    }

        );

        Button button9 = (Button) findViewById(R.id.button9);
        button9.setOnClickListener( new OnClickListener() {

                                        public void onClick(View v) {
         // Burada future events butonuna basılıyor ve future events activitisine gidilliyor.

         Intent intent = new Intent(v.getContext(), FutureEventsPage.class);
         startActivityForResult(intent,0);
                                        }
                                    }

        );

        Button button10 = (Button) findViewById(R.id.button10);
        button10.setOnClickListener( new OnClickListener() {

                                        public void onClick(View v) {
        // Burada contact info butonuna basılıyor ve contact info activitisine gidilliyor.

         Intent intent = new Intent(v.getContext(), ContactInfoPage.class);
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
}
