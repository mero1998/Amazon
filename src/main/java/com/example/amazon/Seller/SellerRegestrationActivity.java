package com.example.amazon.Seller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amazon.Buyers.MainActivity;
import com.example.amazon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegestrationActivity extends AppCompatActivity {

    private Button btnRegisterSeller , btnAlreadyHaveAccountSeller;
    private EditText txtNameSellerRegister, txtPhoneSellerRegister
            ,txtEmailSellerRegister ,txtPasswordSellerRegister , txtAddressSellerRegister;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_regestration);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);
        btnRegisterSeller = findViewById(R.id.btnRegisterSeller);
        btnAlreadyHaveAccountSeller = findViewById(R.id.btnAlreadyHaveAccountSeller);
        txtNameSellerRegister = findViewById(R.id.txtNameSellerRegister);
        txtPhoneSellerRegister = findViewById(R.id.txtPhoneSellerRegister);
        txtEmailSellerRegister = findViewById(R.id.txtEmailSellerRegister);
        txtPasswordSellerRegister = findViewById(R.id.txtPasswordSellerRegister);
        txtAddressSellerRegister = findViewById(R.id.txtAddressSellerRegister);

        btnAlreadyHaveAccountSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegestrationActivity.this , SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegisterSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });



    }

    private void registerSeller() {

        final String name = txtNameSellerRegister.getText().toString();
        final String phone = txtPhoneSellerRegister.getText().toString();
        final String email = txtEmailSellerRegister.getText().toString();
        final String password = txtPasswordSellerRegister.getText().toString();
        final String address = txtAddressSellerRegister.getText().toString();

        if(!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals("") ){

            loadingBar.setTitle("Creating Seller Account");
            loadingBar.setMessage("Please Wait , while we are checking the credentials . ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                final DatabaseReference ref;
                                ref = FirebaseDatabase.getInstance().getReference();

                                String sid = mAuth.getCurrentUser().getUid();

                                HashMap<String , Object> sellerMap = new HashMap<>();
                                sellerMap.put("sid",sid);
                                sellerMap.put("email",email);
                                sellerMap.put("phone",phone);
                                sellerMap.put("name",name);
                                sellerMap.put("address",address);
                                sellerMap.put("password",password);

                                ref.child("sellers").child(sid).updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                loadingBar.dismiss();
                                                Toast.makeText(SellerRegestrationActivity.this, "Yor are registered successfully ", Toast.LENGTH_SHORT).show();

                                                Intent intent  = new Intent(SellerRegestrationActivity.this , SellerHomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });


                            }
                        }
                    });
        }
        else{
            Toast.makeText(this, "Please , complete the form registration .", Toast.LENGTH_SHORT).show();
        }
    }
}
