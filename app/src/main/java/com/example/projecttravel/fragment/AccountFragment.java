package com.example.projecttravel.fragment;


import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.projecttravel.activity.MainActivity;
import com.example.projecttravel.activity.UpdateAccount;
import com.example.projecttravel.dao.AccountDB;
import com.example.projecttravel.model.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
    private ImageView imgAvatar;
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

        txtDoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
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
        imgAvatar = mView.findViewById(R.id.imgAvatar);
    }

    public void setUserInfo() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Glide.with(getActivity()).load(currentUser.getPhotoUrl()).error(R.drawable.ic_account_circle).into(imgAvatar);

        accountDB.getCurrentAccount(new AccountDB.CurrentAccountCallBack() {
            @Override
            public void onCurrentAccount(Account currentAccount) {
                String firstName = currentAccount.getFirstName().trim();
                String lastName = currentAccount.getLastName().trim();
                txtTen.setText(lastName + " " + firstName);
            }
        });
    }

    public void changePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        EditText mkCu = dialogView.findViewById(R.id.edtMKHientai);
        EditText mkMoi = dialogView.findViewById(R.id.edtMKMoi);
        EditText mkMoi2 = dialogView.findViewById(R.id.edtXNMKMoi);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.findViewById(R.id.btnChange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = mkCu.getText().toString().trim();
                String newPassword = mkMoi.getText().toString().trim();
                String newPassword2 = mkMoi2.getText().toString().trim();
                if(TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) ||TextUtils.isEmpty(newPassword2)){
                    Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin ", Toast.LENGTH_SHORT).show();
                    return;
                };

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Lấy user ID của người dùng đang đăng nhập
                    String userID = user.getUid();

                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
                    // Xác thực lại người dùng với mật khẩu cũ
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Mật khẩu cũ đúng, tiến hành đổi mật khẩu mới
                                if (newPassword.equals(newPassword2)) {
                                    // Cập nhật mật khẩu mới trên Authentication của Firebase
                                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Cập nhật mật khẩu mới vào Firebase Realtime Database
                                                accountDB.updatePassword(newPassword, new AccountDB.UpdateAccountCallback() {
                                                    @Override
                                                    public void onUpdateSuccess() {
                                                        dialog.dismiss();
                                                        Toast.makeText(requireContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();

                                                        // Đăng xuất người dùng và chuyển hướng đến màn hình đăng nhập
                                                        FirebaseAuth.getInstance().signOut();
                                                        Intent intent = new Intent(getActivity(), Login.class);
                                                        startActivity(intent);
                                                        getActivity().finish();
                                                        dialog.dismiss();
                                                    }

                                                    @Override
                                                    public void onUpdateFailure(String errorMessage) {
                                                        Toast.makeText(requireContext(), "Lỗi khi cập nhật thông tin tài khoản: " + errorMessage, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                // Đổi mật khẩu không thành công
                                                Toast.makeText(requireContext(), "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    // Mật khẩu mới không khớp
                                    Toast.makeText(requireContext(), "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Mật khẩu cũ không đúng
                                Toast.makeText(requireContext(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}

