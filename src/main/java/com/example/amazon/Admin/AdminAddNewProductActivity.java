package com.example.amazon.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.amazon.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class AdminAddNewProductActivity extends AppCompatActivity {

    private  String CategoryName , Description , Price , ProductName , saveCurrentDate , saveCurrentTime;
    private Button btnAddProduct;
    private ImageView imgSelectProduct;
    private EditText txtProductName , txtProductDescription , txtProductPrice;
    private  final int galleryPic = 1;
    private Uri imageUri;
    private String productRandomKey , downloadImageUrl;
    private ProgressDialog loadingBar;
    private StorageReference ProductImageRef;
    private DatabaseReference ProductRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        CategoryName = getIntent().getExtras().get("category").toString();
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("products");

        txtProductName = findViewById(R.id.txtProducName);
        txtProductDescription = findViewById(R.id.txtProducDescription);
        txtProductPrice = findViewById(R.id.txtProducPrice);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        imgSelectProduct = findViewById(R.id.imgSelectProduct);
     loadingBar = new ProgressDialog(this);


        imgSelectProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });
    }



    private void openGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent , galleryPic);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == galleryPic && resultCode == RESULT_OK && data!= null){

            imageUri = data.getData();

            imgSelectProduct.setImageURI(imageUri);

        }
    }

    private void validateProductData() {

        Description = txtProductDescription.getText().toString();
        ProductName = txtProductName.getText().toString();
        Price = txtProductPrice.getText().toString();

        if(imageUri == null ){
            Toast.makeText(this, "Product image is mandatory .. ", Toast.LENGTH_SHORT).show();
        }

        else if( TextUtils.isEmpty(Description) ){

            Toast.makeText(this, "Please write product description", Toast.LENGTH_SHORT).show();
        }

        else if( TextUtils.isEmpty(Price) ){

            Toast.makeText(this, "Please write product price", Toast.LENGTH_SHORT).show();
        }

        else if( TextUtils.isEmpty(ProductName) ){

            Toast.makeText(this, "Please write product name", Toast.LENGTH_SHORT).show();
        }

        else{

            storeProductInformation();
        }
    }

    private void storeProductInformation() {
        loadingBar.setTitle("Adding New Product");
        loadingBar.setMessage("Dear Admin , Please Wait while we are adding new product ... ");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd , yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImageRef.child(imageUri.getLastPathSegment() + productRandomKey);

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error : " + message, Toast.LENGTH_SHORT).show();
               loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AdminAddNewProductActivity.this, "Product Image Uploaded Successfully .. ", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()){

                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();

                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        downloadImageUrl = task.getResult().toString();

                        Toast.makeText(AdminAddNewProductActivity.this, " got the Product image URL successfully ..", Toast.LENGTH_SHORT).show();

                        saveProductInfoToDatabase();
                    }
                });
            }
        });

    }

    private void saveProductInfoToDatabase() {

        HashMap<String , Object> productMap = new HashMap<>();
        productMap.put("pid" , productRandomKey);
        productMap.put("date" , saveCurrentDate);
        productMap.put("time" , saveCurrentTime);
        productMap.put("description" , Description);
        productMap.put("image" , downloadImageUrl);
        productMap.put("price" , Price);
        productMap.put("name" , ProductName);
        productMap.put("category" , CategoryName);

        ProductRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Intent intent = new Intent(AdminAddNewProductActivity.this , AdminCategoryActivity.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Product is added successfully .. ", Toast.LENGTH_SHORT).show();
                        }
                        else{

                       loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error : "  + message , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
