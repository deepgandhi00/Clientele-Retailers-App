package com.clientele.retailers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class RegisterActivity extends AppCompatActivity {
    ImageButton register_image;
    EditText first_name,last_name,email,password,contact;
    Button btn_signup;
    Bitmap bmp;
    String file_path="";
    private final int req_code=999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_image=findViewById(R.id.register_image_btn);
        first_name=findViewById(R.id.register_firstname);
        last_name=findViewById(R.id.register_last_name);
        email=findViewById(R.id.register_email);
        password=findViewById(R.id.register_password);
        contact=findViewById(R.id.register_contact);

        btn_signup=findViewById(R.id.register_btn_signup);

        register_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String con=contact.getText().toString().trim();
                String em=email.getText().toString().trim();
                String pw=password.getText().toString().trim();
                String nm=first_name.getText().toString().trim()+" "+last_name.getText().toString().trim();
                String pn=first_name.getText().toString().trim()+last_name.getText().toString().trim();
                register_retailer r=new register_retailer(con,pw,em,nm,pn);
                r.execute();
            }
        });
    }

    private void selectImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,req_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==req_code && resultCode==RESULT_OK && data!=null){
            Uri path=data.getData();
            try {

                bmp= MediaStore.Images.Media.getBitmap(getContentResolver(),path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            register_image.setImageBitmap(bmp);
        }
    }

    class register_retailer extends AsyncTask<String, String,String>{
        String ret_con;
        String ret_pwd;
        String ret_email;
        String ret_name;
        String photo_name;
        public register_retailer(String ret_con,String ret_pwd,String ret_email,String ret_name,String photo_name){
            this.ret_con=ret_con;
            this.ret_pwd=ret_pwd;
            this.ret_email=ret_email;
            this.ret_name=ret_name;
            this.photo_name=photo_name;
        }
        @Override
        protected String doInBackground(String... strings) {
            String json_name="";
            String res="";
            try{
                String connection=getResources().getString(R.string.connection_upload);
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(connection);
//                FileBody bin=new FileBody(new File(file_path));
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                byte[] upload=byteArrayOutputStream.toByteArray();
//                HttpEntity reqentity= MultipartEntityBuilder.create()
//                        .addPart("image", bin)
//                        .build();

                MultipartEntityBuilder builder=MultipartEntityBuilder.create();
//                builder.addBinaryBody("image",upload);
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                builder.addPart("uplod_img",new ByteArrayBody(upload,"image/jpeg",photo_name+".jpg"));
                HttpEntity entity=builder.build();
                httpPost.setEntity(entity);
//                httpPost.setEntity(reqentity);
                HttpResponse response=httpClient.execute(httpPost);
                HttpEntity ent=response.getEntity();
                Log.i("response","response");
                InputStream is=ent.getContent();
//
                BufferedInputStream bis=new BufferedInputStream(is);
                int x;
               // String r="";
                while ((x=bis.read())!=-1)
                {
                    res=res+(char)x;
                }
                res=res.trim();
//                res=response.toString();

            }
            catch (Exception e){}
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("onpostresponse",s);
            InsertRetailer i=new InsertRetailer(ret_con,ret_name,ret_email,ret_pwd,s);
            i.execute();
        }
    }

    class InsertRetailer extends AsyncTask<String, String, String>{
        String ret_con;
        String ret_name;
        String ret_email;
        String ret_pwd;
        String ret_photo;
        public InsertRetailer (String ret_con,String ret_name,String ret_email,String ret_pwd,String ret_photo){
            this.ret_con=ret_con;
            this.ret_name=ret_name;
            this.ret_email=ret_email;
            this.ret_pwd=ret_pwd;
            this.ret_photo=ret_photo;
        }
        @Override
        protected String doInBackground(String... strings) {
            String json_res="";
            try{

                JSONObject jo=new JSONObject();
                jo.put("retailerContact",ret_con);
                jo.put("retailerEmail",ret_email);
                jo.put("retailerName",ret_name);;
                jo.put("retailerPwd",ret_pwd);
                jo.put("retailerPhoto",ret_photo);

                String req=jo.toString();
                String connection=getResources().getString(R.string.connection_data);
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(connection+"clientele.retailers/addRetailer");
                httpPost.setEntity(new StringEntity(req,"UTF-8"));
                httpPost.setHeader("Accept","text/plain");
                httpPost.setHeader("content-type","application/json");
                HttpResponse httpResponse=httpClient.execute(httpPost);
                HttpEntity ent=httpResponse.getEntity();
                Log.i("response","response");
                InputStream is=ent.getContent();
//
                BufferedInputStream bis=new BufferedInputStream(is);
                int x;
                // String r="";
                while ((x=bis.read())!=-1)
                {
                    json_res=json_res+(char)x;
                }
                json_res=json_res.trim();
            }
            catch (Exception e){}
            Log.i("retaileridres",json_res);
            return json_res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
                Log.i("onpostexeadd",ret_con+ret_photo+ret_pwd+ret_email+ret_name+s);
            SharedPreferences sh=getSharedPreferences("data",MODE_PRIVATE);
            SharedPreferences.Editor ed=sh.edit();
            ed.putString("email",ret_email);
            ed.putString("retailerImage",ret_photo);
            ed.putString("retailerName",ret_name);
            ed.putString("addId","0");
            ed.putString("retailerId",s);
            ed.commit();
            Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
