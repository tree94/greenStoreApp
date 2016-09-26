package com.seoul.greenstore.greenstore.Recycler;

/**
 * Created by X on 2016-09-06.
 */

 import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.seoul.greenstore.greenstore.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<Recycler_item> items = Collections.emptyList();
    private int currentPos = 0;
    private Recycler_item current;
    private ViewHolder holder;
    private View v;

    public RecyclerAdapter(){}

    public RecyclerAdapter(Context context,List<Recycler_item> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.items = data;
    }

//    public void setRecyclerAdapter(List<Recycler_item> data){
//        items = data;
//        notifyDataSetChanged();
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,null);
        holder = new ViewHolder(v);
        holder.imageView = (ImageView) v.findViewById(R.id.image);
        holder.name = (TextView) v.findViewById(R.id.name);
        holder.addr = (TextView) v.findViewById(R.id.addr);
        holder.cardview = (CardView) v.findViewById(R.id.cardview);
        v.setTag(holder);
        Log.e("tes13","112");
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder = (ViewHolder) v.getTag();
        Recycler_item store = items.get(position);
        Log.e("storename",store.getName());
        holder.name.setText(Html.fromHtml(store.getName()));
        holder.addr.setText(Html.fromHtml(store.getAddr()));
        System.out.println(store.getImage()+" image~~");
        Picasso.with(context).load(store.getImage()).fit().centerInside().into(holder.imageView);
//        final Recycler_item item = items.get(position);
//        Recycler_item current = items.get(position);
//        holder.name.setText(current.getName());
//        holder.addr.setText(current.getAddr());
//        holder.imageURL = current.getImage();

//        Picasso.with(context).load(current.getImage()).into(holder.imageView);


//        new DownloadAsyncTask().execute(holder);

//        holder.cardview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, item.getName(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }
//    extends RecyclerView.ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private String imageURL;
        private Bitmap bitmap;
        private TextView name;
        private TextView addr;
        private CardView cardview;

        public ViewHolder(View itemView){
            super(itemView);
        }
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            imageView = (ImageView) itemView.findViewById(R.id.image);
//            name = (TextView) itemView.findViewById(R.id.name);
//            addr = (TextView) itemView.findViewById(R.id.addr);
//            cardview = (CardView) itemView.findViewById(R.id.cardview);
//        }
    }
//
//    public void addItem(int id,int like,int indutyCode,String image,String name,String addr){
//        Recycler_item store = new Recycler_item();
//        store.setId(id);
//        store.setLike(like);
//        store.setImage(image);
//        store.setName(name);
//        store.setIndutyCode(indutyCode);
//        items.add(store);
//    }

//    private class DownloadAsyncTask extends AsyncTask<ViewHolder,Void,ViewHolder> {
//
//        @Override
//        protected ViewHolder doInBackground(ViewHolder...params){
//            ViewHolder viewHolder = params[0];
//            try{
//                InputStream in = new URL(viewHolder.imageURL).openStream();
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 4;
//                viewHolder.bitmap = BitmapFactory.decodeStream(in,null,options);
//            }catch (Exception e){
//                e.printStackTrace();
//                Log.e("error","Downloading Image Failed");
//                viewHolder.bitmap = null;
//            }
//            return viewHolder;
//        }
//
//        @Override
//        protected void onPostExecute(ViewHolder result){
//            if(result.bitmap == null){
//                Log.e("error","error");
//                result.imageView.setImageResource(R.drawable.loading);
//            }else {
//                result.imageView.setImageBitmap(result.bitmap);
//            }
//        }
//    }
}
