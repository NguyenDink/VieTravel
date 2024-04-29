package com.example.projecttravel.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projecttravel.R;
import com.example.projecttravel.adapter.LocationAdapter;
import com.example.projecttravel.dao.FavoritesDB;
import com.example.projecttravel.dao.LocationDB;
import com.example.projecttravel.model.Location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private View mView;
    private ImageView btnReload;
    private RecyclerView rViewFavorites;
    private LocationAdapter locationAdapter;
    private List<Location> listLocation;
    LocationDB locationDB = new LocationDB();
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_favorite, container, false);
        initUI();
        progressDialog = new ProgressDialog(getActivity());
        getListLocation();
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListLocation();
            }
        });
        return mView;
    }

    private void getListLocation() {
        progressDialog.show();
        locationDB.getFavoriteLocations(new LocationDB.FavoriteLocationsCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onFavoriteLocationsRetrieved(List<Location> favoriteLocations) {
                listLocation.clear();
                listLocation.addAll(favoriteLocations);
                locationAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });
    }


    private void initUI() {
        rViewFavorites = mView.findViewById(R.id.rView_Favorites);
        btnReload = mView.findViewById(R.id.btnReload);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rViewFavorites.setLayoutManager(linearLayoutManager);
        listLocation = new ArrayList<>();
        locationAdapter = new LocationAdapter(listLocation, R.layout.viewholder_location_in);
        rViewFavorites.setAdapter(locationAdapter);
    }

}
