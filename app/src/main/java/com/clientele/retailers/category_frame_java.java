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
import java.util.List;

/**
 * Created by Royal on 12-Feb-18.
 */

public class category_frame_java extends Fragment {
    List<pojo_category> lst_product;
    RecyclerView category_recycler;
    RecyclerView.LayoutManager manager;
    RecyclerView.Adapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.category_recycler_view,container,false);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        lst_product=new ArrayList<>();
        category_recycler=v.findViewById(R.id.category_recycler_view);

        manager=new LinearLayoutManager(getActivity());
        category_recycler.setLayoutManager(manager);
        adapter=new categoryRecyclerAdapter(lst_product,this);
        category_recycler.setAdapter(adapter);


        /******************************asynctask to load image for each list data**********************************/

        category_data o=new category_data();
        o.execute();

        return v;
    }

    /*********************asynctask to get list data**************************************/
    class category_data extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String json_array="";
            try{
                String connection_string=getResources().getString(R.string.connection_data);
                URL uc=new URL(connection_string+"clientele.category");
                Log.i("urlconnected",uc.toString());
                URLConnection urlConnection=uc.openConnection();
                Log.i("connectionopened","true");
                InputStream insc=urlConnection.getInputStream();
                BufferedInputStream bisc=new BufferedInputStream(insc);
                Log.i("inputstreamconnected","true");
                int i=0;
                while((i=bisc.read())!=-1){
                    json_array=json_array+(char)i;
                }
                json_array=json_array.trim();
                Log.i("json_array_category1",json_array);
                bisc.close();
                insc.close();
            }
            catch(Exception e){}
            return json_array;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray array1 = new JSONArray(s);
                Log.i("json_array_category",s);
                for(int i=0;i<array1.length();i++){
                    JSONObject o=(JSONObject)array1.get(i);
                    String name=o.getString("categoryName");
                    String image=o.getString("categoryImage");
                    String desc=o.getString("categoryDesc");
                    int id=o.getInt("categoryId");
                    pojo_category pojoproduct=new pojo_category(name,image,id,desc);
                    lst_product.add(pojoproduct);
                    adapter.notifyDataSetChanged();
                }
            }
            catch(Exception e){
                Log.i("exc",e+"");
            }
        }
    }
}
