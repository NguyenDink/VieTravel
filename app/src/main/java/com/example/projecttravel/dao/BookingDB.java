package com.example.projecttravel.dao;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projecttravel.model.Booking;
import com.example.projecttravel.model.Hotel;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookingDB {
    private DatabaseReference myRef;
    public BookingDB() {
        myRef = FirebaseDatabase.getInstance().getReference("Booking");
    }

    public void addBooking(Context context, Booking booking, int booking_id) {
        myRef.child(booking_id+"").setValue(booking, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(context, "Đặt phòng thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
