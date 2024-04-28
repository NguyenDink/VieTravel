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
import com.example.projecttravel.dao.FavoritesDB;
import com.example.projecttravel.model.Location;

public class DetailLocation extends AppCompatActivity {
    private TextView txtName, txtTitle, txtDescription, txtAddress;
    private ImageView imgBack, imgLocation, imgLike;
    private Location location;
    private String location_id;
    FavoritesDB favoritesDB = new FavoritesDB();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_location);
        intUI();
        setVariable();
        checkLike();
        initListener();
    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoritesDB.checkFavorite(location_id, new FavoritesDB.CheckFavoriteCallback() {
                    @Override
                    public void onCallback(boolean isFavorite) {
                        if (isFavorite) {
                            favoritesDB.deleteFavorite(location_id);
                            imgLike.setImageResource(R.drawable.ic_favorite_do);
                        } else {
                            favoritesDB.addFavorite(location_id);
                            imgLike.setImageResource(R.drawable.ic_favorite_did);
                        }
                    }
                });
            }
        });
    }

    private void setVariable() {
        location = (Location) getIntent().getSerializableExtra("object");

        location_id = String.valueOf(location.getLocation_id());
        txtName.setText(location.getName());
        txtTitle.setText(location.getTitle());
        txtAddress.setText(location.getAddress());
        txtDescription.setText(location.getDescription());
        Glide.with(this)
                .load(location.getImages())
                .error(R.drawable.ic_launcher_background)
                .into(imgLocation);
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

    private void checkLike() {
        favoritesDB.checkFavorite(location_id, new FavoritesDB.CheckFavoriteCallback() {
            @Override
            public void onCallback(boolean isFavorite) {
                if (isFavorite) {
                    imgLike.setImageResource(R.drawable.ic_favorite_did);
                } else {
                    imgLike.setImageResource(R.drawable.ic_favorite_do);
                }
            }
        });
    }
}