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
import android.widget.EditText;
import android.widget.Toast;

import me.sunapp.R;
import me.sunapp.client.SUNClient;
import me.sunapp.client.SUNResponseHandler;


public class SignUpPage extends ActionBarActivity {
    EditText username;
    EditText password;
    EditText email;
    EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        email = (EditText)findViewById(R.id.email);
        name = (EditText)findViewById(R.id.name);
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new OnClickListener() {

                                       public void onClick(View v) {
              // Burada e-mail,    name, surname ve password konrollerden geçerek database e işlenecek.
               final ProgressDialog pd = new ProgressDialog(SignUpPage.this);
                                           pd.setMessage("Signing Up..");
                                           pd.setCancelable(false);
                                           pd.show();
                                           SUNClient.getInstance().createUser(username.getText().toString(), email.getText().toString(), password.getText().toString(), name.getText().toString(), new SUNResponseHandler.SUNBooleanResponseHandler() {
                                               @Override
                                               public void actionCompleted() {
                                                   pd.dismiss();
                                                   Toast.makeText(getApplicationContext(), "User Created!", Toast.LENGTH_LONG).show();
                                                   finish();
                                               }

                                               @Override
                                               public void actionFailed(Error error) {
                                                   pd.dismiss();
                                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                               }
                                           });
                                       }
                                   }
        );
    }

}
