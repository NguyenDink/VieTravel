package com.example.projecttravel.fragment;

import static com.example.projecttravel.activity.Home.MY_REQUEST_CODE;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.projecttravel.R;
import com.example.projecttravel.activity.DetailAccount;
import com.example.projecttravel.activity.Home;
import com.example.projecttravel.activity.Login;
import com.example.projecttravel.dao.AccountDB;
import com.example.projecttravel.model.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    private View mView;
    private TextView txtLogout, txtTen, txtThongTin, txtDoiMK, txtDoiTac, txtQuanLyKS;
    private ImageView imgAvata;
    AccountDB accountDB = new AccountDB();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_account, container, false);

        initUI();
        setUserInfo();
        initLinsenter();

        return mView;
    }

    private void initLinsenter() {
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        txtThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailAccount.class);
                startActivity(intent);
            }
        });

        imgAvata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
            }
        });
    }

    private void initUI() {
        txtLogout = mView.findViewById(R.id.txtLogout);
        txtTen = mView.findViewById(R.id.txtTen);
        txtThongTin = mView.findViewById(R.id.txtThongTin);
        txtDoiMK = mView.findViewById(R.id.txtDoiMK);
        txtDoiTac = mView.findViewById(R.id.txtDoiTac);
        txtQuanLyKS = mView.findViewById(R.id.txtQuanLyKS);
        imgAvata = mView.findViewById(R.id.imgAvatar);
    }

    public void updateImage() {
        Home home = (Home) getActivity();
        if (home == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            home.openGallery();
            return;
        }

        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            home.openGallery();
        } else {
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    public void setBitmapImageView(Bitmap bitmapImageView) {
        imgAvata.setImageBitmap(bitmapImageView);
    }

    public void setUserInfo() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Glide.with(getActivity()).load(currentUser.getPhotoUrl()).error(R.drawable.ic_account_circle).into(imgAvata);

        accountDB.getCurrentAccount(new AccountDB.CurrentAccountCallBack() {
            @Override
            public void onCurrentAccount(Account currentAccount) {
                String firstName = currentAccount.getFirstName().trim();
                String lastName = currentAccount.getLastName().trim();
                txtTen.setText(lastName + " " + firstName);
            }
        });
    }

}

