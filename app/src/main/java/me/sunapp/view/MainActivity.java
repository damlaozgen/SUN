package me.sunapp.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import me.sunapp.ContextManager;
import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.client.SUNResponseHandler;


public class MainActivity extends ActionBarActivity {

    private EditText email;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContextManager.prepare(this);
        ContextManager.getInstance().setCurrentActivity(this);
        email = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
         // Burada sign up tuşuna basılıyor ve SignUpPage activitisine gidiliyor.

                Intent intent = new Intent(v.getContext(), SignUpPage.class);
                startActivityForResult(intent,0);
            }
        }
        );

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {

                                      public void onClick(final View v) {
                                          final ProgressDialog pd = new ProgressDialog(MainActivity.this);
                                          pd.setCancelable(false);
                                          pd.setMessage("Logging in...");
                                          pd.show();
                                          SUNClient.getInstance().login(email.getText().toString(), password.getText().toString(), new SUNResponseHandler.SUNBooleanResponseHandler() {
                                              @Override
                                              public void actionCompleted() {

                                                  pd.dismiss();
                                                  showMainPage();
                                              }


                                              @Override
                                              public void actionFailed(Error error) {
                                                  pd.dismiss();
                                                  Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                              }
                                          });
                                      }
                                  });

        checkLogin();
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
    private void checkLogin(){
        if(SUNClient.getInstance().isLoggedIn()){
            showMainPage();
        }
    }
    private void showMainPage(){
        Intent intent = new Intent(getApplicationContext(), MainPage.class);
        startActivityForResult(intent, 0);
    }
}
