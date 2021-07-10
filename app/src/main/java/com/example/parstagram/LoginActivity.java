package com.example.parstagram;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public final String TAG = "LoginActivity";
    EditText etUsername;
    EditText etPassword;
    Button btLogin;
    Button btSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*--------------------- ACTION BAR & NAVIGATION BAR ------------------------*/
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        // we add fnacy styled "Parstagram" to action bar
        actionBar.setDisplayShowTitleEnabled(true);
        SpannableString s = new SpannableString(getString(R.string.app_name));
        s.setSpan(new TypefaceSpan(MainActivity.fontFamily), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new RelativeSizeSpan(2f), 0, getString(R.string.app_name).length() , 0);
        actionBar.setTitle(s);

        /* We change the color of home, back and windows buttons */
        getWindow().setNavigationBarColor(getResources().getColor(R.color.pinkish));

        /*--------------------------------------------------------------------------*/

        /*--------------------- VIEW REFERENCES ------------------------*/
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        btSignUp = findViewById(R.id.btSignUpRedir);
        /*--------------------------------------------------------------------------*/

        /* ------------------ WE EVALUATE IF THERE IS ALREADY A USER --------------------- */
        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }
        /* ------------------------------------------------------------------------------- */

        /* ------------- IF LOGIN IS CLICKED WE EVALUATE THE LOGIN CREDENTIALS ----------*/
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                loginUser(username, password);
            }
        });
        /* ------------------------------------------------------------------------------- */

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
            }
        });


    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "attempting to login");
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with login" + e, e);
                    return;
                }
                goMainActivity();
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_LONG);
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();   // ALLOWS LOGIN ACT TO GO OUT OF THE STACK AND AVOID GOING INTO LOGIN AGAIN
    }
}