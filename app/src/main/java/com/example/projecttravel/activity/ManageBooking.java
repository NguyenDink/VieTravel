package com.example.projecttravel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.projecttravel.R;
import com.example.projecttravel.adapter.BookingManagementAdapter;
import com.example.projecttravel.dao.BookingDB;
import com.example.projecttravel.model.Booking;

import java.util.ArrayList;
import java.util.List;

public class ManageBooking extends AppCompatActivity {
    private ImageView btnBack;
    private ListView lvProcessing, lvProcessed;
    private ArrayList<Booking> arrProcessing, arrProcessed;
    private BookingManagementAdapter processingAdapter = null;
    private BookingManagementAdapter processedAdapter = null;
    private TabHost bookingTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_booking);
        initUI();
        getListProcessing();
        getListProcessed();
        initListener();
    }

    public void initUI() {
        btnBack = findViewById(R.id.btnBack);
        lvProcessing = findViewById(R.id.lvProcessing);
        lvProcessed = findViewById(R.id.lvProcessed);

        arrProcessing = new ArrayList<>();
        arrProcessed = new ArrayList<>();
        processingAdapter = new BookingManagementAdapter(this, R.layout.viewholder_manage_booking, arrProcessing);
        processedAdapter = new BookingManagementAdapter(this, R.layout.viewholder_manage_booking, arrProcessed);
        lvProcessing.setAdapter(processingAdapter);
        lvProcessed.setAdapter(processedAdapter);

        bookingTab = findViewById(R.id.bookingTap);
        bookingTab.setup();
        TabHost.TabSpec spec1, spec2;
        spec1 = bookingTab.newTabSpec("t1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Chờ xác nhận");
        bookingTab.addTab(spec1);
        spec2 = bookingTab.newTabSpec("t2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Đã xác nhận");
        bookingTab.addTab(spec2);
    }

    public void initListener() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getListProcessing() {
        BookingDB bookingDB = new BookingDB();
        bookingDB.getListBookingOwnerProcessing(new BookingDB.ListBookingCallback() {
            @Override
            public void onListBookingRetrieved(List<Booking> booking) {
                arrProcessing.clear();
                arrProcessing.addAll(booking);
                processingAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getListProcessed() {
        BookingDB bookingDB = new BookingDB();
        bookingDB.getListBookingOwnerProcessed(new BookingDB.ListBookingCallback() {
            @Override
            public void onListBookingRetrieved(List<Booking> booking) {
                arrProcessed.clear();
                arrProcessed.addAll(booking);
                processedAdapter.notifyDataSetChanged();
            }
        });
    }
}