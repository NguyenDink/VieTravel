package com.example.projecttravel.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.projecttravel.R;
import com.example.projecttravel.adapter.AutoLocationAdapter;
import com.example.projecttravel.dao.HotelDB;
import com.example.projecttravel.model.Hotel;
import com.example.projecttravel.model.Location;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddHotel extends AppCompatActivity {
    public static final int MY_REQUEST_CODE = 2;
    private Uri mUri = null;
    private ImageView imgHotel;
    private EditText edtName, edtAddress, edtPrice, edtDescription;
    private AutoCompleteTextView autoCompleteLocation;
    private Spinner spinType, spinCapacity;
    private Button btnAddHotel, btnCancel;
    private ArrayAdapter<Integer> adapterCapacity = null;
    private ArrayAdapter<String> adapterCategory = null;
    private AutoLocationAdapter autoLocationAdapter = null;
    private List<Location> listLocation;
    String name, address, description, location_name, account_id;
    Double price;
    int location_id, category_id, capacity, hotel_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotel);
        initUI();
        getListLocation();
        initListener();
    }

    private void initListener() {
        imgHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
            }
        });
        autoCompleteLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Location selectedLocation = (Location) parent.getItemAtPosition(position);
                location_id = selectedLocation.getLocation_id();
            }
        });

        autoCompleteLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần thực hiện gì trước khi văn bản thay đổi
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Khi văn bản thay đổi, thực hiện việc lọc danh sách dựa trên văn bản đã nhập
                String input = s.toString().toLowerCase();
                List<Location> filteredLocations = new ArrayList<>();
                for (Location location : listLocation) {
                    if (location.getName().toLowerCase().contains(input)) {
                        filteredLocations.add(location);
                    }
                }
                // Tạo một adapter mới và gán vào AutoCompleteTextView
                autoLocationAdapter = new AutoLocationAdapter(AddHotel.this, filteredLocations);
                autoCompleteLocation.setAdapter(autoLocationAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần thực hiện gì sau khi văn bản thay đổi
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAddHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHotel();
            }
        });
    }

    private void initUI() {
        imgHotel = findViewById(R.id.imgHotel);
        edtName = findViewById(R.id.edtName);
        edtAddress = findViewById(R.id.edtAddress);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        autoCompleteLocation = findViewById(R.id.autoCompleteLocation);
        spinType = findViewById(R.id.spinType);
        spinCapacity = findViewById(R.id.spinCapacity);
        btnAddHotel = findViewById(R.id.btnAddHotel);
        btnCancel = findViewById(R.id.btnCancel);

        Integer[] capacity = {1, 2, 4, 8, 10};
        adapterCapacity = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, capacity);
        adapterCapacity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCapacity.setAdapter(adapterCapacity);

        String[] type = {"Khách sạn", "Homestay"};
        adapterCategory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, type);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinType.setAdapter(adapterCategory);

        listLocation = new ArrayList<>();
        autoLocationAdapter = new AutoLocationAdapter(this, listLocation);
        autoCompleteLocation.setAdapter(autoLocationAdapter);
        autoCompleteLocation.setThreshold(1);
    }

    public void getListLocation() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Location");
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listLocation != null)
                    listLocation.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Location location = dataSnapshot.getValue(Location.class);
                    listLocation.add(location);
                }
                autoLocationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddHotel.this, "Không thể tải danh sách địa điểm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addHotel() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        account_id = currentUser.getUid();

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Hotel_id");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               hotel_id = snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if ((edtName.getText().toString()).isEmpty() || (edtDescription.getText().toString()).isEmpty() ||
                (edtAddress.getText().toString()).isEmpty() || (autoCompleteLocation.getText().toString()).isEmpty() || (edtPrice.getText().toString()).isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            name = edtName.getText().toString().trim();
            description = edtDescription.getText().toString().trim();
            address = edtAddress.getText().toString().trim();
            location_name = autoCompleteLocation.getText().toString().trim();
            price =  Double.parseDouble(edtPrice.getText().toString().trim());
            if (spinType.getSelectedItemPosition() == 0)
                category_id = 1;
            else
                category_id = 2;
            capacity = (int) spinCapacity.getSelectedItem();
            Hotel hotel = new Hotel(hotel_id, location_id, name, category_id, description, capacity, address, price, account_id);
            HotelDB hotelDB = new HotelDB();
            hotelDB.addHotel(this, hotel, hotel_id);
            if (mUri != null)
                uploadImageToStorage(mUri);
            myRef.setValue(hotel_id+1);
            finish();
        }
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
                    Toast.makeText(AddHotel.this, "Upload image to Firebase Storage successful", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi tải ảnh lên thất bại
                    Toast.makeText(AddHotel.this, "Failed to upload image to Firebase Storage: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}