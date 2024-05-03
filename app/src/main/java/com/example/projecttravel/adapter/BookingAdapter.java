package com.example.projecttravel.adapter;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projecttravel.R;
import com.example.projecttravel.model.Booking;
import com.example.projecttravel.model.Hotel;
import com.example.projecttravel.model.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingAdapter extends ArrayAdapter<Booking> {
    Activity context;
    int layoutId;
    ArrayList<Booking> arrBooking;


    public BookingAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<Booking> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutId = resource;
        this.arrBooking = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = context.getLayoutInflater().inflate(layoutId, null);
        TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
        TextView txtTime = (TextView) convertView.findViewById(R.id.txtTime);
        TextView txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
        Button btnHuy = (Button) convertView.findViewById(R.id.btnHuy);
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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_detail_booking, null);
                TextView txtBookingId = dialogView.findViewById(R.id.txtBookingId);
                TextView txtAccountName = dialogView.findViewById(R.id.txtAccountName);
                TextView txtHotelName = dialogView.findViewById(R.id.txtHotelName);
                TextView txtAddress = dialogView.findViewById(R.id.txtAddress);
                TextView txtCheckIn = dialogView.findViewById(R.id.txtCheckIn);
                TextView txtCheckOut = dialogView.findViewById(R.id.txtCheckOut);
                TextView txtPrice = dialogView.findViewById(R.id.txtPrice);
                TextView txtTotal = dialogView.findViewById(R.id.txtTotal);
                TextView txtStatus = dialogView.findViewById(R.id.txtStatus);
                Button btnBack = dialogView.findViewById(R.id.btnBack);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();

                txtBookingId.setText("Mã đơn:" + booking.getBooking_id());
                txtAccountName.setText("Khách hàng: " + booking.getAccount().getLastName() + booking.getAccount().getFirstName());
                if (booking.getHotel().getCategory_id() == 1)
                    txtHotelName.setText("Khách sạn: " + booking.getHotel().getName());
                else
                    txtHotelName.setText("Homestay: " + booking.getHotel().getName());
                txtAddress.setText("Địa chỉ: " + booking.getHotel().getAddress());
                txtCheckIn.setText("Ngày nhận phòng: " + booking.getCheckIn());
                txtCheckOut.setText("Ngày trả phòng: " + booking.getCheckOut());
                txtPrice.setText("Giá/Đêm: " + booking.getHotel().getPrice() + " VNĐ");
                txtTotal.setText("Tổng: " + booking.getPrice() + "VNĐ");
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

                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        return convertView;
    }
}
