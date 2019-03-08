package com.clientele.retailers;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Register2Activity extends AppCompatActivity {
    EditText shop_house_name,shop_house_no,complex,road,landmark,street_name,society_name,street_no,society_no;
    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        shop_house_name=findViewById(R.id.register2_shop_house_name);
        shop_house_no=findViewById(R.id.register2_shop_house_no);
        complex=findViewById(R.id.register2_complex);
        road=findViewById(R.id.register2_road);
        landmark=findViewById(R.id.register2_landmark);
        street_name=findViewById(R.id.register2_street_name);
        society_name=findViewById(R.id.register2_society_name);
        street_no=findViewById(R.id.register2_street_no);
        society_no=findViewById(R.id.register2_society_no);
        btn_add=findViewById(R.id.register2_btn_add);

        JSONObject jo=new JSONObject();
        try {
            jo.put("",shop_house_name.getText().toString());
            jo.put("",shop_house_no.getText().toString());
            jo.put("",complex.getText().toString());
            jo.put("",road.getText().toString());
            jo.put("",landmark.getText().toString());
            jo.put("",street_name.getText().toString());
            jo.put("",street_no.getText().toString());
            jo.put("",society_name.getText().toString());
            jo.put("",society_no.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String json_string=jo.toString();
    }

    class add_adress extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            String add_id="";
            try {
                String connection = getResources().getString(R.string.connection_data);
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(connection+"clientele.orderdetail/createOrderDetail");
                httpPost.setEntity(new StringEntity(strings[0],"UTF-8"));
                httpPost.setHeader("Accept","text/plain");
                httpPost.setHeader("content-type","application/json");
                HttpResponse httpResponse=httpClient.execute(httpPost);
                String res=httpResponse.getStatusLine().getStatusCode()+"";
                Log.i("responsehttp",res);
                update_retailer up=new update_retailer();
                up.execute(res,strings[1]);
            }
            catch (Exception e){}
            return null;
        }
    }
    class update_retailer extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            String connection = getResources().getString(R.string.connection_data);
            String res="";
            try{
                URL uup=new URL(connection+"");
                URLConnection uconup=uup.openConnection();
                InputStream insup=uconup.getInputStream();
                BufferedInputStream bisup=new BufferedInputStream(insup);
                int i=0;
                while((i=bisup.read())!=-1){
                    res=res+(char)i;
                }

            }
            catch (Exception e){}
            return null;
        }
    }
}
