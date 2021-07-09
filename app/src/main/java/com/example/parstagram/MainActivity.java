package com.example.parstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.parstagram.fragments.ComposeFragment;
import com.example.parstagram.fragments.HomeFragment;
import com.example.parstagram.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    MenuItem miActionProgressItem;
    BottomNavigationView bottomNavigationItemView;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationItemView = findViewById(R.id.bottom_navigation);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        /*--------------------- ACTION BAR (ICON) ------------------------*/
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        SpannableString s = new SpannableString("Parstagram");
        s.setSpan(new TypefaceSpan("cursive"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new RelativeSizeSpan(2f), 0,10, 0);
        actionBar.setTitle(s);

        /*---------------------------------------------------------*/
        
        bottomNavigationItemView.setBackgroundColor(getResources().getColor(R.color.pinkish));
        //getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        bottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_profile:
                        Log.i(TAG, "profile selected");
                        fragment = new ProfileFragment();
                        break;
                    case R.id.action_compose:
                        Log.i(TAG, "compose selected");
                        fragment = new ComposeFragment();
                        //fragment = fragment2;
                        break;
                    case R.id.action_home:
                    default:
                        fragment = new HomeFragment();
                        Log.i(TAG, "home selected");
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationItemView.setSelectedItemId(R.id.action_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.timeline_menu, menu);
            MenuItem newPost = menu.findItem(R.id.new_post);
            newPost.setVisible(false);
            miActionProgressItem = menu.findItem(R.id.miActionProgress);
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

    public void showProgressBar() {
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(true);
        }
    }

    public void hideProgressBar() {
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(false);
        }
    }

}