package com.clientele.retailers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Royal on 08-Mar-18.
 */

public class cart_frame extends Fragment {
    TextView tv_items,tv_total;
    List<pojo_cart> lst_cart;
    List<pojo_cart_product> lst_product;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    RecyclerView.Adapter adapter;
    Button checkout;
    String csv="";
    cart_sqlhelper sqlhelper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.cart_layout,container,false);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        sqlhelper=new cart_sqlhelper(getActivity(),"retailer",99);
        lst_cart=new ArrayList<>();
        lst_product=new ArrayList<>();
        tv_items=v.findViewById(R.id.cart_layout_totalitems);
        tv_total=v.findViewById(R.id.cart_layout_total);
        checkout=v.findViewById(R.id.cart_layout_checkout);
        lst_cart=sqlhelper.getAllcart();
        tv_items.setText(lst_cart.size()+"");
        for(pojo_cart p:lst_cart){
            csv=csv+","+p.getProduct_id();
        }
        csv=csv.substring(1,csv.length());
        Log.i("csvcart",lst_cart.size()+"");
        Log.i("csvcart",csv);
        recyclerView=v.findViewById(R.id.cart_recycler_view);
        manager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter=new cartRecyclerAdapter(lst_product,this,tv_total,checkout);
        recyclerView.setAdapter(adapter);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"clicked",Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                checkout_frame c=new checkout_frame();
                fragmentTransaction.replace(R.id.main_frame_layout,c);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });
        cart_product data=new cart_product(lst_cart,tv_total,checkout);
        data.execute(csv);
        return v;
    }
    /*****************************************asynctask to download product data*************************************/
    class cart_product extends AsyncTask<String,String,String>{
        TextView tvcost;
        Button chkout;
        int total_cost=0;
        List<pojo_cart> lst;
        public cart_product(List<pojo_cart> lst,TextView tvcost,Button chkout){
            this.lst=lst;
            this.tvcost=tvcost;
            this.chkout=chkout;
        }
        @Override
        protected String doInBackground(String... strings) {
            String json_product="";
            try{
                String connection_url=getResources().getString(R.string.connection_data);
                URL u=new URL(connection_url+"clientele.productdetails/getCartProducts/"+strings[0]);
                Log.i("urlcart",u.toString());
                URLConnection ucon=u.openConnection();
                InputStream ins=ucon.getInputStream();
                BufferedInputStream bis=new BufferedInputStream(ins);
                int j=0;
                while ((j=bis.read())!=-1){
                    json_product=json_product+(char)j;
                }
                json_product=json_product.trim();
            }
            catch (Exception e){}
            Log.i("jsoncartproduct",json_product);
            return json_product;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.i("oppostcartframe",s);
                JSONArray ja = new JSONArray(s);
                for(int k=0;k<ja.length();k++){
                    JSONObject jo=(JSONObject)ja.get(k);
                    String product_name=jo.getString("productName");
                    String product_image=jo.getString("productImage");
                    int product_id=jo.getInt("productId");
                    String product_cost=jo.getString("productCost");
                    int procost=Integer.parseInt(product_cost);
                    total_cost=total_cost+procost;
                    tvcost.setText(total_cost+"");
                    chkout.setText("BUY FOR Rs." + total_cost);
                    if(jo.has("productWt")){
                        String product_wt=jo.getString("productWt");
                        int cid=lst_cart.get(k).getCart_id();
                        int qty=lst_cart.get(k).getProduct_qty();
                        pojo_cart_product p=new pojo_cart_product(cid,product_image,product_name,null,product_wt,product_cost,qty);
                        lst_product.add(p);
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        String product_qty=jo.getString("productQty");
                        int cid=lst_cart.get(k).getCart_id();
                        int qty=lst_cart.get(k).getProduct_qty();
                        pojo_cart_product p=new pojo_cart_product(cid,product_image,product_name,product_qty,null,product_cost,qty);
                        lst_product.add(p);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            catch (Exception e){}
        }
    }
}
