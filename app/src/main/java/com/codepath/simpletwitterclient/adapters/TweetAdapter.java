package com.codepath.simpletwitterclient.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.simpletwitterclient.interfaces.TweetAdapterListener;
import com.codepath.simpletwitterclient.models.Tweet;

import java.util.List;

/**
 * Created by michaelsignorotti on 9/30/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;

    Context context;

    TweetAdapterListener listener;


    public TweetAdapter(List<Tweet> tweets, TweetAdapterListener listener) {
        this.mTweets = tweets;
        this.listener = listener;
    }

    //for each row, inflate the layout and cache references into ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    //bind the values based on the position of the element
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //get the data according to the position
        Tweet tweet = mTweets.get(position);

        //populate the views according to this data
        holder.tvUserName.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvRelativeTimestamp.setText(tweet.relativeTimestamp);
        holder.tvScreenName.setText("@" + tweet.user.screenName);


        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .into(holder.ivProfileImage);

    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }


    //create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvRelativeTimestamp;
        public TextView tvScreenName;

        public ViewHolder(View itemView) {
            super(itemView);

            //perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvRelativeTimestamp = (TextView) itemView.findViewById(R.id.tvRelativeTimestamp);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);

            if (!listener.getClass().toString().contains("UserTimelineFragment")) {
                ivProfileImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            listener.onItemSelected(view, position);
                        }
                    }
                });
            }
        }
    }
}
