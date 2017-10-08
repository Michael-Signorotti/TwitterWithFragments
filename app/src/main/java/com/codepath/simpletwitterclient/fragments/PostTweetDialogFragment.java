package com.codepath.simpletwitterclient.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.simpletwitterclient.application.TwitterApp;
import com.codepath.simpletwitterclient.interfaces.PostTweetDialogListener;
import com.codepath.simpletwitterclient.models.Tweet;
import com.codepath.simpletwitterclient.models.User;
import com.codepath.simpletwitterclient.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.codepath.apps.restclienttemplate.R.string.user;


public class PostTweetDialogFragment extends DialogFragment {

    TwitterClient client;
    private ImageView ivClose;
    private ImageView ivProfileImage;
    private TextView tvName;
    private TextView tvScreenName;
    private TextView tvCharCount;
    private EditText etTweet;
    private Button btnTweet;

    private User myAccount;

    public PostTweetDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_tweet, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        client = TwitterApp.getRestClient();
        getUserInformation();
        tvScreenName = (TextView) view.findViewById(R.id.tvMyAccountScreenName);
        tvName = (TextView) view.findViewById(R.id.tvMyAccountName);
        etTweet = (EditText) view.findViewById(R.id.etMyAccountTweet);
        btnTweet = (Button) view.findViewById(R.id.btnMyAccountTweet);
        ivClose = (ImageView) view.findViewById(R.id.ivMyAccountClose);
        ivProfileImage = (ImageView) view.findViewById(R.id.ivMyAccountProfileImage);
        tvCharCount = (TextView) view.findViewById(R.id.tvCharCount);


        //create a click listener for closing the dialog fragment
        View.OnClickListener closeClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        };
        ivClose.setOnClickListener(closeClickListener);

        //create a click listener for posting Tweets
        View.OnClickListener postTweetClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                String status = etTweet.getText().toString();
                client.postTweet(status, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            Tweet tweet = Tweet.fromJson(response);

                            PostTweetDialogListener listener = (PostTweetDialogListener) getActivity();
                            listener.onPostTweet(tweet);
                            dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("PostTweetDialogFragment", errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.e("PostTweetDialogFragment", errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("PostTweetDialogFragment", responseString);
                        throwable.printStackTrace();
                    }
                });
            }
        };
        btnTweet.setOnClickListener(postTweetClickListener);


        //add a listener for counting characters in etTweet
        etTweet.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int charCount = etTweet.getText().length();
                String charCountStr = charCount + "/140 characters";
                tvCharCount.setText(charCountStr);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void getUserInformation() {
        client.getUserInformation(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    myAccount = User.fromJson(response);

                    tvScreenName.setText(myAccount.screenName);
                    tvName.setText(myAccount.name);

                    Glide.with(getContext())
                            .load(myAccount.profileImageUrl).override(150, 150)
                            .into(ivProfileImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TimelineActivity", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TimelineActivity", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TimelineActivity", responseString);
                throwable.printStackTrace();
            }
        });
    }
}