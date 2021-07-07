package com.example.parstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    EditText etPhotoDescription;
    Button btTakePhoto;
    Button btSubmitPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPhotoDescription = findViewById(R.id.etPhotoDescription);
        btTakePhoto = findViewById(R.id.btTakePhoto);
        btSubmitPhoto  = findViewById(R.id.btSubmitPhoto);

        /*--------------------- ACTION BAR (ICON) ------------------------*/
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.drawable.ic_action_name);
        actionBar.setDisplayUseLogoEnabled(true);
        /*---------------------------------------------------------*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.timeline_menu, menu);
            return true;
        }
        catch (Exception exception){
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // we check if the item seleted is the compose item (pencil drawing)
        if (item.getItemId() == R.id.logout_icon) {
            //compose item has been selected
            //Toast.makeText(this, "compose", Toast.LENGTH_SHORT).show();
            ParseUser.logOut();
            if (ParseUser.getCurrentUser() == null){
                Toast.makeText(MainActivity.this, "logged out successfully", Toast.LENGTH_SHORT);
                Log.i(TAG, "user logged out");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }else{
                Toast.makeText(MainActivity.this, "there was a problem, try again", Toast.LENGTH_SHORT);
                Log.i(TAG, "user couldn't log out");
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}