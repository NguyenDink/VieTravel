package com.example.projecttravel.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;

import com.example.projecttravel.R;
import com.example.projecttravel.adapter.ViewPagerAdapter;
import com.example.projecttravel.fragment.AccountFragment;
import com.example.projecttravel.transformer.DepthPageTransformer;
import com.example.projecttravel.transformer.ZoomOutPageTransformer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Home extends AppCompatActivity {

    public static final int MY_REQUEST_CODE = 1;

    private ViewPager2 mViewPager2;
    private BottomNavigationView mBottomNavigationView;
    final private AccountFragment myAccountFragment = new AccountFragment();

    final private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent == null) {
                    return;
                }
                Uri uri = intent.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    myAccountFragment.setBitmapImageView(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mViewPager2 = findViewById(R.id.view_pager_2);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(this);
        mViewPager2.setAdapter(mViewPagerAdapter);

        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id==R.id.bottom_home) {
                    mViewPager2.setCurrentItem(0);
                } else if (id==R.id.bottom_favorite) {
                    mViewPager2.setCurrentItem(1);
                } else if (id==R.id.bottom_schedule) {
                    mViewPager2.setCurrentItem(2);
                } else if (id==R.id.bottom_booking) {
                    mViewPager2.setCurrentItem(3);
                } else if (id==R.id.bottom_account) {
                    mViewPager2.setCurrentItem(4);
                }
                return true;
            }
        });

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_home).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_favorite).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_schedule).setChecked(true);
                        break;
                    case 3:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_booking).setChecked(true);
                        break;
                    case 4:
                        mBottomNavigationView.getMenu().findItem(R.id.bottom_account).setChecked(true);
                        break;
                }
            }
        });

        mViewPager2.setPageTransformer(new ZoomOutPageTransformer());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE)
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Chọn ảnh"));
    }
}