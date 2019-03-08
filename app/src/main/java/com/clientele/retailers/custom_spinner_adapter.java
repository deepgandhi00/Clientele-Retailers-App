package com.clientele.retailers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Royal on 01-Mar-18.
 */

public class custom_spinner_adapter extends BaseAdapter {
    Activity activity;
    List<pojo_product_details> lst;
    LayoutInflater inflater;

    public custom_spinner_adapter(Activity activity,List<pojo_product_details> lst){
        this.activity=activity;
        this.lst=lst;
        inflater=activity.getLayoutInflater();
    }
    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=inflater.inflate(R.layout.spinner_row_product,null);
        TextView data=v.findViewById(R.id.spinner_product_text);
        if(lst.get(i).getQty()!=null){
            data.setText(lst.get(i).getQty()+"-"+lst.get(i).getCost());
        }
        else{
            data.setText(lst.get(i).getWt()+"-"+lst.get(i).getCost());
        }
        return v;
    }
}
