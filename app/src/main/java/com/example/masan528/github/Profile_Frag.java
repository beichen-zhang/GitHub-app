package com.example.masan528.github;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.*;
import android.text.style.*;
import android.graphics.*;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.io.*;
import java.net.*;
import org.w3c.dom.Text;
import java.util.concurrent.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_Frag extends Fragment {

    private TextView name;
    private TextView user;
    private TextView website;
    private TextView email;
    private TextView public_repos;
    private TextView followers;
    private TextView following;
    private TextView create;
    private TextView bio;
    public static ImageView img;
    public String output;

    public Profile_Frag() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=  inflater.inflate(R.layout.fragment_profile_, container, false);
        name = (TextView) v.findViewById(R.id.name);
        String name_text = getArguments().getString("name");
        name.setText(name_text);

        user = (TextView) v.findViewById(R.id.username);
        String user_text = getArguments().getString("username");
        user.setText(user_text);

        website= (TextView) v.findViewById(R.id.website);
        String website_text=getArguments().getString("website");
        String input = "                website:"+'\n'+website_text;
        SpannableString ss1=  new SpannableString(input);
        ss1.setSpan(new RelativeSizeSpan(1.2f), 0,24, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 24, 0);// set color
        website.setText(ss1);

        email = (TextView) v.findViewById(R.id.email);
        String email_text = getArguments().getString("email");
        String input2 = "             email:"+'\n'+email_text;
        SpannableString ss3=  new SpannableString(input2);
        ss3.setSpan(new RelativeSizeSpan(1.2f), 0,20, 0); // set size
        ss3.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 20, 0);// set color

        email.setText(ss3);

        public_repos= (TextView) v.findViewById(R.id.repo_count);
        String repo_text = getArguments().getString("public_repos");
        public_repos.setText("Repo:"+repo_text);

        followers = (TextView)v.findViewById(R.id.follower_count);
        String followers_text = getArguments().getString("followers");
        followers.setText("Follower:"+followers_text);

        following = (TextView) v.findViewById(R.id.following_count);
        String following_text = getArguments().getString("following");
        following.setText("Following:"+following_text);

        create = (TextView) v.findViewById(R.id.create_date);
        String create_text = getArguments().getString("created_at");
        create.setText("Create date:"+create_text);

        bio = (TextView) v.findViewById(R.id.bio);
        String bio_text = getArguments().getString("bio");

        String s = "              Bio:"+'\n'+bio_text;
        SpannableString ss2=  new SpannableString(s);
        ss2.setSpan(new RelativeSizeSpan(1.2f), 0,19, 0); // set size
        ss2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 19, 0);// set color
        bio.setText(ss2);

        img = (ImageView)v.findViewById(R.id.avatar);
        String imgurl = getArguments().getString("avatar_url");
        this.output = imgurl;

        try {
            Void ret = new ImgTask().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }




        return v;
    }

    private class ImgTask extends AsyncTask<Void,Void,Void>{
        Bitmap bitmap;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(output).getContent());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            img.setImageBitmap(bitmap);
            super.onPostExecute(aVoid);
        }
    }

}
