package com.example.projecttravel.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecttravel.R;
import com.example.projecttravel.activity.ListAllLocation;
import com.example.projecttravel.activity.LocationSearch;
import com.example.projecttravel.adapter.LocationAdapter;
import com.example.projecttravel.adapter.LocationTypeAdapter;
import com.example.projecttravel.model.Location;
import com.example.projecttravel.model.LocationType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private View mView;
    private TextView txtTatCaDD;
    private EditText edtFindLocation;
    private ImageView btnSearch;
    private RecyclerView rViewLocation, rViewLocationType;
    private LocationAdapter locationAdapter;
    private LocationTypeAdapter locationTypeAdapter;
    private List<Location> listLocation;
    private List<LocationType> listLocationType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        initUI();
        getListLocation();
        getListLocationType();
        initListener();
        return mView;
    }

    private void initListener() {
        txtTatCaDD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListAllLocation.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edtFindLocation.getText().toString().trim();
                Intent intent = new Intent(getContext(), LocationSearch.class);
                intent.putExtra("object", text);
                Toast.makeText(getActivity(), "Kết quả tìm kiếm cho: " + text, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }

    public void initUI() {
        txtTatCaDD = mView.findViewById(R.id.xemTatCaDD);
        btnSearch = mView.findViewById(R.id.btnSearch);
        edtFindLocation = mView.findViewById(R.id.edtFindLocation);


        rViewLocation = mView.findViewById(R.id.rView_location);
        rViewLocationType = mView.findViewById(R.id.rView_LocationType);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rViewLocation.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManagerType = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rViewLocationType.setLayoutManager(linearLayoutManagerType);

        listLocation = new ArrayList<>();
        locationAdapter = new LocationAdapter(listLocation, R.layout.viewholder_location);

        listLocationType = new ArrayList<>();
        locationTypeAdapter = new LocationTypeAdapter(listLocationType);

        rViewLocation.setAdapter(locationAdapter);
        rViewLocationType.setAdapter(locationTypeAdapter);
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
                Toast.makeText(getActivity(), "Không thể tải danh sách địa điểm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getListLocationType() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("LocationType");
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listLocationType != null)
                    listLocationType.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LocationType locationType = dataSnapshot.getValue(LocationType.class);
                    listLocationType.add(locationType);
                }
                locationTypeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Không thể tải danh mục địa điểm", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
