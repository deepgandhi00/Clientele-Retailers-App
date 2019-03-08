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
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Royal on 13-Feb-18.
 */

public class subcategoryRecyclerAdapter extends RecyclerView.Adapter<subcategoryRecyclerAdapter.ViewHolder> {
    List<pojo_subcategory> lst=new ArrayList<>();
    Fragment fm;
    public subcategoryRecyclerAdapter(List<pojo_subcategory> lst, Fragment fm){
        this.lst=lst;
        this.fm=fm;
    }
    @Override
    public subcategoryRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_card_layout,parent,false);
        subcategoryRecyclerAdapter.ViewHolder viewHolder=new subcategoryRecyclerAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(subcategoryRecyclerAdapter.ViewHolder holder, int position) {
        try {
            pojo_subcategory p = lst.get(position);
            String connection_url = fm.getContext().getResources().getString(R.string.connection_image);
            URL u = new URL(connection_url + p.getSub_image());
            Bitmap bmp = BitmapFactory.decodeStream(u.openStream());
            holder.subcategory_image.setImageBitmap(bmp);
            holder.subcategory_name.setText(p.getSub_name());
            holder.subcategory_desc.setText(p.getSub_desc());
            holder.id = p.getSub_id();
        }
        catch(Exception e){}
    }

    @Override
    public int getItemCount() {
        return lst.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public int id;
        public ImageView subcategory_image;
        public TextView subcategory_name;
        public TextView subcategory_desc;

        public ViewHolder(View itemView) {
            super(itemView);
            subcategory_image=itemView.findViewById(R.id.subcategory_card_image);
            subcategory_name=itemView.findViewById(R.id.subcategory_card_name);
            subcategory_desc=itemView.findViewById(R.id.subcategory_card_desc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager f=fm.getFragmentManager();
                    FragmentTransaction ft=f.beginTransaction();
                    product_frame_java p=new product_frame_java();
                    Bundle b=new Bundle();
                    b.putInt("id",id);
                    p.setArguments(b);
                    ft.replace(R.id.main_frame_layout,p);
                    ft.addToBackStack("");
                    ft.commit();

                }
            });
        }
    }
}
