package com.example.projecttravel.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projecttravel.R;
import com.example.projecttravel.adapter.AutoLocationAdapter;
import com.example.projecttravel.dao.HotelDB;
import com.example.projecttravel.dao.LocationDB;
import com.example.projecttravel.model.Hotel;
import com.example.projecttravel.model.Location;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;

public class UpdateHotel extends AppCompatActivity {

    public static final int MY_REQUEST_CODE = 2;
    private Hotel hotel;
    private Uri mUri = null;
    private TextView txtName, txtLocation;
    private ImageView imgHotel, btnDelete;
    private EditText edtAddress, edtPrice, edtDescription;
    private Spinner spinType, spinCapacity;
    private Button btnUpdateHotel, btnCancel;
    private ArrayAdapter<Integer> adapterCapacity = null;
    private ArrayAdapter<String> adapterCategory = null;
    String name, address, description, location_name, account_id, urlHotel;
    Double price;
    int location_id, category_id, capacity, hotel_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hotel);
        initUI();
        setVariable();
        initListener();
    }
    public void initUI() {
        imgHotel = findViewById(R.id.imgHotel);
        txtName = findViewById(R.id.txtName);
        edtAddress = findViewById(R.id.edtAddress);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        txtLocation = findViewById(R.id.txtLocation);
        spinType = findViewById(R.id.spinType);
        spinCapacity = findViewById(R.id.spinCapacity);
        btnUpdateHotel = findViewById(R.id.btnUpdateHotel);
        btnCancel = findViewById(R.id.btnCancel);
        btnDelete = findViewById(R.id.btnDelete);

        Integer[] capacity = {1, 2, 4, 8, 10};
        adapterCapacity = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, capacity);
        adapterCapacity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCapacity.setAdapter(adapterCapacity);

        String[] type = {"Khách sạn", "Homestay"};
        adapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, type);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinType.setAdapter(adapterCategory);
    }
    public void initListener() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateHotel.this);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa khách sạn này không?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nếu người dùng chọn "Xóa", thực hiện xóa khách sạn
                        deleteHotel(hotel_id);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nếu người dùng chọn "Hủy", đóng dialog
                        dialog.dismiss();
                    }
                });
                // Hiển thị dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnUpdateHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateHotel.this);
                builder.setTitle("Xác nhận thay đổi");
                builder.setMessage("Bạn có chắc chắn muốn cập nhật thông tin khách sạn này không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateHotel(hotel_id);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                // Hiển thị dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void setVariable() {
        hotel = (Hotel) getIntent().getSerializableExtra("object");
        name = hotel.getName().toString().trim();
        capacity = hotel.getCapacity();
        address = hotel.getAddress().toString().trim();
        price = hotel.getPrice();
        description = hotel.getDescription().toString().trim();
        category_id = hotel.getCategory_id();
        location_id = hotel.getLocation_id();
        hotel_id = hotel.getHotel_id();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("hotels").child(hotel_id+".jpg");
        storageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Xử lý khi lấy URL thành công
                    urlHotel = uri.toString();
                    Glide.with(this).load(urlHotel).error(R.drawable.ic_account_circle).into(imgHotel);
                    // Sử dụng URL của ảnh ở đây
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi không thể lấy URL của ảnh
                    Toast.makeText(UpdateHotel.this,"Failed to get image URL from Firebase Storage: " + e.getMessage(),Toast.LENGTH_SHORT).show();
                });

        Integer[] listCapacity = {1, 2, 4, 8, 10};
        int positionCap = -1;
        for (int i = 0; i < listCapacity.length; i++) {
            if (listCapacity[i] == capacity) {
                positionCap = i;
                break;
            }
        }

        txtName.setText(name);
        edtAddress.setText(address);
        edtDescription.setText(description);
        edtPrice.setText(price + "");
        if (positionCap != -1) {
            spinCapacity.setSelection(positionCap);
        }
        if (category_id == 1)
            spinType.setSelection(0);
        else
            spinType.setSelection(1);

        LocationDB locationDB = new LocationDB();
        locationDB.getLocationById(location_id, new LocationDB.LocationCallback() {
            @Override
            public void onLocationRetrieved(Location location) {
                location_name = location.getName().toString().trim();
                txtLocation.setText(location_name);
            }
        });
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
                mUri = uri;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imgHotel.setImageBitmap(bitmap);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE)
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
    }

    public void uploadImageToStorage(Uri imageUri) {
        // Tạo một tham chiếu tới nơi bạn muốn lưu trữ ảnh trong Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("hotels").child(hotel_id+".jpg");
        // Tải ảnh lên Storage
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(UpdateHotel.this, "Upload image to Firebase Storage successful", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi tải ảnh lên thất bại
                    Toast.makeText(UpdateHotel.this, "Failed to upload image to Firebase Storage: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void deleteHotel(int hotel_id) {
        HotelDB hotelDB = new HotelDB();
        hotelDB.deleteHotelById(hotel_id, this, new HotelDB.DeleteHotelListener() {
            @Override
            public void onHotelDeleted() {
                finish();
            }
        });
    }

    public void updateHotel(int hotel_id) {
        if (((edtDescription.getText().toString()).isEmpty() || (edtAddress.getText().toString()).isEmpty() || (edtPrice.getText().toString()).isEmpty())) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            HotelDB hotelDB = new HotelDB();
            if (spinType.getSelectedItemPosition() == 0)
                category_id = 1;
            else
                category_id = 2;
            capacity = (int) spinCapacity.getSelectedItem();
            description = edtDescription.getText().toString().trim();
            address = edtAddress.getText().toString().trim();
            price =  Double.parseDouble(edtPrice.getText().toString().trim());
            hotelDB.updateHotelById(hotel_id, category_id, capacity, address, price, description, this, new HotelDB.UpdateHotelListener() {
                @Override
                public void onHotelUpdated() {
                    if (mUri != null)
                        uploadImageToStorage(mUri);
                    finish();
                }
            });
        }
    }
}