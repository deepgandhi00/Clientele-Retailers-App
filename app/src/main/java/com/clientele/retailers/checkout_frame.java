package com.clientele.retailers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Royal on 16-Mar-18.
 */

public class checkout_frame extends Fragment {
    RadioGroup group, address;
    List<pojo_cart> lst;
    cart_sqlhelper carthelper;
    Button checkout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.checkout_layout, container, false);
        group = v.findViewById(R.id.checkout_radio_group_payment);
        address = v.findViewById(R.id.checkout_radio_group_delivery);
        checkout = v.findViewById(R.id.checkout_btn_checkout);
        carthelper = new cart_sqlhelper(getActivity(), "retailer", 98);
        lst = new ArrayList<>();
        lst = carthelper.getAllcart();
        checkout.setEnabled(false);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                checkout.setEnabled(true);
            }
        });

        add_radio a = new add_radio(group, checkout);
        a.execute();
        add_delivery b = new add_delivery(address);
        b.execute();
        SharedPreferences sh = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        final String retailerId = sh.getString("retailerId", "");
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertOrder insert = new insertOrder();
                insert.execute(retailerId, group.getCheckedRadioButtonId() + "");
            }
        });
        return v;
    }

    class add_delivery extends AsyncTask<String, String, String> {
        RadioGroup group1;

        public add_delivery(RadioGroup group1) {
            this.group1 = group1;
        }

        @Override
        protected String doInBackground(String... strings) {
            String add_data = "";
            try {
                String connection = getActivity().getResources().getString(R.string.connection_data);
                SharedPreferences sh = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                String addids = sh.getString("addId", "");
                URL uadd = new URL(connection + "clientele.addressdetail/getaddresses/" + addids);
                Log.i("urladdids", uadd.toString());
                URLConnection uadcon = uadd.openConnection();
                InputStream insadd = uadcon.getInputStream();
                BufferedInputStream bisadd = new BufferedInputStream(insadd);
                int add;
                while ((add = bisadd.read()) != -1) {
                    add_data = add_data + (char) add;
                }
            } catch (Exception e) {
            }
            add_data = add_data.trim();
            Log.i("adddata", add_data);
            return add_data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray ja = new JSONArray(s);
                for (int k = 0; k < ja.length(); k++) {
                    JSONObject jo = (JSONObject) ja.get(k);
                    RadioButton button = new RadioButton(getActivity());
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 55);
                    params.setMargins(0, 15, 0, 15);
                    String text_add = "";
                    if (jo.has("shopNameOrHouseName")) {
                        text_add = text_add + "," + jo.getString("shopNameOrHouseName");
                    }
                    if (jo.has("shopNoOrHouseNo")) {
                        text_add = text_add + "," + jo.getString("shopNoOrHouseNo");
                    }
                    if (jo.has("complex")) {
                        text_add = text_add + "," + jo.getString("complex");
                    }
                    if (jo.has("societyName")) {
                        text_add = text_add + "," + jo.getString("societyName");
                    }
                    if (jo.has("societyNo")) {
                        text_add = text_add + "," + jo.getString("societyNo");
                    }
                    if (jo.has("streetName")) {
                        text_add = text_add + "," + jo.getString("streetName");
                    }
                    if (jo.has("streetNo")) {
                        text_add = text_add + "," + jo.getString("streetNo");
                    }
                    if (jo.has("road")) {
                        text_add = text_add + "," + jo.getString("road");
                    }
                    if (jo.has("landmark")) {
                        text_add = text_add + "," + jo.getString("landmark");
                    }
                    if (jo.has("mallName")) {
                        text_add = text_add + "," + jo.getString("mallName");
                    }
                    Log.i("buttontext", text_add);
                    button.setText(text_add);
                    button.setId(jo.getInt("addId"));
                    group1.addView(button);
                }
            } catch (Exception e) {
            }
        }
    }

    class add_radio extends AsyncTask<String, String, String> {
        RadioGroup buttons;
        Button btn_check;

        public add_radio(RadioGroup buttons, Button btn_check) {
            this.btn_check = btn_check;
            this.buttons = buttons;
        }

        @Override
        protected String doInBackground(String... strings) {
            String json_paymentmode = "";
            try {
                String connection = getActivity().getResources().getString(R.string.connection_data);
                URL upay = new URL(connection + "clientele.paymentmode");
                Log.i("urlpay", upay.toString());
                URLConnection uconpay = upay.openConnection();
                InputStream inspay = uconpay.getInputStream();
                BufferedInputStream bispay = new BufferedInputStream(inspay);
                int p = 0;
                while ((p = bispay.read()) != -1) {
                    json_paymentmode = json_paymentmode + (char) p;
                }
            } catch (Exception e) {
            }
            json_paymentmode = json_paymentmode.trim();
            return json_paymentmode;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray ja = new JSONArray(s);
                for (int k = 0; k < ja.length(); k++) {
                    JSONObject jo = (JSONObject) ja.get(k);
                    RadioButton button = new RadioButton(getActivity());
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 55);
                    params.setMargins(0, 15, 0, 15);
                    button.setText(jo.getString("mode"));
                    button.setId(jo.getInt("paymentModeId"));
                    buttons.addView(button);
                }
            } catch (Exception e) {
            }
        }
    }

    class insertOrder extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String orderId = "";
            try {
                String connection = getActivity().getResources().getString(R.string.connection_data);
                URL uoder = new URL(connection + "clientele.productorder/placeorder/" + strings[0] + "/" + strings[1]);
                Log.i("insertorder", uoder.toString());
                URLConnection uconorder = uoder.openConnection();
                InputStream insorder = uconorder.getInputStream();
                BufferedInputStream bisorder = new BufferedInputStream(insorder);
                int or;
                while ((or = bisorder.read()) != -1) {
                    orderId = orderId + (char) or;
                }
            } catch (Exception e) {
            }
            orderId = orderId.trim();
            return orderId;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int orderid = Integer.parseInt(s);
            Log.i("checkoutorderid", orderid + "");
            insertOrderDetail od = new insertOrderDetail();
            od.execute(orderid + "");
        }
    }

    class insertOrderDetail extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String json_orderDetails = "";
            try {
                String connection=getActivity().getResources().getString(R.string.connection_data);
                JSONArray ja = new JSONArray();
                for (int m = 0; m < lst.size(); m++) {
                    JSONObject jo = new JSONObject();
                    jo.put("orderId", strings[0]);
                    jo.put("productDetailId", lst.get(m).getProduct_id());
                    jo.put("qty", lst.get(m).getProduct_qty());
                    ja.put(jo);
                }
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(connection+"clientele.orderdetail/createOrderDetail");
                json_orderDetails=ja.toString();


                httpPost.setEntity(new StringEntity(json_orderDetails,"UTF-8"));
                httpPost.setHeader("Accept","text/plain");
                httpPost.setHeader("content-type","application/json");
                HttpResponse httpResponse=httpClient.execute(httpPost);
                String res=httpResponse.getStatusLine().getStatusCode()+"";
                Log.i("responsehttp",res);
                Toast.makeText(getActivity(),"order has been placed",Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){}
            return null;
        }

    }
}
