package com.clientele.retailers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class LoginActivity extends AppCompatActivity {
    //implement for google sign in annd uncomment  implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener
    EditText login_ed_user, login_ed_pwd;
    Button login_btn_login;
    TextView login_tv_msg,login_tv_signup;

    //SignInButton login_gsignin;
    //GoogleApiClient googleApiClient;
    //int req_code=9000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_ed_user = findViewById(R.id.signin_ed_email);
        login_ed_pwd = findViewById(R.id.signin_ed_pwd);
        login_btn_login = findViewById(R.id.signin_btn_sign_in);
        login_tv_msg = findViewById(R.id.signin_tv_msg);
        login_tv_signup=findViewById(R.id.login_tv_signup);

        login_tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });
        //login_gsignin=findViewById(R.id.signin_btn_gsignin);
        login_btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = login_ed_user.getText().toString();
                String connection_string=getResources().getString(R.string.connection_data);
                String ur = connection_string+"clientele.retailers/getRetailers";
                login_Asyntask ltask = new login_Asyntask(ur, login_tv_msg);
                ltask.execute(email);
                Log.i("urlinnactivity", ur);
            }
        });
        //login_gsignin.setOnClickListener(this);
        //GoogleSignInOptions options=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        //googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,options).build();


//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.signin_btn_sign_in:
//                email_signin();
//                break;
//
//            case R.id.signin_btn_gsignin:
//                gsign();
//                break;
//        }
//    }

//    private void email_signin() {
//        Log.i("clicked","emailsignedin");
//        String email=login_ed_user.getText().toString();
//        String pwd=login_ed_pwd.getText().toString();
//        String url="192.168.43.140:35769/clienttelewebservice/webresources/clientele.retailers/getRetailers/"+email;
//        login_Asyntask ltask=new login_Asyntask();
//        ltask.execute(url);
//
//    }
//    private void gsign(){
//        Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
//        startActivityForResult(intent,req_code);
//    }

//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode==req_code){
//            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            if (result.isSuccess()){
//                GoogleSignInAccount account=result.getSignInAccount();
//                String email=account.getEmail();
//                //login_Asyntask ltask=new login_Asyntask("192.168.43.20:8080/clienttelewebservice/webresources/clientele.retailers/getRetailers/%7Bemail%7D",email);
//                //ltask.execute();
//            }
//        }
//    }
    }

    public class login_Asyntask extends AsyncTask<String, String, String> {
        String url;
        TextView msg;

        login_Asyntask(String url, TextView msg) {
            this.url = url;
            this.msg = msg;
        }

        @Override
        protected String doInBackground(String... strings) {
            String sb = "";

            Log.i("doinbackground", "heloooooo");

            try {
                Log.i("beforeurl", "jbjbjbjhbj");
                String u1 = strings[0];
                Log.i("ssssssssssssssss", u1);
                URL u = new URL(url + "/" + strings[0]);
                Log.i("cccccurl", u.toString());
                URLConnection ucon = u.openConnection();
                Log.i("uuuuuuuuuuuuuuu", "hiiiiiiiiiiiii");
                InputStream ins = ucon.getInputStream();
                Log.i("ddddddddddddddd", "hiiiiiiiiiiiiiii");
                BufferedInputStream bis = new BufferedInputStream(ins);
                int i = 0;

                while ((i = bis.read()) != -1) {
                    sb = sb + (char) i;
                }
                Log.i("sssssssssbbbbbbbbbb", sb);
                sb = sb.trim();
            } catch (Exception e) {
//                e.printStackTrace();
            }
            return sb;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            try {
                Log.i("sstring", s);
                JSONArray arr = new JSONArray(s);
                int arr_llength = arr.length();
                Log.i("json_array", arr.toString());
                JSONObject object = (JSONObject) arr.get(0);
                String jpwd=object.getString("retailerPwd").trim();
                String pwd=login_ed_pwd.getText().toString().trim();
                String email=login_ed_user.getText().toString().trim();
                if (arr_llength > 0) {
                    if(pwd.equals(jpwd)) {
                        SharedPreferences sh=getSharedPreferences("data",MODE_PRIVATE);
                        SharedPreferences.Editor ed=sh.edit();
                        ed.putString("email",email);
                        ed.putString("retailerId",object.getString("retailerId"));
                        ed.putString("retailerImage",object.getString("retailerPhoto"));
                        ed.putString("retailerName",object.getString("retailerName"));
                        ed.putString("addId","1");
                        ed.commit();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        Log.i("loginsucessful","true");
                        startActivity(i);
                        finish();
                    }
                    else{
                        msg.setText("password is incorrect");
                    }
                } else {
                    msg.setText("email doesn't exists");
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }

    }
}