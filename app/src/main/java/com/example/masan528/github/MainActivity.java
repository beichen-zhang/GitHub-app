package com.example.masan528.github;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private BottomNavigationView main_nav;
    private FrameLayout main_frame;

    private Profile_Frag profile_frag;
    private following foll_ing;
    private Follower_Frag follower_frag;
    private Repo_Frag repo_frag;

    private String clientId = "3de3290a900bbebd5131";
    private String clientSecret = "08e513c37e66b5fcd2299e6ffed52c2ba0ac0167";
    private String redirectUrl = "githubapiclient://callback";
    private String OAuth = "?client_id="+clientId+"&client_secret="+clientSecret;
    private String API = "https://api.github.com/users";
    private String API2 = "https://api.github.com/user";
    private String username = "beichen-zhang";
    public String url = API+"/"+username+OAuth;
    public static String result;
    private static boolean resume = false;
    //https://api.github.com/users/beichen-zhang?
    public String repo_url = "https://api.github.com/users/"+username+"/repos"+OAuth;
    //url = 'https://api.github.com/users/beichen-zhang?client_id=3de3290a900bbebd5131&client_secret=08e513c37e66b5fcd2299e6ffed52c2ba0ac0167';
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        main_frame = (FrameLayout) findViewById(R.id.main_frame);
        main_nav = (BottomNavigationView) findViewById(R.id.nav);

        profile_frag = new Profile_Frag();
        foll_ing = new following();
        follower_frag = new Follower_Frag();
        repo_frag = new Repo_Frag();



        setFrag(profile_frag);

        if (MainActivity.resume == false) {
           // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/login/oauth/authorize" + "?client_id=" + clientId + "&scope=user&redirect_uri=" + redirectUrl));
           // startActivity(intent);
        }




        main_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.profile:
                        setFrag(profile_frag);
                        return true;

                    case R.id.follower:
                        setFrag(follower_frag);
                        return true;

                    case R.id.following:
                        setFrag(foll_ing);
                        return true;

                    case R.id.repo:
                        setFrag(repo_frag);
                        return true;

                    default:
                        return true;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.resume = true;
    }





    public void setFrag(Fragment fragment) {
        Bundle b = new Bundle();
        b.putString("url",this.url);
        fragment.setArguments(b);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

}
