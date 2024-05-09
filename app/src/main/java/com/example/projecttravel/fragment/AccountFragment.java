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
import android.widget.Button;
import android.widget.CheckBox;
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
import com.example.projecttravel.activity.ManageBooking;
import com.example.projecttravel.activity.ManageHotel;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AccountFragment extends Fragment {

    private View mView;
    private TextView txtLogout, txtTen, txtThongTin, txtDoiMK, txtDoiTac, txtQuanLyKS, txtQuanLyBooking;
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
        txtQuanLyBooking.setOnClickListener(new View.OnClickListener() {
            int role;
            @Override
            public void onClick(View v) {
                accountDB = new AccountDB();
                accountDB.getCurrentAccount(new AccountDB.CurrentAccountCallBack() {
                    @Override
                    public void onCurrentAccount(Account currentAccount) {
                        role = currentAccount.getRole();
                        if (role == 2) {
                            Intent intent = new Intent(getActivity(), ManageBooking.class);
                            startActivity(intent);
                        } else
                            Toast.makeText(getActivity(), "Vui lòng trở thành đối tác với VieTravel", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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

        txtDoiTac.setOnClickListener(new View.OnClickListener() {
            int role;
            @Override
            public void onClick(View v) {
                accountDB = new AccountDB();
                accountDB.getCurrentAccount(new AccountDB.CurrentAccountCallBack() {
                    @Override
                    public void onCurrentAccount(Account currentAccount) {
                        role = currentAccount.getRole();
                        if (role == 2) {
                            Toast.makeText(getActivity(), "Hiện bạn đang là đối tác với VieTravel", Toast.LENGTH_SHORT).show();
                        } else
                            becomePartner();
                    }
                });
            }
        });

        txtQuanLyKS.setOnClickListener(new View.OnClickListener() {
            int role;
            @Override
            public void onClick(View v) {
                accountDB = new AccountDB();
                accountDB.getCurrentAccount(new AccountDB.CurrentAccountCallBack() {
                    @Override
                    public void onCurrentAccount(Account currentAccount) {
                        role = currentAccount.getRole();
                        if (role == 2) {
                            Intent intent = new Intent(getActivity(), ManageHotel.class);
                            startActivity(intent);
                        } else
                            Toast.makeText(getActivity(), "Vui lòng trở thành đối tác với VieTravel", Toast.LENGTH_SHORT).show();
                    }
                });
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
        txtQuanLyBooking = mView.findViewById(R.id.txtQuanLyBooking);
        imgAvatar = mView.findViewById(R.id.imgAvatar);
    }

    public void setUserInfo() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String account_id = currentUser.getUid();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("avatars").child(account_id+".jpg");
        storageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Xử lý khi lấy URL thành công
                    String urlAvatar = uri.toString();
                    Glide.with(this).load(urlAvatar).error(R.drawable.ic_account_circle).into(imgAvatar);
                    // Sử dụng URL của ảnh ở đây
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi không thể lấy URL của ảnh
                    Toast.makeText(getActivity(),"Failed to get image URL from Firebase Storage: " + e.getMessage(),Toast.LENGTH_SHORT).show();
                });

        accountDB.getCurrentAccount(new AccountDB.CurrentAccountCallBack() {
            @Override
            public void onCurrentAccount(Account currentAccount) {
                String firstName = currentAccount.getFirstName().trim();
                String lastName = currentAccount.getLastName().trim();
                txtTen.setText(lastName + " " + firstName);
            }
        });
    }

    public void becomePartner() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_become_partner, null);
        Button btnAgree = dialogView.findViewById(R.id.btnReset);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        CheckBox checkAgree = dialogView.findViewById(R.id.checkAgree);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAgree.isChecked()) {
                    accountDB.updateRole(2, new AccountDB.UpdateAccountCallback() {
                        @Override
                        public void onUpdateSuccess() {
                            dialog.dismiss();
                            Toast.makeText(requireContext(), "Chào mừng bạn trở thành đối tác của VieTravel", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onUpdateFailure(String errorMessage) {
                            Toast.makeText(requireContext(), "Lỗi khi cập nhật quyền người dùng: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(requireContext(), "Vui lòng đồng ý với Chính sách và Điều khoản của VieTravel", Toast.LENGTH_SHORT).show();
                }
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

