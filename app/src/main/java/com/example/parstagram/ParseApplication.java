package com.example.parstagram;

import android.app.Application;

import com.example.parstagram.models.Post;
import com.example.parstagram.models.NonParseUser;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(NonParseUser.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId( getString(R.string.parse_aplication_id) )
                .clientKey( getString(R.string.parse_client_key) )
                .server( getString(R.string.parse_server))
                .build()
        );
    }
}
