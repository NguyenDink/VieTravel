package com.example.projecttravel.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.projecttravel.R;
import com.example.projecttravel.activity.DetailAccount;
import com.example.projecttravel.activity.UpdateHotel;
import com.example.projecttravel.model.Hotel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HotelAdapter extends ArrayAdapter<Hotel> {
    Activity context;
    int layoutId;
    ArrayList<Hotel> arrHotel;


    public HotelAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<Hotel> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutId = resource;
        this.arrHotel = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = context.getLayoutInflater().inflate(layoutId, null);
        TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
        TextView txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);
        TextView txtCapacity = (TextView) convertView.findViewById(R.id.txtCapacity);
        TextView txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
        ImageView imgHotel = (ImageView) convertView.findViewById(R.id.imgHotel);
        Hotel hotel = arrHotel.get(position);
        txtName.setText(hotel.getName());
        txtAddress.setText(hotel.getAddress());
        txtCapacity.setText(hotel.getCapacity()+"");
        txtPrice.setText(hotel.getPrice()+"");

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("hotels").child(hotel.getHotel_id()+".jpg");
        storageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    // Xử lý khi lấy URL thành công
                    String urlHotel = uri.toString();
                    Glide.with(getContext()).load(urlHotel)
                            .error(R.drawable.ic_launcher_background)
                            .transform(new CenterCrop(), new GranularRoundedCorners(40, 40, 40, 40))
                            .into(imgHotel);
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi không thể lấy URL của ảnh
                    Toast.makeText(getContext(),"Failed to get image URL from Firebase Storage: " + e.getMessage(),Toast.LENGTH_SHORT).show();
                });

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, UpdateHotel.class);
//                intent.putExtra("object", hotel);
//                context.startActivity(intent);
//            }
//        });

        return convertView;
    }
}
