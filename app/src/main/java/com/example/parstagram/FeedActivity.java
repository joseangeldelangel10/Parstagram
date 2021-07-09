package com.example.parstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    private  final String TAG = "FeedActivity";
    private EndlessRecyclerViewScrollListener scrollListener;
    protected FeedAdapter adapter;
    protected List<Post> allPosts;
    private SwipeRefreshLayout swipeContainer;
    private BottomNavigationView bottomNavigationItemView;
    RecyclerView rvFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        /*rvFeed = findViewById(R.id.rvFeed);
        bottomNavigationItemView = findViewById(R.id.bottom_navigation);*/

        /* ------------------------------------------------------------------------------------------------------------------------------------
                                                        WE CONFIGURE RV & ADAPTER
        ------------------------------------------------------------------------------------------------------------------------------------
        allPosts = new ArrayList<>();
        adapter = new FeedAdapter(this, allPosts);
        rvFeed.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvFeed.setLayoutManager(linearLayoutManager);*/

        /* ------------------------------------------------------------------------------------------------------------------------------------
                                                        SWIPE CONTAINER SECTION
        ------------------------------------------------------------------------------------------------------------------------------------
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer); // We get a reference to the "swipe container" view
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            /* --------------------------------------------
            // we setup a refresh listener which through fetchTimelineAsync makes a request for new data
            and if the request succeeds we clear actual tweets (from list and rv) and append new ones
             --------------------------------------------
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        */
        /* ------------------------------------------------------------------------------------------------------------------------------------
                                                 BINDING INFINITE SCROLLING TO RV
        ------------------------------------------------------------------------------------------------------------------------------------
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                //loadNextDataFromApi(page);
                Toast.makeText(FeedActivity.this, "fetching new data", Toast.LENGTH_LONG);
                Log.e(TAG, "fetching new data");
                fetchNewData();
            }
        };
        // Adds the scroll listener to RecyclerView
        rvFeed.addOnScrollListener(scrollListener);*/
        /* ------------------------------------------------------------------------------------------------------------------------------------*/


       //queryPosts();

    }

    private void fetchNewData() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        query.whereLessThan("createdAt", allPosts.get(allPosts.size()-1).getCreatedAt());
        // limit query to latest 20 items
        query.setLimit(7);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                adapter.addAll(posts);
            }
        });
    }

    private void queryPosts() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        // limit query to latest 20 items
        query.setLimit(7);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                adapter.addAll(posts);
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        adapter.clear();
        queryPosts();
        swipeContainer.setRefreshing(false);
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
                Toast.makeText(FeedActivity.this, "logged out successfully", Toast.LENGTH_SHORT);
                Log.i(TAG, "user logged out");
                Intent intent = new Intent(FeedActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }else{
                Toast.makeText(FeedActivity.this, "there was a problem, try again", Toast.LENGTH_SHORT);
                Log.i(TAG, "user couldn't log out");
            }

            return true;
        }
        if (item.getItemId() == R.id.new_post){
            Intent intent = new Intent(FeedActivity.this, MainActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }


}