package com.clientele.retailers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * Created by Royal on 13-Feb-18.
 */

public class subcategory_frame_java extends Fragment {
    int cid;
    List<pojo_subcategory> lst_subcategory;
    RecyclerView subcategory_recycler;
    RecyclerView.LayoutManager manager;
    RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.subcategory_recycler_view,container,false);

        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Bundle b=getArguments();
       cid= b.getInt("id");
       Log.i("subcategoryframe",cid+"");
        lst_subcategory=new ArrayList<>();
        subcategory_recycler=v.findViewById(R.id.subcategory_recycler_view);
        manager=new LinearLayoutManager(getActivity());
        subcategory_recycler.setLayoutManager(manager);
        adapter=new subcategoryRecyclerAdapter(lst_subcategory,this);
        subcategory_recycler.setAdapter(adapter);
        /******************************asynctask to load image for each list data**********************************/

        subcategory_data o=new subcategory_data();
        o.execute(cid+"");

        return v;
    }
    /*********************asynctask to get list data**************************************/
    class subcategory_data extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String json_array="";
            try{
                String connection_string=getResources().getString(R.string.connection_data);
                URL us=new URL(connection_string+"clientele.subcategory/getsubcategories/"+strings[0]);
                Log.i("suburlconnected",us.toString());
                URLConnection ucons=us.openConnection();
                Log.i("subconnectionopened",us.toString());
                InputStream inss=ucons.getInputStream();
                BufferedInputStream biss=new BufferedInputStream(inss);
                Log.i("subinputstreamconnected",us.toString());
                int i=0;
                while((i=biss.read())!=-1){
                    json_array=json_array+(char)i;
                }
                json_array=json_array.trim();
                Log.i("json_array_subcategory1",json_array);
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
                    String name=o.getString("subcategoryName");
                    String image=o.getString("subcategoryImage");
                    String desc=o.getString("subcategoryDesc");
                    int id=o.getInt("subcategoryId");
                    pojo_subcategory pojoproduct=new pojo_subcategory(name,image,id,desc);
                    lst_subcategory.add(pojoproduct);
                    adapter.notifyDataSetChanged();
//                    subcategory_image cimage=new subcategory_image(name,desc,image,id);
//                    Log.i("postexecute","datapart");
//                    String connection_image=getResources().getString(R.string.connection_image);
//                    cimage.execute(connection_image+image);

                }
            }
            catch(Exception e){
                Log.i("exc",e+"");
            }
        }
    }
}
