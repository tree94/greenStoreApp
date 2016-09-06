package com.seoul.greenstore.greenstore.Server;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.seoul.greenstore.greenstore.MainActivity;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 2016-08-31.
 */
public class Server extends AsyncTask<String, Void, String> {

    private ProgressDialog waitDlg = null;
    Activity activity;

    public Server(Activity mActivit){
        activity = mActivit;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        waitDlg = new ProgressDialog(activity);
        waitDlg.setMessage("데이터 요청 중...");
        waitDlg.show();
    }

    @Override
    protected String doInBackground(String...values) {
        return request(values);

    }

    @Override
    protected void onPostExecute(String aResult) {
        if(waitDlg != null){
            waitDlg.dismiss();
            waitDlg = null;
        }
        ((MainActivity)activity).addList(aResult);
    }


    protected String request(String...values) {
        final int TIME_OUT = 20;
        final String SERVER_URL = "http://172.30.22.170:8080/greenStore/app";
        JSONArray jsonArray = null;
        StringBuffer sb= new StringBuffer();

        try{
            URL url = new URL(SERVER_URL+values[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(TIME_OUT * 10000);
            httpURLConnection.setReadTimeout(TIME_OUT * 10000);
            httpURLConnection.setRequestMethod(values[1]);
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();

            if(responseCode<200 || responseCode>=300){
                Toast.makeText(activity,"서버와 연결이 실패하였습니다.",Toast.LENGTH_LONG).show();
                return null;
            }
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in));

            String json;
            while((json=rd.readLine())!=null){
                sb.append(json+"\n");
            }



        }catch (Exception e){
            e.printStackTrace();
        }

        return sb.toString().trim();
    }
}

