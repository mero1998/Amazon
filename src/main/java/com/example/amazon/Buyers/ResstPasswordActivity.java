package com.example.amazon.Buyers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amazon.Prevalent.Prevalent;
import com.example.amazon.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResstPasswordActivity extends AppCompatActivity {

    private String check = "";

    private TextView tvPageTitle, tvQuestionTitle;
    private EditText txtFindPhoneNumber ,txtQuestion_1 ,txtQuestion_2;
    private Button btnVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resst_password);

        check = getIntent().getStringExtra("check");

        tvPageTitle = findViewById(R.id.tvPageTitle);
        txtFindPhoneNumber = findViewById(R.id.txtFindPhoneNumber);
        txtQuestion_1 = findViewById(R.id.txtQuestion_1);
        txtQuestion_2 = findViewById(R.id.txtQuestion_2);
        btnVerify = findViewById(R.id.btnVerify);

    }

    @Override
    protected void onStart() {
        super.onStart();

        txtFindPhoneNumber.setVisibility(View.GONE);

        if(check.equals("settings")){

            tvPageTitle.setText("Set Question");
            tvQuestionTitle.setText("Please set answers  for the following security question");
            btnVerify.setText("Set");
            displayPreviousAnswer();

            btnVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setAnswer();
                }
            });
        }
        else if(check.equals("login")){

            txtFindPhoneNumber.setVisibility(View.VISIBLE);
        
            btnVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });
        }
    }

    private void verifyUser() {

        final String phone = txtFindPhoneNumber.getText().toString();
        final String answer1 = txtQuestion_1.getText().toString().toLowerCase();
        final String answer2 = txtQuestion_2.getText().toString().toLowerCase();

        if(!phone.equals("") && !answer1.equals("") && !answer2.equals("")){
            final DatabaseReference ref =FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(phone);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String mPhone = dataSnapshot.child("phone").getValue().toString();

                        if(phone.equals(mPhone)){

                            if(dataSnapshot.hasChild("Security Question")){
                                String ans1 = dataSnapshot.child("Security Question").child("answer 1").getValue().toString();
                                String ans2 = dataSnapshot.child("Security Question").child("answer 2").getValue().toString();

                                if(!ans1.equals(answer1)){

                                    Toast.makeText(ResstPasswordActivity.this, "your answer 1st is wrong.", Toast.LENGTH_SHORT).show();
                                }
                                else  if(!ans2.equals(answer2)){

                                    Toast.makeText(ResstPasswordActivity.this, "your answer 2nd is wrong.", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ResstPasswordActivity.this);
                                    builder.setTitle("New Password");
                                    final EditText newPassword = new EditText(ResstPasswordActivity.this);
                                    newPassword.setHint("Write New Password Here ..");

                                    builder.setView(newPassword);
                                    builder.setPositiveButton("Change password", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            if(!newPassword.getText().toString().equals("")){
                                                ref.child("password")
                                                        .setValue(newPassword.getText()
                                                                .toString())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                Toast.makeText(ResstPasswordActivity.this, "Password changed successfully .", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(ResstPasswordActivity.this , LoginActivity.class);
                                                                startActivity(intent);
                                                            }
                                                        });
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.cancel();
                                        }
                                    });
                                builder.show();

                                }
                            }
                        }
                        else{
                            Toast.makeText(ResstPasswordActivity.this, "This user phone number not exists", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
            else{
            Toast.makeText(this, "Please , complete the form", Toast.LENGTH_SHORT).show();
        }

    }

    private void setAnswer(){
        String answer1 = txtQuestion_1.getText().toString().toLowerCase();
        String answer2 = txtQuestion_2.getText().toString().toLowerCase();

        if(txtQuestion_1.equals("") && txtQuestion_2.equals("")){

            Toast.makeText(ResstPasswordActivity.this, "Please answer both question ", Toast.LENGTH_SHORT).show();
        }
        else{

            DatabaseReference ref =FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String , Object> userMap = new HashMap<>();
            userMap.put("answer 1", answer1);
            userMap.put("answer 2", answer2);

            ref.child("Security Question").updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ResstPasswordActivity.this, "You have set security questions successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResstPasswordActivity.this , HomeActivity.class);
                        startActivity(intent);
                    }
                }
            });

        }
    }

    private void displayPreviousAnswer(){
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());
        ref.child("Security Question").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    String ans1 = dataSnapshot.child("answer 1").getValue().toString();
                    String ans2 = dataSnapshot.child("answer 2").getValue().toString();

                    txtQuestion_1.setText(ans1);
                    txtQuestion_2.setText(ans2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
