package com.example.masan528.github;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import android.widget.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class following extends Fragment {
    private String clientId = "3de3290a900bbebd5131";
    private String clientSecret = "08e513c37e66b5fcd2299e6ffed52c2ba0ac0167";
    private String redirectUrl = "https://github.com/beichen-zhang";
    private String OAuth = "?client_id="+clientId+"&client_secret="+clientSecret;
    private String API = "https://api.github.com/users";
    private String API2 = "https://api.github.com/user";
    private String username = "beichen-zhang";
    public static String result;
    public String url = API+"/"+username+"/following"+OAuth;
    private RelativeLayout relativeLayout;
    public String output;

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
                relativeLayout.addView(im);

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

}
