package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.Date;

public class DetailedViewActivity extends AppCompatActivity {
    TextView tvUsername;
    TextView tvTimestamp;
    ImageView ivImage;
    TextView tvDescription;
    RecyclerView rvComments;
    Post post;
    ParseFile image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);

        tvUsername = findViewById(R.id.tvDetailedViewUsername);
        tvTimestamp = findViewById(R.id.tvDetailedViewTimestamp);
        ivImage = findViewById(R.id.ivDetailedViewImage);
        tvDescription = findViewById(R.id.tvDetailedViewDescription);
        rvComments = findViewById(R.id.rvDetailedViewComments);

        post = Parcels.unwrap(this.getIntent().getParcelableExtra(Post.class.getSimpleName()));
        //post = this.getIntent().getParcelableExtra();
        tvDescription.setText(post.getDescription());
        tvUsername.setText(post.getUser().getUsername());
        tvTimestamp.setText(" · " + calculateTimeAgo(post.getCreatedAt()));

        image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivImage);
        }


    }

    public static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }

}