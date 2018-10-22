package com.example.masan528.github;


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

/**
 * A simple {@link Fragment} subclass.
 */
public class Repo_Frag extends Fragment {

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
    private RelativeLayout scroll;
    public static ArrayList<String> url_list;
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
        for (int i =0; i<list.size();i++){
            TextView t = new TextView(getActivity());
            t.setId(i);
            String text = "";
            if(i%3 ==0){
                text = list.get(i);
                t.setTextSize(25);
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
            if (i==0) {
                //params.addRule(RelativeLayout.ALIGN_TOP,scroll.getId());
                //params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                params.setMargins(0,10,0,0);
            }
            else{

                params.setMargins(0,10+180*i,0,0);
            }
            t.setLayoutParams(params );
            scroll.addView(t);
        }

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

}
