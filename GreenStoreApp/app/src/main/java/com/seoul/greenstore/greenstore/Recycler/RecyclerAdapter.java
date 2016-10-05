package com.seoul.greenstore.greenstore.Recycler;

/**
 * Created by X on 2016-09-06.
 */

 import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

    public RecyclerAdapter(Context context,List<Recycler_item> data) {
        this.context = context;
        this.items = data;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView name;
        private TextView addr;

        public ViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView)itemView.findViewById(R.id.name);
            addr = (TextView)itemView.findViewById(R.id.addr);
        }

    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,null);
//        RecyclerView.ViewHolder
//        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,null);
//        holder = new ViewHolder(v);
//        holder.imageView = (ImageView) v.findViewById(R.id.image);
//        holder.name = (TextView) v.findViewById(R.id.name);
//        holder.addr = (TextView) v.findViewById(R.id.addr);
//        holder.cardview = (CardView) v.findViewById(R.id.cardview);
//        v.setTag(holder);
//        Log.e("tes13","112");
//        ViewHolder v = new ViewHolder(viewItem);
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recycler_item recycler_item = items.get(position);
//        holder = (ViewHolder) v.getTag();
//        Recycler_item store = items.get(position);
//        Log.e("storename",store.getName());
        holder.name.setText(recycler_item.getName());
        holder.addr.setText(recycler_item.getAddr());
        Picasso.with(context).load(recycler_item.getImage()).fit().centerInside().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

}
