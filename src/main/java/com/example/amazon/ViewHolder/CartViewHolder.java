package com.example.amazon.ViewHolder;

import android.view.View;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazon.Interface.ItemClickListner;
import com.example.amazon.R;


public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvCartProductName , tvCartProductQuantity , tvCartProductPrice;

    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);


            tvCartProductName = itemView.findViewById(R.id.tvCartProductName);
        tvCartProductQuantity = itemView.findViewById(R.id.tvCartProductQuantity);
        tvCartProductPrice = itemView.findViewById(R.id.tvCartProductPrice);


    }

    @Override
    public void onClick(View v) {

        itemClickListner.onClick(v , getAdapterPosition() , false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
