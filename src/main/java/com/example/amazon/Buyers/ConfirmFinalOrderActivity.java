package com.example.amazon.Buyers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amazon.Prevalent.Prevalent;
import com.example.amazon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText txtNameShipment , txtPhoneShipment , txtAddressShipment ,txtCityShipment;
    private Button btnConfirmFinalOrder;

    private String totalPrice = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalPrice = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price = $" + totalPrice, Toast.LENGTH_SHORT).show();
        txtNameShipment = findViewById(R.id.txtNameShipment);
        txtPhoneShipment = findViewById(R.id.txtPhoneShipment);
        txtAddressShipment = findViewById(R.id.txtAddressShipment);
        txtCityShipment = findViewById(R.id.txtCityShipment);
        btnConfirmFinalOrder = findViewById(R.id.btnConfirmFinalOrder);


        btnConfirmFinalOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });


    }

    private void check() {

            if( TextUtils.isEmpty(txtNameShipment.getText().toString())){
                Toast.makeText(this, "Please , provide your full name .", Toast.LENGTH_SHORT).show();
            }

            else   if( TextUtils.isEmpty(txtPhoneShipment.getText().toString())){
                Toast.makeText(this, "Please , provide your phone .", Toast.LENGTH_SHORT).show();
            }


            else   if( TextUtils.isEmpty(txtAddressShipment.getText().toString())){
                Toast.makeText(this, "Please , provide your address .", Toast.LENGTH_SHORT).show();
            }

            else   if( TextUtils.isEmpty(txtCityShipment.getText().toString())){
                Toast.makeText(this, "Please , provide your city name .", Toast.LENGTH_SHORT).show();
            }
            else {
                confirmOrder();
            }

    }

    private void confirmOrder() {
       final String saveCurrentDate , saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd , yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        DatabaseReference ordersRef =FirebaseDatabase.getInstance().getReference()
                .child("orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String , Object> orderMap = new HashMap<>();

        orderMap.put("totalPrice" , totalPrice);
        orderMap.put("name" , txtNameShipment.getText().toString());
        orderMap.put("phone" , txtPhoneShipment.getText().toString());
        orderMap.put("address" , txtAddressShipment.getText().toString());
        orderMap.put("city" , txtCityShipment.getText().toString());
        orderMap.put("date" , saveCurrentDate);
        orderMap.put("time" , saveCurrentTime);
        orderMap.put("state" , "Not Shipped");

        ordersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if ( task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .child("products")
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(ConfirmFinalOrderActivity.this, "Your final order has been placed successfully .", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ConfirmFinalOrderActivity.this , HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();

                            }
                        }
                    });
                }
            }
        });

    }
}
