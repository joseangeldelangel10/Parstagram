package com.example.parstagram.models;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.DetailedViewActivity;
import com.example.parstagram.R;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public FeedAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.feed_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private TextView tvTimestamp;
        private ImageView ivImage;
        private TextView tvDescription;
        private ImageButton like;
        private ImageButton comment;
        private ImageButton dm;
        private boolean liked = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivPostPhoto);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            like = itemView.findViewById(R.id.btLike);
            comment = itemView.findViewById(R.id.btComment);
            dm = itemView.findViewById(R.id.btDM);
        }

        public void bind(Post post) {
            // Bind the post data to the view elements

            SpannableString s = new SpannableString(post.getUser().getUsername());
            s.setSpan(new TypefaceSpan("bold"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //s.setSpan(new RelativeSizeSpan(2f), 0,10, 0);
            tvDescription.setText(s + ".- " + post.getDescription());
            tvUsername.setText(s);
            tvTimestamp.setText(" · " + post.calculateTimeAgo());

            Glide.with(context).load(R.drawable.unfi_like).into(like);
            Glide.with(context).load(R.drawable.unfi_comment).into(comment);
            Glide.with(context).load(R.drawable.unfi_dm).into(dm);

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!liked){
                        Glide.with(context).load(R.drawable.fi_like).into(like);
                    }else{
                        Glide.with(context).load(R.drawable.unfi_like).into(like);
                    }
                    liked = !liked;
                }
            });


            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }
            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailedViewActivity.class);
                    //intent.putExtra("post", Parcels.wrap(post));
                    intent.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                    context.startActivity(intent);
                }
            });
        }
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
