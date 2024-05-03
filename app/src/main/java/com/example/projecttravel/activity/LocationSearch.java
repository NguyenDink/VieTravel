package com.example.projecttravel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.projecttravel.R;
import com.example.projecttravel.adapter.LocationAdapter;
import com.example.projecttravel.dao.LocationDB;
import com.example.projecttravel.model.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationSearch extends AppCompatActivity {

    private ImageView btnBack;
    private List<Location> listLocation;
    private LocationAdapter locationAdapter;
    private RecyclerView rViewLocation;
    private EditText edtFindLocation;
    private ImageView btnSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        initUI();
        setVariable();
        initListener();
    }

    public void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edtFindLocation.getText().toString().trim();
                LocationDB locationDB = new LocationDB();
                locationDB.searchLocation(text, new LocationDB.LocationsCallback() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onLocationsRetrieved(List<Location> locations) {
                        listLocation.clear();
                        listLocation.addAll(locations);
                        locationAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
    public void initUI() {
        btnSearch = findViewById(R.id.btnSearch);
        btnBack = findViewById(R.id.btnBack);
        edtFindLocation = findViewById(R.id.edtFindLocation);
        rViewLocation = findViewById(R.id.rView_listLocation);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rViewLocation.setLayoutManager(linearLayoutManager);
        listLocation = new ArrayList<>();
        locationAdapter = new LocationAdapter(listLocation, R.layout.viewholder_location_in);

        rViewLocation.setAdapter(locationAdapter);
    }

    public void setVariable() {
        String text = (String) getIntent().getSerializableExtra("object");
        edtFindLocation.setText(text);
        LocationDB locationDB = new LocationDB();
        locationDB.searchLocation(text, new LocationDB.LocationsCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onLocationsRetrieved(List<Location> locations) {
                listLocation.addAll(locations);
                locationAdapter.notifyDataSetChanged();
            }
        });
    }
}