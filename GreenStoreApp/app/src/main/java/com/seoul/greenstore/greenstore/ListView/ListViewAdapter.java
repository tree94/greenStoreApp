package com.seoul.greenstore.greenstore.ListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.seoul.greenstore.greenstore.R;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by user on 2016-09-01.
 */
public class ListViewAdapter extends BaseAdapter{
    private ArrayList<ListView_item> storeList = new ArrayList<ListView_item>();

    public ListViewAdapter(){
    }

    @Override
    public int getCount(){
        return storeList.size();
    }

    private static class ViewHolder{
        ImageView imageView;
        String imageURL;
        Bitmap bitmap;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        ViewHolder viewHolder = null;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.simple_list_item_1,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.imageView1);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder)convertView.getTag();

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
        TextView titleView = (TextView) convertView.findViewById(R.id.textView1);
        TextView addrView = (TextView) convertView.findViewById(R.id.textView2);

        final ListView_item store = storeList.get(position);

        viewHolder.imageURL = store.getImg();
        new DownloadAsyncTask().execute(viewHolder);

        titleView.setText(store.getName());
        addrView.setText(store.getAddr());

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position){
        return storeList.get(position);
    }

    public void addItem(String img, String title, String addr) {
        ListView_item store = new ListView_item();
        store.setAddr(addr);
        store.setImg(img);
        store.setName(title);
        storeList.add(store);
    }

    private class DownloadAsyncTask extends AsyncTask<ViewHolder,Void,ViewHolder>{

        @Override
        protected ViewHolder doInBackground(ViewHolder...params){
            ViewHolder viewHolder = params[0];
            try{
                URL imageURL = new URL(viewHolder.imageURL);
                viewHolder.bitmap = BitmapFactory.decodeStream(imageURL.openStream());
            }catch (Exception e){
                e.printStackTrace();
                Log.e("error","Downloading Image Failed");
                viewHolder.bitmap = null;
            }
            return viewHolder;
        }


        @Override
        protected void onPostExecute(ViewHolder result){
            if(result.bitmap == null){
                Log.e("error","error");
                result.imageView.setImageResource(R.drawable.loading);
            }else {
                result.imageView.setImageBitmap(result.bitmap);
            }
        }
    }
}
