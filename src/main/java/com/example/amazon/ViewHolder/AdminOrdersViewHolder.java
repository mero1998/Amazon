package com.example.amazon.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazon.R;

public class AdminOrdersViewHolder extends RecyclerView.ViewHolder {

    public TextView tvUserName_orders , tvPhoneNumber_orders , tvAddress_City_orders, tvTotalPrice_orders , tvDate_time_orders;
    public Button btnShowAllProducts;
    public AdminOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        tvUserName_orders =  (TextView ) itemView.findViewById(R.id.tvUserName_orders);
        tvPhoneNumber_orders =  (TextView )itemView.findViewById(R.id.tvPhoneNumber_orders);
        tvAddress_City_orders = (TextView ) itemView.findViewById(R.id.tvAddress_City_orders);
        tvTotalPrice_orders = (TextView ) itemView.findViewById(R.id.tvTotalPrice_orders);
        tvDate_time_orders = (TextView ) itemView.findViewById(R.id.tvDate_time_orders);

        btnShowAllProducts = (Button ) itemView.findViewById(R.id.btnShowAllProducts);

    }
}
