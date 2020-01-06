package com.example.amazon.Buyers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazon.Model.Cart;
import com.example.amazon.Prevalent.Prevalent;
import com.example.amazon.R;
import com.example.amazon.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private TextView tvTotalPrice , tvMsg1;
    private Button btnNextProcess;
    private int totalOverPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cartList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnNextProcess = findViewById(R.id.btnNextProcess);
        tvMsg1 = findViewById(R.id.tvMsg1);
        btnNextProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTotalPrice.setText( "Total Price = $ " + String.valueOf(totalOverPrice));
                Intent intent = new Intent(CartActivity.this , ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(totalOverPrice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderState();
       final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                        .child(Prevalent.currentOnlineUser.getPhone())
                        .child("products"), Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart , CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {

                holder.tvCartProductName.setText("Name = " + model.getProductName());
                holder.tvCartProductQuantity.setText("Quantity = " +  model.getQuantity());
                holder.tvCartProductPrice.setText("Price = " + model.getPrice() + " $");

                int oneTypeProductTotalPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());
                totalOverPrice =  totalOverPrice + oneTypeProductTotalPrice;



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[] = new CharSequence[]
                        {
                            "Edit",
                                "Remove"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                                if(i == 0){
                                    Intent intent = new Intent(CartActivity.this , ProductDetailsActivity.class);
                                    intent.putExtra("pid" , model.getPid());
                                    startActivity(intent);
                                }
                                
                                if(i == 1 ){
                                    
                                    cartListRef.child("User View ")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(CartActivity.this, "Item removed successfully . ", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CartActivity.this , HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view  =LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout , parent , false);

                CartViewHolder holder = new CartViewHolder(view);

                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
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
                        String userName = dataSnapshot.child("name").getValue().toString();

                        if(shipmentState.equals("Shipped")){
                            tvTotalPrice.setText("Dear " + userName + " \n orders shipped successfully");
                            recyclerView.setVisibility(View.GONE);

                            tvMsg1.setVisibility(View.VISIBLE);
                            tvMsg1.setText("Congratulations , your final order has been shipped successflly , soon you will received your order at your door step");
                            btnNextProcess.setVisibility(View.GONE);
                            Toast.makeText(CartActivity.this, "You can Purchase more products , once your received first final orders ", Toast.LENGTH_SHORT).show();
                        }
                        else if(shipmentState.equals("Not Shipped")){

                            tvTotalPrice.setText("Shipped State = Not Shipped");
                            recyclerView.setVisibility(View.GONE);
                            tvMsg1.setVisibility(View.VISIBLE);
                            btnNextProcess.setVisibility(View.GONE);
                            Toast.makeText(CartActivity.this, "You can Purchase more products , once your received first final orders ", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }
}
