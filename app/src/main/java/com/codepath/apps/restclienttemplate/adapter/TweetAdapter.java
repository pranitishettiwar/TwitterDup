package com.codepath.apps.restclienttemplate.adapter;

import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.helper.PatternEditableBuilder;
import com.codepath.apps.restclienttemplate.models.Tweet;

/**
 * Created by praniti on 9/27/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;
    private TweetAdapterListener mListener;

    //Define an interface required by ViewHolder
    public interface TweetAdapterListener {
        void onItemSelected(View view, int position);
    }

    public TweetAdapter(List<Tweet> tweets, TweetAdapterListener listener) {
        mTweets = tweets;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get the data according to position
        Tweet tweet = mTweets.get(position);

        //populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvName.setText("@" + tweet.user.screenName);

        holder.tvTimeAgo.setText(tweet.timeAgo);

        Glide.with(context).load(tweet.user.getProfileImageUrl()).into(holder.ivProfileImage);

        // Style clickable spans based on pattern
        new PatternEditableBuilder().
            addPattern(Pattern.compile("\\@(\\w+)"), Color.parseColor("#c62c09"), new PatternEditableBuilder.SpannableClickedListener() {
                @Override
                public void onSpanClicked(String text) {
                    Toast.makeText(context, "Clicked username: " + text, Toast.LENGTH_SHORT).show();
                }
            }).into(holder.tvBody);

        new PatternEditableBuilder().
            addPattern(Pattern.compile("\\#(\\w+)"), Color.parseColor("#c62c09"), new PatternEditableBuilder.SpannableClickedListener() {
                @Override
                public void onSpanClicked(String text) {
                    Toast.makeText(context, "Clicked username: " + text, Toast.LENGTH_SHORT).show();
                }
            }).into(holder.tvBody);

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimeAgo;
        public TextView tvName;

        public ViewHolder(final View itemView) {
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTimeAgo = (TextView) itemView.findViewById(R.id.tvTimeAgo);
            tvName = (TextView) itemView.findViewById(R.id.tvName);

            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        //get position of row element
                        int position = getAdapterPosition();
                        //fire the listener callback
                        mListener.onItemSelected(view, position);
                    }
                }
            });

        }

    }

}
