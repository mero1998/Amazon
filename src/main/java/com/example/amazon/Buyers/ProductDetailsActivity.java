package com.example.amazon.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.amazon.Model.Products;
import com.example.amazon.Prevalent.Prevalent;
import com.example.amazon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private Button btnAddProductToCart;
    private ImageView imgProduct_details;
    private TextView tvProductName_details , tvProductDescription_details ,tvProductPrice_details;
    private ElegantNumberButton  btnNumber;
    private  String ProductID = "" , state = "Normal";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ProductID = getIntent().getStringExtra("pid");
        btnNumber = findViewById(R.id.btnNumber);
        imgProduct_details = findViewById(R.id.imgProductDetails);
        tvProductName_details = findViewById(R.id.tvProductName_details);
        tvProductDescription_details = findViewById(R.id.tvProductDescription_details);
        tvProductPrice_details = findViewById(R.id.tvProductPrice_details);
        btnAddProductToCart = findViewById(R.id.btnAddProductToCart);

        getProductDetails(ProductID);

        btnAddProductToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(state.equals("Order Shipped") || state.equals("Order Placed")){

                    Toast.makeText(ProductDetailsActivity.this, "You can add purchase more products , once your order is shipped or confirmed ", Toast.LENGTH_LONG).show();

                }
                else {
                    addingToCartList();
                }
            }


        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    checkOrderState();
    }

    private void addingToCartList() {

        String saveCurrentDate , saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd , yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());


        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String , Object> cartMap = new HashMap<>();
        cartMap.put("pid" , ProductID);
        cartMap.put("ProductName" , tvProductName_details.getText().toString());
        cartMap.put("Price" , tvProductPrice_details.getText().toString());
        cartMap.put("quantity" , btnNumber.getNumber());
        cartMap.put("date" , saveCurrentDate);
        cartMap.put("time" , saveCurrentTime);
        cartMap.put("discount" , "");

        cartRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("products").child(ProductID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            cartRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("products").child(ProductID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            Toast.makeText(ProductDetailsActivity.this, "Added to cart list .", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ProductDetailsActivity.this , HomeActivity.class);
                                            startActivity(intent);
                                           }
                                    });
                        }
                    }
                });


    }

    private void getProductDetails(String productID) {

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("products");
        productsRef.child(ProductID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Products products = dataSnapshot.getValue(Products.class);

                tvProductName_details.setText(products.getName());
                tvProductPrice_details.setText(products.getPrice());
                tvProductDescription_details.setText(products.getDescription());
                Picasso.get().load(products.getImage()).into(imgProduct_details);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkOrderState(){

        DatabaseReference orderRef;

        orderRef =( DatabaseReference ) FirebaseDatabase.getInstance().getReference()
                .child("orders")
                .child(Prevalent.currentOnlineUser.getPhone())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String shipmentState = dataSnapshot.child("state").getValue().toString();

                            if(shipmentState.equals("Shipped")){

                                state = "Order Shipped";
                            }
                            else if(shipmentState.equals("Not Shipped")){

                                state = "Order Placed";

                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


}
