package com.clientele.retailers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Royal on 16-Feb-18.
 */

public class logout_frag extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.layout_logout_frag,container,false);
        SharedPreferences sh=getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed=sh.edit();
        ed.remove("email");
        ed.commit();
        Intent intent=new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
        return v;
    }
}
