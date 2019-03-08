package com.clientele.retailers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
 * Created by Royal on 05-Mar-18.
 */

public class product_detail_frame extends Fragment {
    cart_sqlhelper cartSqlhelper;
    ImageView img_product;
    TextView product_name,product_desc,product_htu,product_ing,product_qty;
    Spinner product_qtywt_cost;
    Button bookmark,add_to_cart,btn_minus,btn_plus;
    List<pojo_product_details> lst;
    custom_spinner_adapter adapter;
    int selected_id;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.product_detail_layout,container,false);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        cartSqlhelper=new cart_sqlhelper(getActivity(),"retailer",99);
        img_product=v.findViewById(R.id.product_detail_image);
        product_name=v.findViewById(R.id.product_detail_name);
        product_desc=v.findViewById(R.id.product_detail_desc);
        product_htu=v.findViewById(R.id.product_detail_htu);
        product_ing=v.findViewById(R.id.product_detail_ing);
        product_qtywt_cost=v.findViewById(R.id.product_detail_spinner);
        bookmark=v.findViewById(R.id.product_detail_bmk);
        add_to_cart=v.findViewById(R.id.product_detail_atc);
        btn_minus=v.findViewById(R.id.product_detail_minus);
        btn_plus=v.findViewById(R.id.product_detail_plus);
        product_qty=v.findViewById(R.id.product_detail_qty);

        lst=new ArrayList<>();
        Bundle b=getArguments();
        adapter=new custom_spinner_adapter(this.getActivity(),lst);
        int pid=b.getInt("pid");
        product_qtywt_cost.setAdapter(adapter);

        product_detail_data data=new product_detail_data(product_name,product_desc,product_ing,product_htu,img_product,pid);
        data.execute();
        Log.i("lilstsizeproductetail",lst.size()+"");

        product_qtywt_cost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(),lst.get(i).getIds()+"",Toast.LENGTH_SHORT).show();
                selected_id=lst.get(i).getIds();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        View.OnClickListener addsub =new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.product_detail_plus){
                    int qty=Integer.parseInt(product_qty.getText().toString());
                    qty=qty+1;
                    product_qty.setText(qty+"");
                }
                else if(view.getId()==R.id.product_detail_minus){
                    int qty=Integer.parseInt(product_qty.getText().toString());
                    if(qty<1){
                        Toast.makeText(getActivity(),"quantity cannot be less than 0",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        qty = qty - 1;
                        product_qty.setText(qty + "");
                    }
                }
            }
        };

        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=view.getId();
                if(id==R.id.product_detail_bmk){
                    Toast.makeText(getActivity(),"bookmarked"+selected_id,Toast.LENGTH_SHORT).show();
                }
                else if (id==R.id.product_detail_atc){
                    int qty_tv=Integer.parseInt(product_qty.getText().toString());
                    boolean b=cartSqlhelper.insert_cart(selected_id,qty_tv);
                    if(b){
                        Toast.makeText(getActivity(),"added to cart"+selected_id,Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(),"not added"+selected_id,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        bookmark.setOnClickListener(listener);
        add_to_cart.setOnClickListener(listener);
        btn_minus.setOnClickListener(addsub);
        btn_plus.setOnClickListener(addsub);
        return v;
    }
    /*************************************asynctask to download details****************************************************/
    class product_detail_spinner extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            String json_product_data_spinner="";
            try{
                String connection_string=getResources().getString(R.string.connection_data);
                URL updetail=new URL(connection_string+"clientele.productdetails/getProdetails/"+strings[0]);
                Log.i("productdetailsurl",updetail.toString());
                URLConnection upcon=updetail.openConnection();
                InputStream inspd=upcon.getInputStream();
                BufferedInputStream bispd=new BufferedInputStream(inspd);
                int j=0;
                while((j=bispd.read())!=-1){
                    json_product_data_spinner=json_product_data_spinner+(char)j;
                }
            }
            catch (Exception e){}
            json_product_data_spinner=json_product_data_spinner.trim();
            return json_product_data_spinner;
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
                        lst.add(p);
                        adapter.notifyDataSetChanged();
                    }
                    else{
                        String wt=jo.getString("productDetailWt");
                        pojo_product_details p=new pojo_product_details(pdid,null,wt,cost);
                        lst.add(p);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            catch (Exception e){}
        }
    }

    /**************************************asynctask to fetch a product*****************************************************/
    class product_detail_data extends AsyncTask<String,String,String>{
        TextView tv_name,tv_desc,tv_ing,tv_htu;
        ImageView img;
        int pid;

        public product_detail_data(TextView tv_name,TextView tv_desc,TextView tv_ing,TextView tv_htu,ImageView img,int pid){
            this.tv_name=tv_name;
            this.tv_desc=tv_desc;
            this.tv_htu=tv_htu;
            this.tv_ing=tv_ing;
            this.img=img;
            this.pid=pid;
        }

        @Override
        protected String doInBackground(String... strings) {
            String json_product_detail="";
            try{
                String connection_string=getResources().getString(R.string.connection_data);
                URL upro=new URL(connection_string+"clientele.product/"+pid);
                Log.i("producturl",upro.toString());
                URLConnection uconpro=upro.openConnection();
                InputStream inspro=uconpro.getInputStream();
                BufferedInputStream bispro=new BufferedInputStream(inspro);
                int i=0;
                while ((i=bispro.read())!=-1){
                    json_product_detail=json_product_detail+(char)i;
                }
                json_product_detail=json_product_detail.trim();
            }
            catch (Exception e){}
            return json_product_detail;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.i("jsonproduct",s);
                JSONObject jo=new JSONObject(s);
                product_detail_spinner data1=new product_detail_spinner();
                data1.execute(pid+"");
                Log.i("productname",jo.getString("productName"));
                tv_name.setText(jo.getString("productName"));
                tv_desc.setText(jo.getString("productDesc"));
                tv_htu.setText(jo.getString("productHowToUse"));
                tv_ing.setText(jo.getString("productIngredients"));
                String connection_image=getResources().getString(R.string.connection_image);
                URL uimg=new URL(connection_image+jo.getString("productImage"));
                Log.i("imageurlpro",uimg.toString());
                Bitmap bmp= BitmapFactory.decodeStream(uimg.openStream());
                img.setImageBitmap(bmp);
            }
            catch (Exception e){}
        }
    }
}
