package com.clientele.retailers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Royal on 08-Mar-18.
 */

public class cartRecyclerAdapter extends RecyclerView.Adapter<cartRecyclerAdapter.ViewHolder> {
    List<pojo_cart_product> lst_cart=new ArrayList<>();
    Fragment fm;
    cart_sqlhelper sqlhelper;
    TextView tv_cost;
    Button chkout;

    public cartRecyclerAdapter(List<pojo_cart_product> lst_cart, Fragment fm,TextView tv_cost,Button chkout) {
        this.lst_cart = lst_cart;
        this.fm = fm;
        this.tv_cost=tv_cost;
        this.chkout=chkout;
        sqlhelper=new cart_sqlhelper(fm.getActivity(),"retailer",98);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card_layout, parent, false);
        cartRecyclerAdapter.ViewHolder viewHolder = new cartRecyclerAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            pojo_cart_product p = lst_cart.get(position);
            String connection_url = fm.getContext().getResources().getString(R.string.connection_image);
            URL u = new URL(connection_url + p.getProduct_image());
            Log.i("pimageurl", u.toString());
            Bitmap bmp = BitmapFactory.decodeStream(u.openStream());
            holder.cid = p.getCid();
            final int delete_id=p.getCid();
            holder.imgv.setImageBitmap(bmp);
            holder.tv_pname.setText(p.getProduct_name());
            final int product_cost=Integer.parseInt(p.getCost());
            tv_cost.setText("TOTAL : -"+product_cost*p.getQty());
            chkout.setText("BUY FOR Rs. "+product_cost*p.getQty());
            if(p.getProduct_qty()!=null){
                holder.tv_qtywt.setText(p.getProduct_qty());
            }
            else{
                holder.tv_qtywt.setText(p.getProduct_wt());
            }
            holder.tv_cost.setText(p.getCost());
            holder.btn.setRange(1,100);
            holder.btn.setNumber(p.getQty()+"");
            holder.btn.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    boolean b=sqlhelper.update_pcart(delete_id,newValue);
                    int chhout_cost=product_cost*newValue;
                    chkout.setText("BUY FOR Rs. " +chhout_cost);
                    tv_cost.setText("TOTAL : -" +chhout_cost);
                }
            });


            holder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("cliced","clicked");
                    Toast.makeText(fm.getActivity(),"deleted",Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e){}
    }

        @Override
        public int getItemCount () {
            return lst_cart.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
        int cid;
        ImageView imgv;
        ImageButton tv_delete;
        TextView tv_pname,tv_qtywt,tv_cost;
        ElegantNumberButton btn;
            public ViewHolder(View itemView) {
                super(itemView);
            imgv=itemView.findViewById(R.id.cart_card_image);
            tv_delete=itemView.findViewById(R.id.cart_card_delete);
            tv_pname=itemView.findViewById(R.id.cart_card_prduct_name);
            tv_qtywt=itemView.findViewById(R.id.cart_card_qtywt);
            tv_cost=itemView.findViewById(R.id.cart_card_cost);
            btn=itemView.findViewById(R.id.cart_card_elegant);
            }
        }
    }