package com.example.masan528.github;


import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public static String result;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    public Profile_Frag() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=  inflater.inflate(R.layout.fragment_profile_, container, false);
        String url=getArguments().getString("url");

        try {
            Void ret = new MyTask(url).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<String> attr = parse_result(this.result);

        name = (TextView) v.findViewById(R.id.name);
        String name_text = attr.get(18);
        name.setText(name_text);

        user = (TextView) v.findViewById(R.id.username);
        String user_text = attr.get(0);
        user.setText(user_text);

        website= (TextView) v.findViewById(R.id.website);
        String website_text=attr.get(6);
        String input = "                website:"+'\n'+website_text;
        SpannableString ss1=  new SpannableString(input);
        ss1.setSpan(new RelativeSizeSpan(1.2f), 0,24, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 24, 0);// set color
        website.setText(ss1);

        email = (TextView) v.findViewById(R.id.email);
        String email_text = attr.get(22);
        String input2 = "             email:"+'\n'+email_text;
        SpannableString ss3=  new SpannableString(input2);
        ss3.setSpan(new RelativeSizeSpan(1.2f), 0,20, 0); // set size
        ss3.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 20, 0);// set color

        email.setText(ss3);

        public_repos= (TextView) v.findViewById(R.id.repo_count);
        String repo_text = attr.get(25);
        public_repos.setText("Repo:"+repo_text);

        followers = (TextView)v.findViewById(R.id.follower_count);
        String followers_text = attr.get(27);
        followers.setText("Follower:"+followers_text);

        following = (TextView) v.findViewById(R.id.following_count);
        String following_text = attr.get(28);
        following.setText("Following:"+following_text);

        create = (TextView) v.findViewById(R.id.create_date);
        String create_text = attr.get(29);
        create.setText("Create date:"+create_text);

        bio = (TextView) v.findViewById(R.id.bio);
        String bio_text = attr.get(24);

        String s = "              Bio:"+'\n'+bio_text;
        SpannableString ss2=  new SpannableString(s);
        ss2.setSpan(new RelativeSizeSpan(1.2f), 0,19, 0); // set size
        ss2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 19, 0);// set color
        bio.setText(ss2);

        img = (ImageView)v.findViewById(R.id.avatar);
        String imgurl = attr.get(3);
        this.output = imgurl;

        try {
            Void ret = new ImgTask().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        saveData();
        //loadData();


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

    private ArrayList<String> parse_result(String s) {
        //{"login":"beichen-zhang","id":28381878,"node_id":"MDQ6VXNlcjI4MzgxODc4","avatar_url":"https://avatars1.githubusercontent.com/u/28381878?v=4","gravatar_id":"","url":"https://api.github.com/users/beichen-zhang","html_url":"https://github.com/beichen-zhang","followers_url":"https://api.github.com/users/beichen-zhang/followers","following_url":"https://api.github.com/users/beichen-zhang/following{/other_user}","gists_url":"https://api.github.com/users/beichen-zhang/gists{/gist_id}","starred_url":"https://api.github.com/users/beichen-zhang/starred{/owner}{/repo}","subscriptions_url":"https://api.github.com/users/beichen-zhang/subscriptions","organizations_url":"https://api.github.com/users/beichen-zhang/orgs","repos_url":"https://api.github.com/users/beichen-zhang/repos","events_url":"https://api.github.com/users/beichen-zhang/events{/privacy}","received_events_url":"https://api.github.com/users/beichen-zhang/received_events","type":"User","site_admin":false,"name":"Beichen Zhang","company":null,"blog":"https://www.linkedin.com/in/beichen-zhang/","location":"Urbana Champaign","email":"bzhang64@illinois.edu","hireable":null,"bio":"Senior student in U of I.","public_repos":5,"public_gists":0,"followers":0,"following":0,"created_at":"2017-05-04T04:51:25Z","updated_at":"2018-10-20T22:12:11Z"
        String input = s.replace('"','|');
        List<String> list = new ArrayList<String>(Arrays.asList(input.split(",")));
        ArrayList<String> result = new ArrayList<String>();
        for (int i=0; i< list.size(); i++){
            String attribute = list.get(i);
            int colon = attribute.indexOf(':');
            if(colon != attribute.length()-3 ){
                char a = attribute.charAt(colon+1);
                if (a=='|'){
                    String value = attribute.substring(colon+2,attribute.length()-1);
                    result.add(value);
                }
                else{
                    String value = attribute.substring(colon+1,attribute.length());
                    result.add(value);
                }

            }
            else{
                if (attribute.charAt(colon+1)=='|'){
                    result.add("");
                }
                else{
                    String value = attribute.substring(colon+1,attribute.length());
                    result.add(value);
                }
            }

        }
        return result;
    }

    private class MyTask extends AsyncTask <Void,Void,Void>{
        String textResult;
        String url;

        public MyTask (String url){
            this.url = url;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            URL textUrl;

            try{
                textUrl = new URL(this.url);
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
                Profile_Frag.result = stringText;

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
            Profile_Frag.result = textResult;

            super.onPostExecute(result_);
        }
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name",name.getText().toString());
        editor.putString("user",user.getText().toString());
        editor.putString("website",website.getText().toString());
        editor.putString("email",email.getText().toString());
        editor.putString("public_repos",public_repos.getText().toString());
        editor.putString("followers",followers.getText().toString());
        editor.putString("following",following.getText().toString());
        editor.putString("create",create.getText().toString());
        editor.putString("bio",bio.getText().toString());
        editor.apply();

    }

    public void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        name.setText(sharedPreferences.getString("name","")+"!");
        user.setText(sharedPreferences.getString("user","")+"!");
    }
}
