package com.example.amazon.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazon.Interface.ItemClickListner;
import com.example.amazon.R;

public class ProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView tvProduct_name , tvProduct_description, tvProduct_price;
    public ImageView imgProduct;
    public  ItemClickListner listner;

    public ProductsViewHolder(@NonNull View itemView) {
        super(itemView);

        imgProduct = (ImageView) itemView.findViewById(R.id.imgProduct);
        tvProduct_name = (TextView) itemView.findViewById(R.id.tvProduct_name);
        tvProduct_description = (TextView) itemView.findViewById(R.id.tvProduct_description);
        tvProduct_price = (TextView) itemView.findViewById(R.id.tvProduct_price);
    }

    public  void setItemClicListner(ItemClickListner listner){

        this.listner = listner;

    }

    @Override
    public void onClick(View v) {

        listner.onClick(v , getAdapterPosition() , false);
    }
}
