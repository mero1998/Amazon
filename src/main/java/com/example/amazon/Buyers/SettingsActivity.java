package com.example.amazon.Buyers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amazon.Prevalent.Prevalent;
import com.example.amazon.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

        private CircleImageView imgSettingProfile;
        private TextView tvCloseSetting,tvUpdateSetting ,tvChangeImgProfile;
        private EditText txtPhoneNumberSetting , txtFullNameSetting , txtAddressSetting;
        private ProgressDialog loadingBar;
        private Button btnSecurityQuestion;
        private Uri imageUri;
        private  String  myUrl = "";
        private UploadTask uploadTask;
        private StorageReference storagePicProfileRef;
        private String checker = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        imgSettingProfile = findViewById(R.id.imgSettingProfile);
        tvCloseSetting = findViewById(R.id.tvCloseSetting);
        tvUpdateSetting = findViewById(R.id.tvUpdateSetting);
        tvChangeImgProfile = findViewById(R.id.tvChangeImgProfile);
        txtPhoneNumberSetting = findViewById(R.id.txtPhoneNumberSetting);
        txtFullNameSetting = findViewById(R.id.txtFullNameSetting);
        txtAddressSetting = findViewById(R.id.txtAddressSetting);
        btnSecurityQuestion = findViewById(R.id.btnSecurityQuestion);
    storagePicProfileRef = FirebaseStorage.getInstance().getReference().child("Profile Picture");
        DisplayUserInfo(imgSettingProfile , txtPhoneNumberSetting , txtFullNameSetting , txtAddressSetting);

        btnSecurityQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingsActivity.this , ResstPasswordActivity.class);
                intent.putExtra("check" , "settings");
                startActivity(intent);
            }
        });
        tvCloseSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvUpdateSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checker.equals("clicked")){

                    userInfoSaved();
                }
                else{

                    updateOnlyUserInfo();
                }
            }
        });

        tvChangeImgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1 , 1)
                        .start(SettingsActivity.this);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            imgSettingProfile.setImageURI(imageUri);
        }
        else{
            Toast.makeText(this, "Error , Try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this , SettingsActivity.class));
            finish();
        }


    }

    private void userInfoSaved() {

        if( TextUtils.isEmpty(txtFullNameSetting.getText().toString())){
            Toast.makeText(this, "Full name is Mandatory .", Toast.LENGTH_SHORT).show();
        }

        else  if( TextUtils.isEmpty(txtPhoneNumberSetting.getText().toString())){
            Toast.makeText(this, "Phone number is Mandatory .", Toast.LENGTH_SHORT).show();
        }

        else  if( TextUtils.isEmpty(txtAddressSetting.getText().toString())){
            Toast.makeText(this, "Address is Mandatory .", Toast.LENGTH_SHORT).show();
        }

        else if(checker.equals("clicked")){
            uploadImage();
        }


    }

    private void uploadImage() {

        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Upadte Profile");
        loadingBar.setMessage("Please Wait , while we are updating account information ... ");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        if(imageUri != null){
            final StorageReference fileRef = storagePicProfileRef
                    .child(Prevalent.currentOnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public  Object then(@NonNull Task task) throws Exception
                {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if ( task.isSuccessful() ){
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String , Object> userMap = new HashMap<>();
                        userMap.put("name" , txtFullNameSetting.getText().toString());
                        userMap.put("phone" , txtPhoneNumberSetting.getText().toString());
                        userMap.put("address" , txtAddressSetting.getText().toString());
                        userMap.put("image" , myUrl);


                        Ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                      loadingBar.dismiss();
                        startActivity(new Intent(SettingsActivity.this , HomeActivity.class));
                        Toast.makeText(SettingsActivity.this, "Profile info update successful .", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                       loadingBar.dismiss();
                        Toast.makeText(SettingsActivity.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Image is not selected .", Toast.LENGTH_SHORT).show();
        }
    }

    private void DisplayUserInfo(final CircleImageView imgSettingProfile, final EditText txtPhoneNumberSetting, final EditText txtFullNameSetting, final EditText txtAddressSetting) {

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("image").exists()){

                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String phone = dataSnapshot.child("phone").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(imgSettingProfile);
                        txtFullNameSetting.setText(name);
                        txtPhoneNumberSetting.setText(phone);
                        txtAddressSetting.setText(address);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateOnlyUserInfo() {
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String , Object> userMap = new HashMap<>();
        userMap.put("name" , txtFullNameSetting.getText().toString());
        userMap.put("phone" , txtPhoneNumberSetting.getText().toString());
        userMap.put("address" , txtAddressSetting.getText().toString());


       Ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this , HomeActivity.class));
        Toast.makeText(SettingsActivity.this, "Profile info update successful .", Toast.LENGTH_SHORT).show();
        finish();
    }


}
