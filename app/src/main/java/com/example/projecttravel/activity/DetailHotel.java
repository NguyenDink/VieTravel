package com.example.projecttravel.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projecttravel.R;
import com.example.projecttravel.dao.AccountDB;
import com.example.projecttravel.dao.BookingDB;
import com.example.projecttravel.model.Account;
import com.example.projecttravel.model.Booking;
import com.example.projecttravel.model.Hotel;
import com.example.projecttravel.model.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DetailHotel extends AppCompatActivity {

    private ImageView imgHotel, btnBack;
    private TextView txtName, txtAddress, txtType, txtCapacity, txtDescription, txtPrice, txtTotal;
    private Button btnBookNow, btnCheckIn, btnCheckOut;
    private Hotel hotel;
    Calendar checkIn, checkOut;
    double price, totalPrice;
    int booking_id;
    Account account;
    private boolean isCheckInValid = false;
    private boolean isCheckOutValid = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hotel);
        initUI();
        setVariable();
        initListener();
    }

    public void initUI() {
        txtName = findViewById(R.id.txtName);
        txtAddress = findViewById(R.id.txtAddress);
        txtDescription = findViewById(R.id.txtDescription);
        txtType = findViewById(R.id.txtType);
        txtCapacity = findViewById(R.id.txtCapacity);
        txtPrice = findViewById(R.id.txtPrice);
        imgHotel = findViewById(R.id.imgHotel);
        btnBack = findViewById(R.id.btnBack);
        btnBookNow = findViewById(R.id.btnBookNow);
    }

    public void setVariable() {
        hotel = (Hotel) getIntent().getSerializableExtra("object");
        String name = hotel.getName().toString().trim();
        int capacity = hotel.getCapacity();
        String address = hotel.getAddress().toString().trim();
        price = hotel.getPrice();
        String description = hotel.getDescription().toString().trim();
        int category_id = hotel.getCategory_id();
        int hotel_id = hotel.getHotel_id();

        txtName.setText(name);
        txtAddress.setText(address);
        if (category_id == 1)
            txtType.setText("Loại: Khách sạn");
        else
            txtType.setText("Loại: Homestay");

        txtCapacity.setText("Tối đa: " + capacity +" (người)");
        txtPrice.setText("Giá:" + price);
        txtDescription.setText(description);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("hotels").child(hotel_id+".jpg");
        storageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Xử lý khi lấy URL thành công
                    String urlHotel = uri.toString();
                    Glide.with(this).load(urlHotel).error(R.drawable.applogo1).into(imgHotel);
                    // Sử dụng URL của ảnh ở đây
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi không thể lấy URL của ảnh
                    Toast.makeText(DetailHotel.this,"Failed to get image URL from Firebase Storage: " + e.getMessage(),Toast.LENGTH_SHORT).show();
                });
    }

    public void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookHotel();
            }
        });
    }

    public void bookHotel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_booking_hotel, null);
        btnCheckIn = dialogView.findViewById(R.id.btnCheckIn);
        btnCheckOut = dialogView.findViewById(R.id.btnCheckOut);
        Button btnBookNow = dialogView.findViewById(R.id.btnBookNow);
        Button btnBackDialog = dialogView.findViewById(R.id.btnBack);
        txtTotal = dialogView.findViewById(R.id.txtPrice);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
        btnCheckOut.setEnabled(false);

        checkIn = Calendar.getInstance();
        checkOut = Calendar.getInstance();

        btnBookNow.setOnClickListener(new View.OnClickListener() {
            boolean isBookingAdded;
            @Override
            public void onClick(View v) {
                isBookingAdded = false;
                if (isCheckInValid && isCheckOutValid) {
                    DatabaseReference booking_id_ref = FirebaseDatabase.getInstance().getReference("Booking_id");
                    booking_id_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            booking_id = snapshot.getValue(Integer.class);
                            if (!isBookingAdded) {
                                addBooking();
                                dialog.dismiss();
                                isBookingAdded = true;
                                booking_id_ref.setValue(booking_id + 1);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    // Thông báo cho người dùng biết rằng cần chọn ngày checkin và checkout hợp lệ trước khi đặt phòng
                    Toast.makeText(DetailHotel.this, "Vui lòng chọn ngày check-in và check-out hợp lệ trước khi đặt phòng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(checkIn);
            }
        });
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(checkOut);
            }
        });
        btnBackDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void addBooking() {
        AccountDB accountDB = new AccountDB();
        accountDB.getCurrentAccount(new AccountDB.CurrentAccountCallBack() {
            @Override
            public void onCurrentAccount(Account currentAccount) {
                account = currentAccount;
                String checkinDay = btnCheckIn.getText().toString().trim();
                String checkOutDay = btnCheckOut.getText().toString().trim();

                Booking booking = new Booking(booking_id, account, hotel, checkinDay, checkOutDay, totalPrice, 1);
                BookingDB bookingDB = new BookingDB();
                bookingDB.addBooking(DetailHotel.this, booking, booking_id);
            }
        });
    }

    private void showDatePickerDialog(final Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        updateDateButton(calendar);
                        if (calendar.equals(checkIn)) {
                            onCheckInDateSelected(checkIn);
                        } else if (calendar.equals(checkOut)) {
                            onCheckOutDateSelected(checkOut);
                        }
                    }
                }, year, month, dayOfMonth);
        if (calendar.equals(checkIn)) {
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        } else if (calendar.equals(checkOut)) {
            datePickerDialog.getDatePicker().setMinDate(checkIn.getTimeInMillis() + 1000 * 60 * 60 * 24); // Thêm 1 ngày vào ngày checkin
        }
        datePickerDialog.show();
    }

    // Khi người dùng chọn ngày checkin
    private void onCheckInDateSelected(Calendar selectedDate) {
        checkIn = selectedDate;

        // Thiết lập ngày tối thiểu cho ngày checkout là ngày sau ngày checkin
        btnCheckOut.setEnabled(true);
        checkOut.setTimeInMillis(checkIn.getTimeInMillis());
        updateDateButton(checkOut);

        // Tính lại số ngày giữa checkin và checkout
        long diffInMillis = checkOut.getTimeInMillis() - checkIn.getTimeInMillis();
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

        // Tính lại giá tổng
        totalPrice = diffInDays * price;

        // Hiển thị giá tổng mới lên TextView
        txtTotal.setText(String.format(Locale.getDefault(), "%.2f", totalPrice));

        // Cập nhật lại ngày check out
        onCheckOutDateSelected(checkOut);

        // Cập nhật trạng thái hợp lệ của ngày checkin
        isCheckInValid = true;
    }

    // Khi người dùng chọn ngày checkout
    private void onCheckOutDateSelected(Calendar selectedDate) {
        // Kiểm tra xem ngày đã chọn có hợp lệ không (sau ngày checkin)
        if (selectedDate.after(checkIn)) {
            checkOut = selectedDate;
            updateDateButton(checkOut);

            // Tính lại số ngày giữa checkin và checkout
            long diffInMillis = checkOut.getTimeInMillis() - checkIn.getTimeInMillis();
            long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

            // Tính lại giá tổng
            totalPrice = diffInDays * price;

            // Hiển thị giá tổng mới lên TextView
            txtTotal.setText(String.format(Locale.getDefault(), "%.2f", totalPrice));
            // Cập nhật trạng thái hợp lệ của ngày checkout
            isCheckOutValid = true;
        } else {
            // Ngày chọn không hợp lệ, yêu cầu người dùng chọn lại
            Toast.makeText(this, "Ngày check-out phải sau ngày check-in", Toast.LENGTH_SHORT).show();
            showDatePickerDialog(checkOut);
            // Cập nhật trạng thái hợp lệ của ngày checkout
            isCheckOutValid = false;
        }
    }


    private void updateDateButton(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateString = sdf.format(calendar.getTime());
        if (calendar.equals(checkIn)) {
            btnCheckIn.setText(dateString);
        } else if (calendar.equals(checkOut)) {
            btnCheckOut.setText(dateString);
        }
    }

}