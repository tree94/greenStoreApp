package com.seoul.greenstore.greenstore.Server;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by user on 2016-08-31.
 */
public class Server extends AsyncTask<String, Void, String> {

    private ProgressDialog waitDlg = null;
    private Activity activity;

    ILoadResult loadResult;

    public Server(Activity mActivit, ILoadResult loadResult) {
        activity = mActivit;
        this.loadResult = loadResult;
    }

    public void setOnLOadResultListener(ILoadResult loadResult) {
        this.loadResult = loadResult;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        waitDlg = new ProgressDialog(activity);
        waitDlg.setMessage("데이터 요청 중...");
        waitDlg.show();
    }

    @Override
    protected String doInBackground(String... values) {
        return request(values);
    }

    @Override
    protected void onPostExecute(String aResult) {
        System.out.println(aResult + "//////////////////");
        if (waitDlg != null) {
            waitDlg.dismiss();
            waitDlg = null;
        }
        loadResult.customAddList(aResult);
    }


    protected String request(String... values) {
        final int TIME_OUT = 20;
        final String SERVER_URL = values[0];
        JSONArray jsonArray = null;
        StringBuffer sb = new StringBuffer();


        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(TIME_OUT * 10000);
            httpURLConnection.setReadTimeout(TIME_OUT * 10000);
            httpURLConnection.setRequestMethod(values[1]);
            Log.d("reviewDelete method:", values[1]);

            if (values.length > 2 && values[2].equals("memberLookup")) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("mid").append("=").append(values[3]).append("&");
                buffer.append("mname").append("=").append(values[4]).append("&");
                buffer.append("mphoto").append("=").append(values[5]);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8");
                PrintWriter writer = new PrintWriter(outputStreamWriter);
                writer.write(buffer.toString());
                writer.flush();
            }


            if (values.length > 2 && values[2].equals("reviewInsert")) {
                Log.d("values[2]:", values[3]);
                String data = URLEncoder.encode("mkey", "UTF-8") + "=" + URLEncoder.encode(values[3], "UTF-8").toString();

//                data+="&"+URLEncoder.encode("rkey","UTF-8")+"="+URLEncoder.encode(values[4],"UTF-8");
                data += "&" + URLEncoder.encode("sh_id", "UTF-8") + "=" + URLEncoder.encode(values[4], "UTF-8");
                data += "&" + URLEncoder.encode("rcontent", "UTF-8") + "=" + URLEncoder.encode(values[5], "UTF-8");
                httpURLConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                Log.d("data : ", data);
                wr.write(data);
                wr.flush();

            }

            if (values.length > 2 && values[2].equals("reviewUpdate")) {
                String data = URLEncoder.encode("rkey", "UTF-8") + "=" + URLEncoder.encode(values[3], "UTF-8").toString();

                data += "&" + URLEncoder.encode("rcontent", "UTF-8") + "=" + URLEncoder.encode(values[4], "UTF-8");
//                data+="&"+URLEncoder.encode("rcontent","UTF-8")+"="+URLEncoder.encode(values[5],"UTF-8");
                httpURLConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                Log.d("data : ", data);
                wr.write(data);
                wr.flush();
            }

            if (values.length > 2 && values[2].equals("reviewDelete")) {
                String data = URLEncoder.encode("rkey", "UTF-8") + "=" + URLEncoder.encode(values[3], "UTF-8").toString();
                httpURLConnection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
                wr.write(data);
                wr.flush();

            }
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode < 200 || responseCode >= 300) {
                Toast.makeText(activity, "서버와 연결이 실패하였습니다.", Toast.LENGTH_LONG).show();
                return null;
            }
            InputStream in = httpURLConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in));

            String json;
            while ((json = rd.readLine()) != null) {
                sb.append(json + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString().trim();
    }

    public interface ILoadResult {
        public void customAddList(String result);
    }


}

