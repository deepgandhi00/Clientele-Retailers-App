package com.clientele.retailers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Royal on 24-Feb-18.
 */

public class productRecyclerAdapter extends RecyclerView.Adapter<productRecyclerAdapter.ViewHolder> {
    List<pojo_product> lst=new ArrayList<>();
    Fragment fm;
    cart_sqlhelper cartSqlhelper;
    int pro_detail_id;

    public productRecyclerAdapter(List<pojo_product> lst,Fragment fm){
        this.lst=lst;
        this.fm=fm;
        cartSqlhelper=new cart_sqlhelper(fm.getActivity(),"retailer",99);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try {
            pojo_product p = lst.get(position);
            String connection_url = fm.getContext().getResources().getString(R.string.connection_image);
            URL u = new URL(connection_url + p.getProductImage());
            Log.i("pimageurl",u.toString());
            Bitmap bmp = BitmapFactory.decodeStream(u.openStream());
            holder.pid=p.getProductId();
            holder.product_image.setImageBitmap(bmp);
            holder.category_name.setText(p.getCategoryName());
            holder.subcategory_name.setText(p.getSubcategoryName());
            Log.i("subcategoryname",p.getCategoryName());
            holder.product_name.setText(p.getProductName());
            custom_spinner_adapter sa=new custom_spinner_adapter(fm.getActivity(),p.getLst());
            final List<pojo_product_details> p1=p.getLst();
            holder.product_details.setAdapter(sa);
            holder.product_details.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(fm.getActivity(),p1.get(i).getIds()+"",Toast.LENGTH_SHORT).show();
                    pro_detail_id=p1.get(i).getIds();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            View.OnClickListener addsub=new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button b=(Button)view;
                    if(b.getId()==R.id.product_card_plus){
                        int qty=Integer.parseInt(holder.product_qty.getText().toString());
                        qty++;
                        holder.product_qty.setText(qty+"");
                    }
                    else if(b.getId()==R.id.product_card_minus){
                        int qty=Integer.parseInt(holder.product_qty.getText().toString());
                        if(qty==1){
                            Toast.makeText(fm.getActivity(),"cannot be 0",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            qty--;
                            holder.product_qty.setText(qty+"");
                        }
                    }
                }
            };
            holder.add.setOnClickListener(addsub);
            holder.sub.setOnClickListener(addsub);
            holder.add_to_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("onclick","clicked");
                    int qty=Integer.parseInt(holder.product_qty.getText().toString());
                    boolean b=cartSqlhelper.insert_cart(pro_detail_id,qty);
                    if(b){
                        Toast.makeText(fm.getActivity(),"added to cart"+pro_detail_id,Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(fm.getActivity(),"not added"+pro_detail_id,Toast.LENGTH_SHORT).show();
                    }

                }
            });
            Log.i("spinnerdata",p.getLst().size()+"");
        }
        catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        Log.i("lstsizeinadapter",lst.size()+"");
        return lst.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public int pid;
        public int pdid;
        ImageView product_image;
        TextView product_name,category_name,subcategory_name,product_qty;
        Spinner product_details;
        Button add,sub,add_to_cart;


        public ViewHolder(View itemView) {
            super(itemView);
            product_image=itemView.findViewById(R.id.product_card_image);
            product_name=itemView.findViewById(R.id.product_card_name);
            category_name=itemView.findViewById(R.id.product_card_category);
            subcategory_name=itemView.findViewById(R.id.product_card_subcategory);
            product_qty=itemView.findViewById(R.id.product_card_qty);
            product_details=itemView.findViewById(R.id.product_card_spinner);
            add=itemView.findViewById(R.id.product_card_plus);
            sub=itemView.findViewById(R.id.product_card_minus);
            add_to_cart=itemView.findViewById(R.id.product_card_addtocart);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager f=fm.getFragmentManager();
                    FragmentTransaction ft=f.beginTransaction();
                    product_detail_frame p=new product_detail_frame();
                    Bundle b=new Bundle();
                    b.putInt("pid",pid);
                    p.setArguments(b);
                    ft.replace(R.id.main_frame_layout,p);
                    ft.addToBackStack("");
                    ft.commit();
                }
            });
        }
    }
}
