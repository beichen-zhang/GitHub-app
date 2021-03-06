package com.example.masan528.github;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import android.widget.*;
import java.util.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class following extends Fragment {
    private String clientId = "xxx";
    private String clientSecret = "xxx";
    private String redirectUrl = "https://github.com/beichen-zhang";
    private String OAuth = "?client_id="+clientId+"&client_secret="+clientSecret;
    private String API = "https://api.github.com/users";
    private String API2 = "https://api.github.com/user";
    private String username = "beichen-zhang";
    public static String result;
    public String url = API+"/"+username+"/following"+OAuth;
    private RelativeLayout relativeLayout;
    public String output;
    private Profile_Frag profile_frag;
    public static final String SHARED_PREFS = "sharedPrefs";

    public following() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_following, container, false);
        relativeLayout = (RelativeLayout)v.findViewById(R.id.layout_id);
        try {
            Void ret = new getFollowing().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<String> list = parse_result(result);

        for (int i =0; i< list.size(); i++){
            if (i%2 ==0){
                ImageView im = new ImageView(getActivity());
                this.output = list.get(i);
                try {
                    Void ret = new ImgTask(im).execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT,  RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(80,10+350*i,0,0);
                im.setLayoutParams(params);
                im.setOnClickListener(new MyLovelyOnClickListener(list.get(i+1)));
                relativeLayout.addView(im);

                Button unfollow = new Button(getActivity());
                RelativeLayout.LayoutParams params_btn = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT,  RelativeLayout.LayoutParams.WRAP_CONTENT);
                params_btn.setMargins(980,10+350*i,0,0);
                unfollow.setLayoutParams(params_btn);
                unfollow.setText("unfollow");
                unfollow.setOnClickListener(new MyButton(list.get(i+1)));
                relativeLayout.addView(unfollow);

            }
            else{
                TextView t = new TextView(getActivity());
                t.setText("User:"+list.get(i));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT,  RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(50,10+350*(i-1)+490,0,0);
                t.setLayoutParams(params);
                relativeLayout.addView(t);
            }

        }
        saveData();

        return v;
    }


    private ArrayList<String> parse_result(String result){
        ArrayList<String> ret_val = new ArrayList<String>();
        List<String> list = new ArrayList<String>(Arrays.asList(result.split("site_admin")));
        for (int i=0; i<list.size()-1;i++){

            int avatar_index = list.get(i).indexOf("avatar_url");
            String img = list.get(i).substring(avatar_index+13,list.get(i).indexOf(',',avatar_index+1)-1);
            ret_val.add(img);

            int login_index = list.get(i).indexOf("login");
            String name = list.get(i).substring(login_index+8,list.get(i).indexOf(',',login_index+1)-1);
            ret_val.add(name);

        }
        return ret_val;
    }

    private class getFollowing extends AsyncTask<Void,Void,Void> {
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
                following.result = stringText;

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
        protected void onPostExecute(Void aVoid) {
            following.result =textResult;
            super.onPostExecute(aVoid);
        }
    }


    public class MyLovelyOnClickListener implements View.OnClickListener
    {

        String myLovelyVariable;
        public MyLovelyOnClickListener(String myLovelyVariable) {
            this.myLovelyVariable = myLovelyVariable;
        }

        @Override
        public void onClick(View v)
        {
            profile_frag = new Profile_Frag();
            Bundle b = new Bundle();
            b.putString("url",API+"/"+myLovelyVariable+OAuth);
            profile_frag.setArguments(b);
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_frame,profile_frag);
            fragmentTransaction.commit();
        }

    };
    private class ImgTask extends AsyncTask<Void,Void,Void>{
        Bitmap bitmap;
        ImageView img;
        public ImgTask(ImageView img){
            this.img = img;
        }


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
            this.img.setImageBitmap(bitmap);
            super.onPostExecute(aVoid);
        }
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("following_result",following.result);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        following.result = sharedPreferences.getString("following_result","");
    }

    public class MyButton implements View.OnClickListener
    {

        String userid;
        public MyButton(String id) {
            this.userid = id;
        }

        @Override
        public void onClick(View v)
        {
            try {
                Void ret = new UnFollow_Task(this.userid).execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    };

    private class UnFollow_Task extends AsyncTask<Void,Void,Void> {
        String id;

        public UnFollow_Task(String id){
            this.id = id;
            Follower_Frag.id_user = this.id;
        }
        @TargetApi(Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL("https://api.github.com/user/following/"+ this.id+OAuth);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

                String password = "ZBCzbc350402";

                //String encoded = Base64.getEncoder().encodeToString((username + ":" + password).getBytes("UTF-8"),android.util.Base64.NO_WRAP);
                String encoded = android.util.Base64.encodeToString((username + ":" + password).getBytes("UTF-8"), android.util.Base64.NO_WRAP);
                httpCon.setRequestProperty("Authorization", "Basic "+encoded);

                httpCon.setDoOutput(true);
                httpCon.setRequestMethod("DELETE");
                //httpCon.getInputStream();
                int code = httpCon.getResponseCode();
                if (code ==204){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Successfully followed!",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }
                else{
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Failed",
                            Toast.LENGTH_SHORT);

                    toast.show();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
