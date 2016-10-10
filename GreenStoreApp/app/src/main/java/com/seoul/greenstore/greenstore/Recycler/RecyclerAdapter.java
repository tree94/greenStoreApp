package com.seoul.greenstore.greenstore.Recycler;

/**
 * Created by X on 2016-09-06.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.seoul.greenstore.greenstore.DetailFragment;
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
        private CardView cardView;

        public ViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView)itemView.findViewById(R.id.name);
            addr = (TextView)itemView.findViewById(R.id.addr);
            cardView = (CardView)itemView.findViewById(R.id.cardview);
        }

    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,null);
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Recycler_item recycler_item = items.get(position);
        holder.name.setText(recycler_item.getName());
        holder.addr.setText(recycler_item.getAddr());
        Picasso.with(context).load(recycler_item.getImage()).fit().centerInside().into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                Fragment fragment = new DetailFragment();

                Toast.makeText(context, ""+recycler_item.getId(), Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putInt("position",recycler_item.getId());
                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.llContents, fragment);
                fragmentTransaction.addToBackStack(fm.findFragmentById(R.id.llContents).toString());
                fragmentTransaction.commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

}
