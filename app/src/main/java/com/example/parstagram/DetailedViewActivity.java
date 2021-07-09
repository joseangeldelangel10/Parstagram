package com.example.parstagram;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.parstagram.models.Post;
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

        ActionBar actionBar = getSupportActionBar();
        SpannableString s = new SpannableString(getString(R.string.app_name));
        s.setSpan(new TypefaceSpan("cursive"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new RelativeSizeSpan(2f), 0,getString(R.string.app_name).length(), 0);
        actionBar.setTitle(s);

        tvUsername = findViewById(R.id.tvDetailedViewUsername);
        tvTimestamp = findViewById(R.id.tvDetailedViewTimestamp);
        ivImage = findViewById(R.id.ivDetailedViewImage);
        tvDescription = findViewById(R.id.tvDetailedViewDescription);
        rvComments = findViewById(R.id.rvDetailedViewComments);

        post = Parcels.unwrap(this.getIntent().getParcelableExtra(Post.class.getSimpleName()));
        //post = this.getIntent().getParcelableExtra();
        tvDescription.setText(post.getDescription());
        tvUsername.setText(post.getUser().getUsername());
        tvTimestamp.setText(" · " + post.calculateTimeAgo());

        image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivImage);
        }


    }

}