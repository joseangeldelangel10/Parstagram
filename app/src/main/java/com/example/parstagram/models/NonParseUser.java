package com.example.parstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("NonParseUser")
public class NonParseUser extends ParseObject {
    public static final String PP_KEY = "ProfilePic";

    /*public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void  setDescription(String description){
        put(KEY_DESCRIPTION,description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }*/

    public ParseFile getImage() {
        return getParseFile(PP_KEY);
    }
}
