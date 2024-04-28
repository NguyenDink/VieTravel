package com.example.projecttravel.dao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projecttravel.model.Location;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FavoritesDB {
    private DatabaseReference myRef;
    FirebaseUser currentUser;
    private String account_id;

    public FavoritesDB() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            account_id = currentUser.getUid();
            myRef = FirebaseDatabase.getInstance().getReference("Favorites/" + account_id);
        }
    }

    public void addFavorite(String location_id) {
        if (currentUser != null) {
            myRef.child(location_id).setValue(true);
        }
    }

    public void deleteFavorite(String location_id) {
        if (currentUser != null) {
            myRef.child(location_id).removeValue();
        }
    }

    public void checkFavorite(final String location_id, final CheckFavoriteCallback callback) {
        if (currentUser != null) {
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(location_id)) {
                        callback.onCallback(true);
                    } else {
                        callback.onCallback(false);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý khi có lỗi xảy ra
                }
            });
        }
    }

    public interface CheckFavoriteCallback {
        void onCallback(boolean isFavorite);
    }
}

