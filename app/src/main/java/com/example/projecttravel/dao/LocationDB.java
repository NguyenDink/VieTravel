package com.example.projecttravel.dao;

import androidx.annotation.NonNull;

import com.example.projecttravel.model.Location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LocationDB {
    private DatabaseReference myRef;
    FavoritesDB favoritesDB = new FavoritesDB();
    public LocationDB() {
        myRef = FirebaseDatabase.getInstance().getReference("Location");
    }

    public void getLocationById(int locationId, final LocationCallback callback) {
        myRef.child(locationId+"").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Location location = dataSnapshot.getValue(Location.class);
                callback.onLocationRetrieved(location);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }

    // Interface để truyền đối tượng địa điểm đã được tìm thấy
    public interface LocationCallback {
        void onLocationRetrieved(Location location);
    }

    public void searchLocation(String keyword, LocationsCallback callback) {
        List<Location> searchList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Location location = dataSnapshot.getValue(Location.class);
                    if (location!=null) {
                        String normalizedKeyword = normalizeText(keyword);
                        String normalizedLocationName = normalizeText(location.getName());
                        String normalizedAddress = normalizeText(location.getAddress());
                        if (normalizedLocationName.contains(normalizedKeyword) || normalizedAddress.contains(normalizedKeyword)) {
                            searchList.add(location);
                        }
                    }
                }
                callback.onLocationsRetrieved(searchList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String normalizeText(String text) {
        // Chuyển đổi về chữ thường và loại bỏ dấu
        return Normalizer.normalize(text.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
    }

    public void getFavoriteLocations(final FavoriteLocationsCallback callback) {
        final List<Location> favoriteLocations = new ArrayList<>();
        final AtomicInteger count = new AtomicInteger(0);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Location location = snapshot.getValue(Location.class);
                    final String locationId = String.valueOf(location.getLocation_id());
                    favoritesDB.checkFavorite(locationId, new FavoritesDB.CheckFavoriteCallback() {
                        @Override
                        public void onCallback(boolean isFavorite) {
                            if (isFavorite) {
                                favoriteLocations.add(location);
                            }
                            count.incrementAndGet();
                            if (count.get() == dataSnapshot.getChildrenCount()) {
                                callback.onFavoriteLocationsRetrieved(favoriteLocations);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });
    }

    // Interface để truyền danh sách các địa điểm được yêu thích
    public interface FavoriteLocationsCallback {
        void onFavoriteLocationsRetrieved(List<Location> favoriteLocations);
    }

    public interface LocationsCallback {
        void onLocationsRetrieved(List<Location> locations);
    }
}



