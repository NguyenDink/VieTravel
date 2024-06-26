package com.example.projecttravel.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.projecttravel.fragment.AccountFragment;
import com.example.projecttravel.fragment.BookingFragment;
import com.example.projecttravel.fragment.FavoriteFragment;
import com.example.projecttravel.fragment.HomeFragment;
import com.example.projecttravel.fragment.ScheduleFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new FavoriteFragment();
//            case 2:
//                return new ScheduleFragment();
            case 2:
                return new BookingFragment();
            case 3:
                return new AccountFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
