package com.example.parstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.parstagram.MainActivity;
import com.example.parstagram.functionalityClasses.EndlessRecyclerViewScrollListener;
import com.example.parstagram.models.FeedAdapter;
import com.example.parstagram.models.Post;
import com.example.parstagram.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    public final String TAG = "FeedFragment";
    protected EndlessRecyclerViewScrollListener scrollListener;
    protected FeedAdapter adapter;
    protected List<Post> allPosts;
    protected SwipeRefreshLayout swipeContainer;
    protected BottomNavigationView bottomNavigationItemView;
    RecyclerView rvFeed;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        bottomNavigationItemView = view.findViewById(R.id.bottom_navigation);

        /* ------------------------------------------------------------------------------------------------------------------------------------
                                                        WE CONFIGURE RV & ADAPTER
        ------------------------------------------------------------------------------------------------------------------------------------*/
        rvFeed = view.findViewById(R.id.rvFeed);
        allPosts = new ArrayList<>();
        adapter = new FeedAdapter(getContext(), allPosts);
        rvFeed.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvFeed.setLayoutManager(linearLayoutManager);

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
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
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

    public void fetchNewData() {
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

    public void queryPosts() {
        // specify what type of data we want to query - Post.class
        MainActivity.showProgressBar();
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

    public void fetchTimelineAsync(int page) {
        adapter.clear();
        queryPosts();
        swipeContainer.setRefreshing(false);
    }
}