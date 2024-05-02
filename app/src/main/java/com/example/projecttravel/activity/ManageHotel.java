package com.example.projecttravel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.projecttravel.R;
import com.example.projecttravel.adapter.HotelAdapter;
import com.example.projecttravel.dao.HotelDB;
import com.example.projecttravel.model.Hotel;

import java.util.ArrayList;
import java.util.List;

public class ManageHotel extends AppCompatActivity {
    private ImageView btnBack, btnAdd;
    private ListView lvHotel;
    private ArrayList<Hotel> arrHotel;
    private HotelAdapter hotelAdapter = null;
    HotelDB hotelDB = new HotelDB();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_hotel);
        initUI();
        getListHotel();
        initListener();
    }

    private void getListHotel() {
        hotelDB.getListHotelByOwner(new HotelDB.HotelListListener() {
            @Override
            public void onHotelListReceived(List<Hotel> hotelList) {
                if (arrHotel != null)
                    arrHotel.clear();
                arrHotel.addAll(hotelList);
                hotelAdapter.notifyDataSetChanged();
            }
        });
    }

        private void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageHotel.this, AddHotel.class);
                startActivity(intent);
            }
        });

        lvHotel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ManageHotel.this, UpdateHotel.class);
                Hotel hotel = arrHotel.get(position);
                intent.putExtra("object", hotel);
                startActivity(intent);
            }
        });
    }

    private void initUI() {
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);
        lvHotel = findViewById(R.id.lvHotel);
        arrHotel = new ArrayList<>();
        hotelAdapter = new HotelAdapter(this, R.layout.viewholder_hotel, arrHotel);
        lvHotel.setAdapter(hotelAdapter);
    }
}