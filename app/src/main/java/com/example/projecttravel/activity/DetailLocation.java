package com.example.projecttravel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.projecttravel.R;
import com.example.projecttravel.model.Location;

public class DetailLocation extends AppCompatActivity {
    private TextView txtName, txtTitle, txtDescription, txtAddress;
    private ImageView imgBack, imgLocation, imgLike;
    private Location location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_location);
        intUI();
        setVariable();
    }

    private void setVariable() {
        location = (Location) getIntent().getSerializableExtra("object");

        txtName.setText(location.getName());
        txtTitle.setText(location.getTitle());
        txtAddress.setText(location.getAddress());
        txtDescription.setText(location.getDescription());
        Glide.with(this)
                .load(location.getImages())
                .error(R.drawable.ic_launcher_background)
                .into(imgLocation);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void intUI() {
        txtName = findViewById(R.id.txtName);
        txtTitle = findViewById(R.id.txtTitle);
        txtAddress = findViewById(R.id.txtAddress);
        txtDescription = findViewById(R.id.txtDescription);
        imgBack = findViewById(R.id.imgBack);
        imgLike = findViewById(R.id.imgLike);
        imgLocation = findViewById(R.id.imgLocation);
    }
}