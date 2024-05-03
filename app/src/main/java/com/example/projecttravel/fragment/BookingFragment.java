package com.example.projecttravel.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projecttravel.R;
import com.example.projecttravel.adapter.BookingAdapter;
import com.example.projecttravel.dao.BookingDB;
import com.example.projecttravel.model.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {
    private View mView;
    private ListView lvBooking;
    private ArrayList<Booking> arrBooking;
    private BookingAdapter bookingAdapter = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_booking, container, false);
        init();
        getListBooking();
        return mView;
    }

    public void init() {
        lvBooking = mView.findViewById(R.id.lvBooking);
        arrBooking = new ArrayList<>();
        bookingAdapter = new BookingAdapter(requireActivity(), R.layout.viewholder_booking, arrBooking);
        lvBooking.setAdapter(bookingAdapter);
    }

    public void getListBooking() {
        BookingDB bookingDB = new BookingDB();
        bookingDB.getListBookingCurrentUser(new BookingDB.ListBookingCallback() {
            @Override
            public void onListBookingRetrieved(List<Booking> booking) {
                arrBooking.addAll(booking);
                bookingAdapter.notifyDataSetChanged();
                if (booking.isEmpty())
                    Toast.makeText(getContext(), "NULL", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
