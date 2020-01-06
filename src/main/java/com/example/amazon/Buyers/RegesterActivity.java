package com.example.amazon.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amazon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegesterActivity extends AppCompatActivity {

    private Button btnRegester;
    private EditText regester_username, regester_password , regester_phoneNamber ;
    private ProgressDialog loadingBar;
    private DatabaseReference RootReef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regester);

        btnRegester = findViewById(R.id.btnRegester);
        regester_username = findViewById(R.id.regester_username);
        regester_password = findViewById(R.id.regester_password);
        regester_phoneNamber = findViewById(R.id.regester_phoneNamber);
       loadingBar = new ProgressDialog(this);


        btnRegester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }

    private void CreateAccount() {

        String name = regester_username.getText().toString();
        String phone = regester_phoneNamber.getText().toString();
        String password = regester_password.getText().toString();

        if( TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please write your name ..", Toast.LENGTH_SHORT).show();
        }

       else if( TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please write your phone number..", Toast.LENGTH_SHORT).show();
        }

       else if( TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please write your password ..", Toast.LENGTH_SHORT).show();
        }

       else{

           loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please Wait , while we are checking the cradentials . ");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(name , phone ,password);
        }

    }

    private void ValidatePhoneNumber(final String name, final String phone, final String password) {

        RootReef = FirebaseDatabase.getInstance().getReference();

        RootReef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!(dataSnapshot.child("Users").child(phone).exists())){

                   HashMap<String,Object> userDataMap = new HashMap<>();
                    userDataMap.put("phone" , phone);
                    userDataMap.put("password" , password);
                    userDataMap.put("name" , name);

                    RootReef.child("Users").child(phone).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Toast.makeText(RegesterActivity.this, "Your account has been created ", Toast.LENGTH_SHORT).show();

                                    loadingBar.dismiss();

                                        Intent intent = new Intent(RegesterActivity.this , LoginActivity.class);
                                       startActivity(intent);


                                    }
                                      else{
                                        Toast.makeText(RegesterActivity.this, "Network Error : Please try again after some time ..", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{

                    Toast.makeText(RegesterActivity.this, "This " + phone + "already exists", Toast.LENGTH_SHORT).show();
                   loadingBar.dismiss();
                    Toast.makeText(RegesterActivity.this, "Please try again using another phone number .", Toast.LENGTH_SHORT).show();

                    Intent regesterIntent = new Intent(RegesterActivity.this , MainActivity.class);
                    startActivity(regesterIntent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
