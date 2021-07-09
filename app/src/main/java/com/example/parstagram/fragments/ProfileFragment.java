package com.example.parstagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.parstagram.MainActivity;
import com.example.parstagram.functionalityClasses.EndlessRecyclerViewScrollListener;
import com.example.parstagram.models.FeedAdapter;
import com.example.parstagram.models.Post;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends HomeFragment{
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvFeed = view.findViewById(R.id.rvFeed);
        bottomNavigationItemView = view.findViewById(R.id.bottom_navigation);

        /* ------------------------------------------------------------------------------------------------------------------------------------
                                                        WE CONFIGURE RV & ADAPTER
        ------------------------------------------------------------------------------------------------------------------------------------*/
        allPosts = new ArrayList<>();
        adapter = new FeedAdapter(getContext(), allPosts);
        rvFeed.setAdapter(adapter);
        LinearLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvFeed.setLayoutManager(gridLayoutManager);

        /* ------------------------------------------------------------------------------------------------------------------------------------
                                                        SWIPE CONTAINER SECTION
        ------------------------------------------------------------------------------------------------------------------------------------*/
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer); // We get a reference to the "swipe container" view
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            /* --------------------------------------------
            // we setup a refresh listener which through fetchTimelineAsync makes a request for new data
            and if the request succeeds we clear actual tweets (from list and rv) and append new ones
             -------------------------------------------- */
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        /* ------------------------------------------------------------------------------------------------------------------------------------
                                                 BINDING INFINITE SCROLLING TO RV
        ------------------------------------------------------------------------------------------------------------------------------------*/
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                //loadNextDataFromApi(page);
                Toast.makeText(getContext(), "fetching new data", Toast.LENGTH_LONG);
                Log.e(TAG, "fetching new data");
                fetchNewData();
            }
        };
        // Adds the scroll listener to RecyclerView
        rvFeed.addOnScrollListener(scrollListener);
        /* ------------------------------------------------------------------------------------------------------------------------------------*/


        queryPosts();
    }

    @Override
    public void queryPosts(){
        MainActivity.showProgressBar();
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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
                    MainActivity.hideProgressBar();
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                adapter.addAll(posts);
                MainActivity.hideProgressBar();
            }
        });
    }

    @Override
    public void fetchNewData() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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
}
