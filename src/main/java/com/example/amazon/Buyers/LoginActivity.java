package com.example.amazon.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amazon.Admin.AdminCategoryActivity;
import com.example.amazon.Model.Users;
import com.example.amazon.Prevalent.Prevalent;
import com.example.amazon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText login_password , login_phoneNamber ;
    private ProgressDialog loadingBar;
    private DatabaseReference RootRef;
    private CheckBox remember_me_cb;
    private String parentDBName = "Users";
    private TextView txtAdminPanel_link , txtNotAdminPanel_link,txtForgetPassword_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        login_password = findViewById(R.id.login_password);
        login_phoneNamber = findViewById(R.id.login_phoneNamber);
        loadingBar = new ProgressDialog(this);
        RootRef = FirebaseDatabase.getInstance().getReference();
        remember_me_cb = findViewById(R.id.remember_me_cb);
        txtAdminPanel_link = findViewById(R.id.txtAdminPanel_link);
        txtNotAdminPanel_link = findViewById(R.id.txtNotAdminPanel_link);
        txtForgetPassword_link = findViewById(R.id.txtForgetPassword_link);
        Paper.init(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        txtForgetPassword_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this , ResstPasswordActivity.class);
                intent.putExtra("check" , "login");
                startActivity(intent);
            }
        });
        txtAdminPanel_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setText("Login Admin");
                txtAdminPanel_link.setVisibility(View.INVISIBLE);
                txtNotAdminPanel_link.setVisibility(View.VISIBLE);
                parentDBName = "Admins";
            }
        });

        txtNotAdminPanel_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.setText("Login");
                txtAdminPanel_link.setVisibility(View.VISIBLE);
                txtNotAdminPanel_link.setVisibility(View.INVISIBLE);
                parentDBName = "Users";
            }
        });
    }

    private void LoginUser() {

        String password = login_password.getText().toString();
        String phone = login_phoneNamber.getText().toString();


        if( TextUtils.isEmpty(password)){

            Toast.makeText(this, "Please write your password ..", Toast.LENGTH_SHORT).show();
        }


    else if( TextUtils.isEmpty(phone)){

            Toast.makeText(this, "Please write your phone number ..", Toast.LENGTH_SHORT).show();
        }

        else{

            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please Wait , while we are checking the cradentials . ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone , password);
        }
    }

    private void AllowAccessToAccount(final String phone, final String password) {

        if(remember_me_cb.isChecked()){

            Paper.book().write(Prevalent.UserPhoneKey , phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }
    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(dataSnapshot.child(parentDBName).child(phone).exists()){

                Users usersData = dataSnapshot.child(parentDBName).child(phone).getValue(Users.class);

                if(usersData.getPhone().equals(phone)){
                    if(usersData.getPassword().equals(password)){
                        if(parentDBName.equals("Admins")){
                            Toast.makeText(LoginActivity.this, "Welcome Admin , You are Logged is successfully ..", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent homeIntent = new Intent(LoginActivity.this , AdminCategoryActivity.class);
                            startActivity(homeIntent);
                        }

                        else if(parentDBName.equals("Users")){

                            Toast.makeText(LoginActivity.this, "Logged is successfully ..", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Prevalent.currentOnlineUser = usersData;
                            Intent homeIntent = new Intent(LoginActivity.this , HomeActivity.class);
                            startActivity(homeIntent);
                        }

                    }
                    else{
                        loadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();

                    }
                }
            }
            else{
                Toast.makeText(LoginActivity.this, "Account with this " + phone + "number not exists", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }


}
