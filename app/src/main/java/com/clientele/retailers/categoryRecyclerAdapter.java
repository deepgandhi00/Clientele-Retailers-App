package com.clientele.retailers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Royal on 11-Feb-18.
 */

public class categoryRecyclerAdapter extends RecyclerView.Adapter<categoryRecyclerAdapter.ViewHolder>{
    List<pojo_category> lst=new ArrayList<>();
    Fragment fm;

    public categoryRecyclerAdapter(List<pojo_category> lst, Fragment fm){
        this.lst=lst;
        this.fm=fm;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            pojo_category p = lst.get(position);
            String connection_url=fm.getContext().getResources().getString(R.string.connection_image);
            URL u=new URL(connection_url+p.getImage());
            Bitmap bmp= BitmapFactory.decodeStream(u.openStream());
            holder.category_image.setImageBitmap(bmp);
            holder.category_name.setText(p.getName());
            holder.category_desc.setText(p.getDesc());
            holder.id = p.getId();
        }
        catch (Exception e){}

    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public int id;
        public ImageView category_image;
        public TextView category_name;
        public TextView category_desc;
//        Context context;

        public ViewHolder(final View itemView) {
            super(itemView);
            category_image=itemView.findViewById(R.id.category_card_image);
            category_name=itemView.findViewById(R.id.category_card_name);
            category_desc=itemView.findViewById(R.id.category_card_desc);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FragmentManager f=fm.getFragmentManager();
                    FragmentTransaction ft=f.beginTransaction();
                    subcategory_frame_java sbj=new subcategory_frame_java();
                    Bundle b=new Bundle();
                    b.putInt("id",id);
                    sbj.setArguments(b);
                    ft.replace(R.id.main_frame_layout,sbj);
                    ft.addToBackStack("");
                    ft.commit();
                }
            });
        }
    }
}
