package com.example.projecttravel.dao;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projecttravel.model.Booking;
import com.example.projecttravel.model.Hotel;
import com.example.projecttravel.model.Location;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    public void getListBookingCurrentUser(ListBookingCallback callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String account_id = currentUser.getUid();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Booking> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Booking booking = dataSnapshot.getValue(Booking.class);

                    if ((booking.getAccount().getAccount_id().toString().trim()).equals(account_id)) {
                        list.add(booking);
                    }
                }

                Collections.sort(list, new Comparator<Booking>() {
                    @Override
                    public int compare(Booking booking1, Booking booking2) {
                        return Integer.compare(booking2.getBooking_id(), booking1.getBooking_id());
                    }
                });
                callback.onListBookingRetrieved(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public interface ListBookingCallback {
        void onListBookingRetrieved(List<Booking> booking);
    }
}
