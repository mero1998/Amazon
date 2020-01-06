package com.example.amazon.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazon.Model.Cart;
import com.example.amazon.Prevalent.Prevalent;
import com.example.amazon.R;
import com.example.amazon.ViewHolder.AdminOrdersViewHolder;
import com.example.amazon.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class AdminUserProductsActivity extends AppCompatActivity {
        private RecyclerView productsList;
        RecyclerView.LayoutManager layoutManager;
        private DatabaseReference cartListRef;

        private  String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        userID = getIntent().getStringExtra("uid");

        productsList = findViewById(R.id.productsList);
        productsList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productsList.setLayoutManager(layoutManager);

        cartListRef =FirebaseDatabase.getInstance().getReference()
                .child("Cate List")
                .child("Admin View")
                .child(userID)
        .child("products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef , Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart , CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                holder.tvCartProductName.setText("Name = " + model.getProductName());
                holder.tvCartProductQuantity.setText("Quantity = " +  model.getQuantity());
                holder.tvCartProductPrice.setText("Price = " + model.getPrice() + " $");
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view  =LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout , parent , false);

                CartViewHolder holder = new CartViewHolder(view);

                return holder;
            }
        };

        productsList.setAdapter(adapter);
        adapter.startListening();
    }
}
