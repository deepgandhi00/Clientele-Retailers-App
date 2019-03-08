package com.clientele.retailers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Royal on 24-Feb-18.
 */

public class product_frame_java extends Fragment {
    List<pojo_product> lst;
    List<pojo_product_details> lstpd;
    int sid;
    RecyclerView product_recycler;
    RecyclerView.LayoutManager manager;
    RecyclerView.Adapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.product_recycler_view,container,false);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        Bundle b=getArguments();
        sid= b.getInt("id");
        lst=new ArrayList<>();
        lstpd=new ArrayList<>();
        product_recycler=v.findViewById(R.id.product_recycler_view);

        manager=new LinearLayoutManager(getActivity());
        product_recycler.setLayoutManager(manager);
        adapter=new productRecyclerAdapter(lst,this);
        product_recycler.setAdapter(adapter);

        product_data p=new product_data();
        p.execute(sid+"");

        return v;
    }
    /*********************************asyntask to ge list of product details*******************************************/
    class product_spinner_data extends AsyncTask<String, String, String>{
        int pid;
        String sname;
        String cname;
        String name;
        String image;
        product_spinner_data(int pid,String sname,String cname,String name,String image){
            this.pid=pid;
            this.sname=sname;
            this.cname=cname;
            this.name=name;
            this.image=image;
        }

        @Override
        protected String doInBackground(String... strings) {
            String json_pdetails="";
            try{
                String connection_string=getResources().getString(R.string.connection_data);
                URL upd=new URL(connection_string+"clientele.productdetails/getProdetails/"+strings[0]);
                Log.i("updurl",upd.toString());
                URLConnection uconpd=upd.openConnection();
                InputStream inspd=uconpd.getInputStream();
                BufferedInputStream bispd=new BufferedInputStream(inspd);
                int i=0;
                while((i=bispd.read())!=-1){
                    json_pdetails=json_pdetails+(char)i;
                }
                json_pdetails=json_pdetails.trim();
            }
            catch (Exception e){}
            Log.i("jsoninpdetails",json_pdetails);
            return json_pdetails;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.i("pdetailspost",s);
                JSONArray arrayp = new JSONArray(s);
                for(int j=0;j<arrayp.length();j++){
                    JSONObject jo=(JSONObject)arrayp.get(j);
                    int pdid=jo.getInt("productDetailId");
                    String cost=jo.getString("productDetailCost");
                    boolean a=jo.has("productDetailQty");
                    if(a){
                        String qty=jo.getString("productDetailQty");
                        pojo_product_details p=new pojo_product_details(pdid,qty,null,cost);
                        lstpd.add(p);
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        String wt=jo.getString("productDetailWt");
                        pojo_product_details p=new pojo_product_details(pdid,null,wt,cost);
                        lstpd.add(p);
                        adapter.notifyDataSetChanged();
                    }
                }
                pojo_product p=new pojo_product(pid,name,cname,sname,image,lstpd);
                lst.add(p);
                adapter.notifyDataSetChanged();
            }
            catch (Exception e){}
        }
    }
    /********************************asynctask to ownload data in product************************************************/
    class product_data extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            String json_product="";
            try{
                String connection_string=getResources().getString(R.string.connection_data);
                URL up=new URL(connection_string+"clientele.productdetails/getproduct/"+strings[0]);
                Log.i("urlproduct",up.toString());
                URLConnection uconp=up.openConnection();
                InputStream insp=uconp.getInputStream();
                BufferedInputStream bisp=new BufferedInputStream(insp);
                int i=0;
                while((i= bisp.read())!=-1){
                    json_product=json_product+(char)i;
                }
                json_product=json_product.trim();

            }
            catch (Exception e){}
            Log.i("jsoninbackgrounf",json_product);
            return json_product;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                Log.i("productdata",s);
                JSONArray arrayp=new JSONArray(s);
                for(int j=0;j<arrayp.length();j++){
                    JSONObject jo1=(JSONObject)arrayp.get(j);
                    int pid=jo1.getInt("product_Id");
                    String sname=jo1.getString("subcategory_Name");
                    String cname=jo1.getString("category_Name");
                    String name=jo1.getString("product_Name");
                    String image=jo1.getString("product_Image");
                    product_spinner_data data1=new product_spinner_data(pid,sname,cname,name,image);
                    data1.execute(pid+"");
                }
            }
            catch (Exception e){}
        }
    }
}
