package com.example.projecttravel.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.projecttravel.R;
import com.example.projecttravel.adapter.LocationAdapter;
import com.example.projecttravel.model.Location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListAllLocation extends AppCompatActivity {

    private ImageView btnBack;
    private List<Location> listLocation;
    private LocationAdapter locationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_all_location);

        initUI();
        getListLocation();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void initUI() {
        listLocation = new ArrayList<>();
        RecyclerView rViewLocation = findViewById(R.id.rView_listLocation);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rViewLocation.setLayoutManager(linearLayoutManager);

        locationAdapter = new LocationAdapter(listLocation, R.layout.viewholder_location_in);

        rViewLocation.setAdapter(locationAdapter);

        btnBack = findViewById(R.id.btnBack);
    }
    public void getListLocation() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Location");
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listLocation != null)
                    listLocation.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Location location = dataSnapshot.getValue(Location.class);
                    listLocation.add(location);
                }
                locationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListAllLocation.this, "Không thể tải danh sách địa điểm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}