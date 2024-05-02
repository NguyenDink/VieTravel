package com.example.projecttravel.dao;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projecttravel.model.Hotel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void getListHotelByLocation(int location_id, HotelListListener listener) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Hotel> hotelList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Hotel hotel = dataSnapshot.getValue(Hotel.class);

                    if (hotel!=null && hotel.getLocation_id()==location_id) {
                        hotelList.add(hotel);
                    }
                }
                listener.onHotelListReceived(hotelList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getListHotelByOwner(HotelListListener listener) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String account_id = currentUser.getUid();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Hotel> hotelList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Hotel hotel = dataSnapshot.getValue(Hotel.class);

                    if (hotel!=null && hotel.getOwner_id().equals(account_id)) {
                        hotelList.add(hotel);
                    }
                }
                listener.onHotelListReceived(hotelList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface HotelListListener {
        void onHotelListReceived(List<Hotel> hotelList);
    }

    public void deleteHotelById(int hotelId, Context context, DeleteHotelListener listener) {
        DatabaseReference hotelRef = myRef.child(hotelId+"");
        hotelRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(context, "Xóa khách sạn thất bại: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    // Xóa thành công
                    Toast.makeText(context, "Xóa khách sạn thành công!", Toast.LENGTH_SHORT).show();
                    // Gọi phương thức callback để thông báo rằng việc xóa đã hoàn thành
                    listener.onHotelDeleted();
                }
            }
        });
    }

    // Định nghĩa một giao diện để lắng nghe sự kiện khi khách sạn được xóa
    public interface DeleteHotelListener {
        void onHotelDeleted();
    }

    public void updateHotelById(int hotelId, int categoryId, int capacity, String address, double price, String description, Context context, UpdateHotelListener listener) {
        DatabaseReference hotelRef = myRef.child(hotelId+"");
        // Tạo một HashMap để lưu các giá trị cần cập nhật
        Map<String, Object> updateValues = new HashMap<>();
        updateValues.put("category_id", categoryId);
        updateValues.put("capacity", capacity);
        updateValues.put("address", address);
        updateValues.put("price", price);
        updateValues.put("description", description);

        // Thực hiện cập nhật dữ liệu của khách sạn
        hotelRef.updateChildren(updateValues, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(context, "Cập nhật thông tin khách sạn thất bại: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    // Cập nhật thành công
                    Toast.makeText(context, "Cập nhật thông tin khách sạn thành công!", Toast.LENGTH_SHORT).show();
                    // Gọi phương thức callback để thông báo rằng việc cập nhật đã hoàn thành
                    listener.onHotelUpdated();
                }
            }
        });
    }

    // Định nghĩa một giao diện để lắng nghe sự kiện khi thông tin của khách sạn được cập nhật
    public interface UpdateHotelListener {
        void onHotelUpdated();
    }
}
