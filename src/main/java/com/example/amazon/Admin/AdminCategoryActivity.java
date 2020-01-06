package com.example.amazon.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.amazon.Buyers.HomeActivity;
import com.example.amazon.Buyers.MainActivity;
import com.example.amazon.R;

public class AdminCategoryActivity extends AppCompatActivity {
    private ImageView t_shirt , shirt_sport , jacket ,dress;
    private  ImageView glass , bag , hat ,shoes;
    private ImageView headphone , laptop , watch , mobile;
    private Button btnAdminLogOut , btnCheckNewOrders , btnMaintainProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        btnAdminLogOut = findViewById(R.id.btnAdminLogOut);
        btnCheckNewOrders = findViewById(R.id.btnCheckNewOrders);
        btnMaintainProducts = findViewById(R.id.btnMaintainProducts);
        t_shirt = findViewById(R.id.t_shirt);
        shirt_sport = findViewById(R.id.shirt_sport);
        jacket = findViewById(R.id.jacket);
        dress = findViewById(R.id.dress);
        glass = findViewById(R.id.glass);
        bag = findViewById(R.id.bag);
        hat = findViewById(R.id.hat);
        shoes = findViewById(R.id.shoes);
        laptop = findViewById(R.id.laptop);
        watch = findViewById(R.id.watch);
        mobile = findViewById(R.id.mobile);

        btnMaintainProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this , HomeActivity.class);
                intent.putExtra("Admin" , "Admin");
                startActivity(intent);
            }
        });
        btnAdminLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this , MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        btnCheckNewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminCategoryActivity.this , AdminNewOrdersActivity.class);

                startActivity(intent);
            }
        });

        t_shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category" , "t_shirt");
                startActivity(intent);
            }

        });

        shirt_sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category" , "shirt_sport");
                startActivity(intent);
            }

        });

        jacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category" , "jacket");
                startActivity(intent);
            }

        });

        dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category" , "dress");
                startActivity(intent);
            }

        });

        glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category" , "glass");
                startActivity(intent);
            }

        });
        bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category" , "bag");
                startActivity(intent);
            }

        });
        hat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category" , "hat");
                startActivity(intent);
            }

        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category" , "shoes");
                startActivity(intent);
            }

        });

        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category" , "laptop");
                startActivity(intent);
            }

        });

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category" , "watch");
                startActivity(intent);
            }

        });

        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this , AdminAddNewProductActivity.class);
                intent.putExtra("category" , "mobile");
                startActivity(intent);
            }

        });

    }
}
