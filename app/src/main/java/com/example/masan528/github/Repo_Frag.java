package com.example.masan528.github;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import android.os.AsyncTask;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.*;
import android.text.method.*;
import android.graphics.drawable.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class Repo_Frag extends Fragment {

    private String clientId = "xxx";
    private String clientSecret = "xxx";
    private String redirectUrl = "https://github.com/beichen-zhang";
    private String OAuth = "?client_id="+clientId+"&client_secret="+clientSecret;
    private String API = "https://api.github.com/users";
    private String API2 = "https://api.github.com/user";
    private String username = "beichen-zhang";
    public String url = API+"/"+username+OAuth;
    public static String result;
    public String repo_url = "https://api.github.com/users/"+username+"/repos"+OAuth;
    private RelativeLayout scroll;
    public static ArrayList<String> url_list;
    public static final String SHARED_PREFS = "sharedPrefs";
    public Repo_Frag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_repo_, container, false);

        try {
            Void ret = new getRepo().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<String> list =parse_result(result);
        scroll = (RelativeLayout) v.findViewById(R.id.scroll);
        TextView title = new TextView(getActivity());
        title.setText("Public Repositories");
        title.setTextSize(25);
        title.setTextColor(Color.BLACK);
        RelativeLayout.LayoutParams params_ = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT,  RelativeLayout.LayoutParams.WRAP_CONTENT);
        params_.setMargins(250,0,0,0);
        title.setLayoutParams(params_);
        scroll.addView(title);

        ShapeDrawable arrowDrawable = new ShapeDrawable();
        arrowDrawable.getPaint().setColor(Color.BLACK);

        View line = new View(getActivity());
        RelativeLayout.LayoutParams line_para = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT,  1);
        line_para.setMargins(0,190,0,0);
        line.setBackgroundDrawable(arrowDrawable);
        line.setLayoutParams(line_para);
        scroll.addView(line);

        for (int i =0; i<list.size();i++){
            TextView t = new TextView(getActivity());
            t.setId(i);
            String text = "";
            if(i%3 ==0){
                text = list.get(i);
                t.setTextSize(20);
            }
            else if (i%3 == 1){
                text = "Repo Owner:"+'\n'+list.get(i);
            }
            else{
                text = "Description:"+'\n'+list.get(i);
            }
            t.setText(text);
            if (i%3==0){
                String url = url_list.get(i/3);
                String value = "<html><a href=\""+url+"\">"+text+"</a></html>";
                t.setText(Html.fromHtml(value));
                t.setMovementMethod(LinkMovementMethod.getInstance());
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT,  RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,200+180*i,0,0);

            t.setLayoutParams(params );
            scroll.addView(t);
        }
        saveData();
        return v;
    }

    private ArrayList<String> parse_result(String result) {
        ArrayList<String> ret_val = new ArrayList<String>();
        List<String> list = new ArrayList<String>(Arrays.asList(result.split("default_branch")));
        url_list = new ArrayList<String>();
        for (int i =0; i< list.size()-1; i++){
            int name_index = list.get(i).indexOf("name");
            String name = list.get(i).substring(name_index+7,list.get(i).indexOf(',',name_index+1)-1);
            ret_val.add(name);

            int log_index = list.get(i).indexOf("login");
            String user = list.get(i).substring(log_index+8, list.get(i).indexOf(',',log_index+1)-1);
            ret_val.add(user);

            int des_index = list.get(i).indexOf("description");
            String description = list.get(i).substring(des_index+14, list.get(i).indexOf(',',des_index+1)-1);
            ret_val.add(description);

            int url_index = list.get(i).indexOf("html_url");
            String url_str= list.get(i).substring(url_index+11, list.get(i).indexOf(',',url_index+1)-1);
            url_list.add(url_str);
        }
        int a =1;
        return ret_val;
    }

    private class getRepo extends AsyncTask <Void,Void,Void>{
        String textResult;
        @Override
        protected Void doInBackground(Void... voids) {
            URL textUrl;

            try{
                textUrl = new URL(repo_url);
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
                Repo_Frag.result = stringText;

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
            Repo_Frag.result =textResult;
            super.onPostExecute(aVoid);
        }
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("repo_result",Repo_Frag.result);
        editor.apply();
    }

    public void loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS,Context.MODE_PRIVATE);
        Repo_Frag.result = sharedPreferences.getString("repo_result","");
    }

}
