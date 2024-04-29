package com.example.projecttravel.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projecttravel.R;
import com.example.projecttravel.dao.AccountDB;
import com.example.projecttravel.fragment.AccountFragment;
import com.example.projecttravel.model.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class UpdateAccount extends AppCompatActivity {
    public static final int MY_REQUEST_CODE = 1;
    private EditText edtFirstName, edtLastName, edtPhone;
    private RadioButton radNam, radNu;
    private Button btnUpdate;
    private ImageView imgAvatar;
    private Uri mUri;
    private ProgressDialog progressDialog;
    AccountDB accountDB = new AccountDB();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        initUI();
        progressDialog = new ProgressDialog(this);
        setVariable();
        initListener();
    }

    private void initListener() {

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = edtFirstName.getText().toString().trim();
                String lastName = edtLastName.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                int gender;
                if (radNam.isChecked())
                    gender = 0;
                else gender = 1;
                if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(UpdateAccount.this, "Vui lòng không bỏ trống thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    accountDB.updateAccount(firstName, lastName, phone, gender, new AccountDB.UpdateAccountCallback() {
                        @Override
                        public void onUpdateSuccess() {
                            Toast.makeText(UpdateAccount.this, "Cập nhật thông tin tài khoản thành công!", Toast.LENGTH_SHORT).show();
//                            if (mUri != null) {
//                                FirebaseUser currrentUser = FirebaseAuth.getInstance().getCurrentUser();
//                                if (currrentUser == null) {
//                                    return;
//                                }
//                                progressDialog.show();
//                                UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
//                                        .setPhotoUri(mUri).build();
//                                currrentUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        progressDialog.dismiss();
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(UpdateAccount.this, "Cập nhật ảnh đại diện thành công", Toast.LENGTH_SHORT).show();
//                                            finish();
//                                        }
//                                    }
//                                });
//                            } else
                            finish();
                        }

                        @Override
                        public void onUpdateFailure(String errorMessage) {
                            Toast.makeText(UpdateAccount.this, "Lỗi khi cập nhật thông tin tài khoản: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void setVariable() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        mUri = currentUser.getPhotoUrl();
        Glide.with(this).load(currentUser.getPhotoUrl()).error(R.drawable.ic_account_circle).into(imgAvatar);
        accountDB.getCurrentAccount(new AccountDB.CurrentAccountCallBack() {
            @Override
            public void onCurrentAccount(Account currentAccount) {
                edtFirstName.setText(currentAccount.getFirstName().trim());
                edtLastName.setText(currentAccount.getLastName().trim());
                edtPhone.setText(currentAccount.getPhone().trim());
                if (currentAccount.getGender() == 0) {
                    radNam.setChecked(true);
                } else
                    radNu.setChecked(true);
            }
        });
    }

    private void initUI() {
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtPhone = findViewById(R.id.edtPhone);
        radNam = findViewById(R.id.radNam);
        radNu = findViewById(R.id.radNu);
        btnUpdate = findViewById(R.id.btnUpdate);
        imgAvatar = findViewById(R.id.imgAvatar);
    }

    public void updateImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    public void setBitmapImageView(Bitmap bitmapImageView) {
        imgAvatar.setImageBitmap(bitmapImageView);
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
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Chọn ảnh"));
    }

    final private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent == null) {
                    return;
                }
                Uri uri = intent.getData();
//                mUri = uri;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    setBitmapImageView(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });

//    public void uploadImageToStorage(Uri imageUri) {
//        // Tạo một tham chiếu tới nơi bạn muốn lưu trữ ảnh trong Firebase Storage
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("avatars").child("avatar.jpg");
//        // Tải ảnh lên Storage
//        storageRef.putFile(imageUri)
//                .addOnSuccessListener(taskSnapshot -> {
//                    // Nếu tải ảnh lên thành công, lấy URL của ảnh
//                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                        // Lưu URL vào Realtime Database
//                        saveImageUrlToDatabase(uri.toString());
//                    });
//                })
//                .addOnFailureListener(e -> {
//                    // Xử lý khi tải ảnh lên thất bại
////                    Log.e("TAG", "Failed to upload image to Firebase Storage: " + e.getMessage());
//                });
//    }
}