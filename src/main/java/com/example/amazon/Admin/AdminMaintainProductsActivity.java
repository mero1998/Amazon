package com.example.amazon.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amazon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private EditText txtProduct_name_maintain , txtProduct_price_maintain ,txtProduct_description_maintain;
    private Button btnApplyChange_maintain , btnDeleteProduct_maintain;
    private ImageView imgProduct_maintain;
    private  String ProductID = "";
    private DatabaseReference ProductsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);


        ProductID = getIntent().getStringExtra("pid");
        ProductsRef =FirebaseDatabase.getInstance().getReference().child("products").child(ProductID);
        txtProduct_description_maintain = findViewById(R.id.txtProduct_description_maintain);
        txtProduct_price_maintain = findViewById(R.id.txtProduct_price_maintain);
        txtProduct_name_maintain = findViewById(R.id.txtProduct_name_maintain);
        btnApplyChange_maintain = findViewById(R.id.btnApplyChange_maintain);
        imgProduct_maintain = findViewById(R.id.imgProduct_maintain);
        btnDeleteProduct_maintain = findViewById(R.id.btnDeleteProduct_maintain);
        displaySpecificProductsInfo();

        btnApplyChange_maintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        btnDeleteProduct_maintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                deleteThisProduct();
            }
        });
    }

    private void deleteThisProduct() {

    ProductsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {

            Intent intent = new Intent(AdminMaintainProductsActivity.this , AdminCategoryActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(AdminMaintainProductsActivity.this, "The Product is deleted successfully", Toast.LENGTH_SHORT).show();
        }
    });
    }

    private void applyChanges() {

        String pName = txtProduct_name_maintain.getText().toString();
        String pPrice = txtProduct_price_maintain.getText().toString();
        String pDescription = txtProduct_description_maintain.getText().toString();

        if(pName.equals("")){
            Toast.makeText(this, "Write down Product Name . ", Toast.LENGTH_SHORT).show();
        }

      else  if(pPrice.equals("")){
            Toast.makeText(this, "Write down Product Price . ", Toast.LENGTH_SHORT).show();
        }

        else if(pDescription.equals("")){
            Toast.makeText(this, "Write down Product Description . ", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String , Object> productMap = new HashMap<>();
            productMap.put("pid" , ProductID);
            productMap.put("description" , pDescription);
            productMap.put("price" , pPrice);
            productMap.put("name" , pName);

            ProductsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes Applied Successfully . ", Toast.LENGTH_SHORT).show();

                        Intent intent  = new Intent(AdminMaintainProductsActivity.this , AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

    private void displaySpecificProductsInfo() {

        ProductsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    String name = dataSnapshot.child("name").getValue().toString();
                    String price = dataSnapshot.child("price").getValue().toString();
                    String description = dataSnapshot.child("description").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();

                    txtProduct_name_maintain.setText(name);
                    txtProduct_description_maintain.setText(description);
                    txtProduct_price_maintain.setText(price);
                    Picasso.get().load(image).into(imgProduct_maintain);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
