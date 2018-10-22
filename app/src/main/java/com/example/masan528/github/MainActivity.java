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
    private String redirectUrl = "https://github.com/beichen-zhang";
    private String OAuth = "?client_id="+clientId+"&client_secret="+clientSecret;
    private String API = "https://api.github.com/users";
    private String API2 = "https://api.github.com/user";
    private String username = "beichen-zhang";
    public String url = API+"/"+username+OAuth;
    public static String result;
    public String repo_url = "https://api.github.com/users/"+username+"/repos"+OAuth;
    //url = 'https://api.github.com/users/yzhan189/repos?client_id=xxxx&client_secret=xxxx';
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

        String url2 = API+"/"+username+OAuth;
        try {
            Void ret = new MyTask().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<String> attr = parse_result(this.result);
        Bundle b = new Bundle();
        b.putString("name",attr.get(18));
        b.putString("username",attr.get(0));
        b.putString("website",attr.get(6));
        b.putString("email",attr.get(22));
        b.putString("public_repos",attr.get(25));
        b.putString("followers",attr.get(27));
        b.putString("following",attr.get(28));
        b.putString("created_at",attr.get(29));
        b.putString("bio",attr.get(24));
        b.putString("avatar_url",attr.get(3));
        profile_frag.setArguments(b);
        setFrag(profile_frag);




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

    private ArrayList<String> parse_result(String s) {
        //{"login":"beichen-zhang","id":28381878,"node_id":"MDQ6VXNlcjI4MzgxODc4","avatar_url":"https://avatars1.githubusercontent.com/u/28381878?v=4","gravatar_id":"","url":"https://api.github.com/users/beichen-zhang","html_url":"https://github.com/beichen-zhang","followers_url":"https://api.github.com/users/beichen-zhang/followers","following_url":"https://api.github.com/users/beichen-zhang/following{/other_user}","gists_url":"https://api.github.com/users/beichen-zhang/gists{/gist_id}","starred_url":"https://api.github.com/users/beichen-zhang/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/beichen-zhang/subscriptions","organizations_url":"https://api.github.com/users/beichen-zhang/orgs","repos_url":"https://api.github.com/users/beichen-zhang/repos","events_url":"https://api.github.com/users/beichen-zhang/events{/privacy}","received_events_url":"https://api.github.com/users/beichen-zhang/received_events","type":"User","site_admin":false,"name":"Beichen Zhang","company":null,"blog":"https://www.linkedin.com/in/beichen-zhang/","location":"Urbana Champaign","email":"bzhang64@illinois.edu","hireable":null,"bio":"Senior student in U of I.","public_repos":5,"public_gists":0,"followers":0,"following":0,"created_at":"2017-05-04T04:51:25Z","updated_at":"2018-10-20T22:12:11Z"
        String input = s.replace('"','|');
        List<String> list = new ArrayList<String>(Arrays.asList(input.split(",")));
        ArrayList<String> result = new ArrayList<String>();
        for (int i=0; i< list.size(); i++){
            String attribute = list.get(i);
            int colon = attribute.indexOf(':');
            if(colon != attribute.length()-3){
                char a = attribute.charAt(colon+1);
                if (attribute.charAt(colon+1)=='|'){
                    String value = attribute.substring(colon+2,attribute.length()-1);
                    result.add(value);
                }
                else{
                    String value = attribute.substring(colon+1,attribute.length());
                    result.add(value);
                }

            }
            else{
                result.add("");
            }

        }
        return result;
    }

    public void setFrag(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    private class MyTask extends AsyncTask <Void,Void,Void>{
        String textResult;
        @Override
        protected Void doInBackground(Void... voids) {
            URL textUrl;

            try{
                textUrl = new URL(url);
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(textUrl.openStream())
                );
                String stringBuffer;
                String stringText = "";
                while ((stringBuffer = bufferedReader.readLine())!= null){
                    stringText += stringBuffer;
                }
                bufferedReader.close();
                textResult = stringText;
                MainActivity.result = stringText;

            }catch (MalformedURLException e){
                e.printStackTrace();
                textResult=e.toString();
            }catch (IOException e){
                e.printStackTrace();
                textResult=e.toString();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result_) {
            MainActivity.result = textResult;

            super.onPostExecute(result_);
        }
    }

}
