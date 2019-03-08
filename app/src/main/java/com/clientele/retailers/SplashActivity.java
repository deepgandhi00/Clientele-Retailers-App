package com.clientele.retailers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);
        String email=sp.getString("email","");
        if(sp.contains("email")){
            Intent i=new Intent(SplashActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        else{
            Intent i=new Intent(SplashActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }
    }
}
