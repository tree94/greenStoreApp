package com.seoul.greenstore.greenstore.Recycler;

/**
 * Created by X on 2016-09-06.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seoul.greenstore.greenstore.R;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<Recycler_item> items = Collections.emptyList();
    int currentPos = 0;
    Recycler_item current;

    public RecyclerAdapter(Context context,List<Recycler_item> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.items = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, null);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        System.out.println("??"+position);
        final Recycler_item item = items.get(position);
        Recycler_item current = items.get(position);
        holder.name.setText(current.getName());
        holder.addr.setText(current.getAddr());
        holder.imageURL = current.getImage();
        new DownloadAsyncTask().execute(holder);

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, item.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private String imageURL;
        private Bitmap bitmap;
        private TextView name;
        private TextView addr;
        private CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            addr = (TextView) itemView.findViewById(R.id.addr);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
        }

    }

    public void addItem(int id,int like,String image,String name,String addr){
        Recycler_item store = new Recycler_item();
        store.setId(id);
        store.setLike(like);
        store.setImage(image);
        store.setName(name);
        items.add(store);
    }

    private class DownloadAsyncTask extends AsyncTask<ViewHolder,Void,ViewHolder> {

        @Override
        protected ViewHolder doInBackground(ViewHolder...params){
            ViewHolder viewHolder = params[0];
            try{
                InputStream in = new URL(viewHolder.imageURL).openStream();
                viewHolder.bitmap = BitmapFactory.decodeStream(in);
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
