package com.seoul.greenstore.greenstore;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.seoul.greenstore.greenstore.Commons.Constants;
import com.seoul.greenstore.greenstore.Dto.Notice;
import com.seoul.greenstore.greenstore.Notice.ListViewAdapter;
import com.seoul.greenstore.greenstore.Server.Server;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by X on 2016-09-08.
 */
public class NoticeFragment extends Fragment implements Server.ILoadResult {
    private ListViewAdapter adapter = null;
    private ArrayList<Notice> items = new ArrayList<Notice>();

    public NoticeFragment newInstance() {
        NoticeFragment fragment = new NoticeFragment();
        return fragment;
    }


    public NoticeFragment() {

// Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        String[] gets = {Constants.GREEN_STORE_URL_APP_NOTICE, "GET"};
        Server server = new Server(getActivity(), this);
        server.execute(gets);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notice, container, false);

        ListView lv = (ListView) view.findViewById(R.id.noticelistView);

        //  adapter = new ListViewAdapter(getActivity(), R.layout.listview, items);
        adapter = new ListViewAdapter(items);
        lv.setAdapter(adapter);

        // ListView의 이벤트 설정
        lv.setOnItemClickListener(new ListViewItemClickListener());
// Inflate the layout for this fragment
        return view;
    }

    @Override
    public void customAddList(String result) {
        Log.d("coffee",result);
        items.clear();
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                Notice notice = new Notice();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                notice.setNkey(Integer.parseInt(jsonObject.getString("nkey")));
                notice.setNtitle(jsonObject.getString("ntitle"));
                notice.setNcontent(jsonObject.getString("ncontent"));

                //String으로 받는 ndate를 Date로 바꾼 후 실행
                Date from = new Date(Long.valueOf(jsonObject.getString("ndate")));
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String temp = transFormat.format(from);
                Date to = transFormat.parse(temp);
                notice.setNdate(to);
                Log.e("dateformat",""+transFormat.format(from));
//                Date to = transFormat.parse(String.valueOf(from));
//                notice.setNdate(to);

                items.add(notice);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
            });

            alertDlg.setMessage(items.get(position).getNkey()+") "+items.get(position).getNtitle()+"\n\n"+items.get(position).getNcontent());
            alertDlg.show();
        }
    }

}
