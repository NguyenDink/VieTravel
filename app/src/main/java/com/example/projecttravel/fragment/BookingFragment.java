package com.example.projecttravel.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projecttravel.R;
import com.example.projecttravel.adapter.BookingAdapter;
import com.example.projecttravel.dao.BookingDB;
import com.example.projecttravel.model.Booking;
import com.example.projecttravel.model.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {
    private View mView;
    private ListView lvBooking;
    private ArrayList<Booking> arrBooking;
    private BookingAdapter bookingAdapter = null;
    private ProgressDialog progressDialog;
    private ImageButton btnReload;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_booking, container, false);
        init();
        progressDialog = new ProgressDialog(getActivity());
        getListBooking();
        initListener();
        return mView;
    }

    public void initListener() {
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListBooking();
            }
        });
    }
    public void init() {
        btnReload = mView.findViewById(R.id.btnReload);
        lvBooking = mView.findViewById(R.id.lvBooking);
        arrBooking = new ArrayList<>();
        bookingAdapter = new BookingAdapter(requireActivity(), R.layout.viewholder_booking, arrBooking);
        lvBooking.setAdapter(bookingAdapter);
    }

    public void getListBooking() {
        progressDialog.show();
        BookingDB bookingDB = new BookingDB();
        bookingDB.getListBookingCurrentUser(new BookingDB.ListBookingCallback() {
            @Override
            public void onListBookingRetrieved(List<Booking> booking) {
                arrBooking.clear();
                arrBooking.addAll(booking);
                bookingAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        });
    }

}
