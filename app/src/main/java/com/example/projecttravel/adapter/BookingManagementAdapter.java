package com.example.projecttravel.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.projecttravel.R;
import com.example.projecttravel.model.Booking;
import com.example.projecttravel.model.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingManagementAdapter extends ArrayAdapter<Booking> {
    Activity context;
    int layoutId;
    ArrayList<Booking> arrBooking;
    private ArrayAdapter<String> adapterStatus = null;

    public BookingManagementAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<Booking> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutId = resource;
        this.arrBooking = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = context.getLayoutInflater().inflate(layoutId, null);
        TextView txtBookingId = (TextView) convertView.findViewById(R.id.txtBookingId);
        TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
        TextView txtKHName = (TextView) convertView.findViewById(R.id.txtKHName);
        TextView txtTime = (TextView) convertView.findViewById(R.id.txtTime);
        TextView txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
        Button btnCapNhat = (Button) convertView.findViewById(R.id.btnCapNhat);
        Spinner spinStatus = (Spinner) convertView.findViewById(R.id.spinStatus);
        Booking booking = arrBooking.get(position);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Status");
        myRef.child("" + booking.getStatus_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Status status = snapshot.getValue(Status.class);
                txtStatus.setText("Tình trạng: " + status.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (booking.getHotel().getCategory_id() == 1)
            txtName.setText("Khách sạn: " + booking.getHotel().getName().toString().trim());
        else
            txtName.setText("Homestay: " + booking.getHotel().getName().toString().trim());
        txtTime.setText("Nhận phòng: " + booking.getCheckIn() + " - Trả phòng: " + booking.getCheckOut());
        txtKHName.setText("Khách hàng: " + booking.getAccount().getLastName() + " " + booking.getAccount().getFirstName());
        txtBookingId.setText("Mã: " + booking.getBooking_id());

        if (booking.getStatus_id() == 1) {
            String[] status = {"Hủy", "Xác nhận"};
            adapterStatus = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, status);
        } else if (booking.getStatus_id() == 2) {
            String[] status = {"Đã Hủy"};
            adapterStatus = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, status);
        } else {
            String[] status = {"Đã nhận phòng", "Không nhận phòng"};
            adapterStatus = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, status);
        }
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinStatus.setAdapter(adapterStatus);

        if (booking.getStatus_id() == 2) {
            btnCapNhat.setEnabled(false);
            btnCapNhat.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_btn));
            btnCapNhat.setBackgroundTintList(null);
            btnCapNhat.setTextColor(ContextCompat.getColor(context, R.color.gray));
        } else if (booking.getStatus_id() == 1) {
            btnCapNhat.setEnabled(true);
            btnCapNhat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int status_id;
                    if (spinStatus.getSelectedItemPosition() == 0)
                        status_id = 2;
                    else
                        status_id = 3;
                    DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("Booking").child(String.valueOf(booking.getBooking_id()));
                    bookingRef.child("status_id").setValue(status_id)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Cập nhật thành công
                                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Xử lý khi cập nhật thất bại
                                    Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        } else {
            btnCapNhat.setEnabled(true);
            btnCapNhat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int status_id;
                    if (spinStatus.getSelectedItemPosition() == 0)
                        status_id = 4;
                    else
                        status_id = 5;
                    DatabaseReference bookingRef = FirebaseDatabase.getInstance().getReference("Booking").child(String.valueOf(booking.getBooking_id()));
                    bookingRef.child("status_id").setValue(status_id)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Cập nhật thành công
                                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Xử lý khi cập nhật thất bại
                                    Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }

        return convertView;
    }
}
