package com.example.amazon.Buyers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazon.Model.Users;
import com.example.amazon.Prevalent.Prevalent;
import com.example.amazon.R;
import com.example.amazon.Seller.SellerHomeActivity;
import com.example.amazon.Seller.SellerLoginActivity;
import com.example.amazon.Seller.SellerRegestrationActivity;
import com.google.android.gms.internal.firebase_auth.zzew;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.zzy;
import com.google.firebase.auth.zzz;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button btn_mainLogin , btn_mainJoin;
    private DatabaseReference RootRef;
    private ProgressDialog loadingBar;
    private TextView tvBecomeSeller;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btn_mainLogin = findViewById(R.id.btn_mainLogin);
        btn_mainJoin = findViewById(R.id.btn_mainJoin);
        tvBecomeSeller = findViewById(R.id.tvBecomeSeller);
        RootRef = FirebaseDatabase.getInstance().getReference();

        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        btn_mainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this , LoginActivity.class);
                startActivity(loginIntent);

            }
        });

        tvBecomeSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this , SellerRegestrationActivity.class);
                startActivity(loginIntent);

            }
        });

        btn_mainJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regesterIntent = new Intent(MainActivity.this , RegesterActivity.class);
                startActivity(regesterIntent);
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if(UserPhoneKey != "" && UserPasswordKey != ""){
            if( !TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){

                AllowAccess(UserPhoneKey , UserPasswordKey);

                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("Please Wait ....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            Intent intent  = new Intent(MainActivity.this , SellerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void AllowAccess(final String phone, final String password) {


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Users").child(phone).exists()){

                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone)){
                        if(usersData.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this, "Please Wait , you are already logged in ..", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent homeIntent = new Intent(MainActivity.this , HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(homeIntent);

                        }
                        else{
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Account with this " + phone + "number not exists", Toast.LENGTH_SHORT).show();
                   loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
