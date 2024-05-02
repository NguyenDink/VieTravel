package com.example.projecttravel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.projecttravel.R;
import com.example.projecttravel.adapter.HotelAdapter;
import com.example.projecttravel.dao.FavoritesDB;
import com.example.projecttravel.dao.HotelDB;
import com.example.projecttravel.model.Hotel;
import com.example.projecttravel.model.Location;

import java.util.ArrayList;
import java.util.List;

public class DetailLocation extends AppCompatActivity {
    private TextView txtName, txtTitle, txtDescription, txtAddress;
    private ImageView imgBack, imgLocation, imgLike;
    private Location location;
    private String location_id;
    private int location_Id;
    FavoritesDB favoritesDB = new FavoritesDB();

    private ListView lvHotel;
    private ArrayList<Hotel> arrHotel;
    private HotelAdapter hotelAdapter = null;
    HotelDB hotelDB = new HotelDB();

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
        lvHotel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DetailLocation.this, DetailHotel.class);
                Hotel hotel = arrHotel.get(position);
                intent.putExtra("object", hotel);
                startActivity(intent);
            }
        });
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

        location_Id = location.getLocation_id();
        location_id = String.valueOf(location.getLocation_id());
        txtName.setText(location.getName());
        txtTitle.setText(location.getTitle());
        txtAddress.setText(location.getAddress());
        txtDescription.setText(location.getDescription());
        Glide.with(this)
                .load(location.getImages())
                .error(R.drawable.ic_launcher_background)
                .into(imgLocation);

        arrHotel = new ArrayList<>();
        hotelAdapter = new HotelAdapter(this, R.layout.viewholder_hotel, arrHotel);
        lvHotel.setAdapter(hotelAdapter);

        getListHotel();
    }

    private void intUI() {
        txtName = findViewById(R.id.txtName);
        txtTitle = findViewById(R.id.txtTitle);
        txtAddress = findViewById(R.id.txtAddress);
        txtDescription = findViewById(R.id.txtDescription);
        imgBack = findViewById(R.id.imgBack);
        imgLike = findViewById(R.id.imgLike);
        imgLocation = findViewById(R.id.imgLocation);
        lvHotel = findViewById(R.id.lvHotel);
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

    private void getListHotel() {
        hotelDB.getListHotelByLocation(location_Id, new HotelDB.HotelListListener() {
            @Override
            public void onHotelListReceived(List<Hotel> hotelList) {
                if (arrHotel != null)
                    arrHotel.clear();
                arrHotel.addAll(hotelList);
                hotelAdapter.notifyDataSetChanged();
            }
        });
    }
}