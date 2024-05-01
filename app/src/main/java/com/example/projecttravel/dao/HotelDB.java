package com.example.projecttravel.dao;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projecttravel.model.Hotel;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HotelDB {
    private DatabaseReference myRef;
    public HotelDB() {
        myRef = FirebaseDatabase.getInstance().getReference("Hotel");
    }

    public void addHotel(Context context, Hotel hotel, int hotel_id) {
        myRef.child(hotel_id+"").setValue(hotel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Thêm khách sạn thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
