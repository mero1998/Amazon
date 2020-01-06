package com.example.amazon.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazon.Model.AdminOrders;
import com.example.amazon.R;
import com.example.amazon.ViewHolder.AdminOrdersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;
    private TextView tvUserName_orders ,tvPhoneNumber_orders ,tvTotalPrice_orders ,tvAddress_City_orders ,tvDate_time_orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef =FirebaseDatabase.getInstance().getReference().child("orders");
        ordersList = findViewById(R.id.ordersList);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
        tvUserName_orders = findViewById(R.id.tvUserName_orders);
        tvPhoneNumber_orders = findViewById(R.id.tvPhoneNumber_orders);
        tvTotalPrice_orders = findViewById(R.id.tvTotalPrice_orders);
        tvAddress_City_orders = findViewById(R.id.tvAddress_City_orders);
        tvDate_time_orders = findViewById(R.id.tvDate_time_orders);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef , AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders , AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {

                holder.tvUserName_orders.setText("Name: " + model.getName());
                holder.tvPhoneNumber_orders.setText("Phone Number: " + model.getPhone());
                holder.tvTotalPrice_orders.setText("Total Price: $ " + model.getTotalPrice());
                holder.tvAddress_City_orders.setText("Address: " + model.getAddress() + model.getCity());
                holder.tvDate_time_orders.setText("Order at: " + model.getData() + model.getTime());

                holder.btnShowAllProducts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String uID = getRef(position).getKey();
                        Intent intent = new Intent(AdminNewOrdersActivity.this , AdminUserProductsActivity.class);
                        intent.putExtra("uid" , uID);
                        startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                                {
                                  "yes",
                                  "No"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                        builder.setTitle("Have you shipped this order products ?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                                if(i == 0){
                                    String uID = getRef(position).getKey();

                                    removeOrder(uID);
                                }
                                else
                                    {
                                        finish();
                                }
                            }
                        });

                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout , parent , false);

                return new AdminOrdersViewHolder(view);
            }
        };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeOrder(String uID) {

        ordersRef.child(uID).removeValue();

    }
}
