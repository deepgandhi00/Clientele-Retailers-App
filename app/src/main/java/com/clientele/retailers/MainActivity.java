package com.clientele.retailers;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // lst_product=new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        SharedPreferences sh=getSharedPreferences("data",MODE_PRIVATE);
        String name=sh.getString("retailerName","");
        String email=sh.getString("email","");
        String photo=sh.getString("retailerImage","");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View v=navigationView.getHeaderView(0);
        ImageView imgvProfile = v.findViewById(R.id.nav_header_image);
        TextView tvName=v.findViewById(R.id.nav_header_name);
        tvName.setText(name);
        TextView tvEmail=v.findViewById(R.id.nav_header_email);
        tvEmail.setText(email);
        try {
            String path= Environment.getExternalStorageDirectory()+"/clientele/retailers/" + photo;
            File image_file=new File(path);
            if(!image_file.exists()){
                downloadImage d=new downloadImage(imgvProfile);
                d.execute(photo);
            }
            else {
                Bitmap bmp=BitmapFactory.decodeFile(path);
                Log.i("filepath",path);
                imgvProfile.setImageBitmap(bmp);
            }
        }
        catch (Exception e){}

        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        category_frame_java catjava=new category_frame_java();
        ft.replace(R.id.main_frame_layout,catjava);
        ft.commit();
    }

        @Override
        public void onBackPressed () {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected (MenuItem item){
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_cart) {
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                cart_frame l=new cart_frame();
                ft.replace(R.id.main_frame_layout,l);
                ft.commit();
            }
            else if (id == R.id.nav_wishlist) {
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                logout_frag l=new logout_frag();
                ft.replace(R.id.main_frame_layout,l);
                ft.commit();
            }
            else if(id==R.id.nav_logout){
                FragmentManager fm=getSupportFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                logout_frag l=new logout_frag();
                ft.replace(R.id.main_frame_layout,l);
                ft.commit();
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        class downloadImage extends AsyncTask<String, String, Bitmap>{
            ImageView imgv;

            public downloadImage(ImageView imgv) {
                this.imgv = imgv;
            }

            String name="";
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bmp=null;
                String connection=getResources().getString(R.string.connection_image);
                try{
                    name=strings[0];
                    URL uimage=new URL(connection+name);
                    Log.i("imageurlprofile",uimage.toString());
                    InputStream insimage=uimage.openStream();
                    bmp=BitmapFactory.decodeStream(insimage);
                }
                catch (Exception e){}
                return bmp;
            }

            @Override
            protected void onPostExecute(Bitmap s) {
                super.onPostExecute(s);
                try {
                    imgv.setImageBitmap(s);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    s.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    byte[] image = bytes.toByteArray();
                    String path=Environment.getExternalStorageDirectory()+"/clientele/retailers/"+name;
                    File folder=new File(Environment.getExternalStorageDirectory()+"/clientele/retailers");
                    if(!folder.exists()){
                        folder.mkdirs();
                    }
                    File file_image =new File(folder,name);
                    OutputStream outputStream = new FileOutputStream(file_image);
                    outputStream.write(image);
                    Log.i("filewrite","done");
                }
                catch (Exception e){Log.i("filewriteexception",e+"");}
            }
        }
    }
