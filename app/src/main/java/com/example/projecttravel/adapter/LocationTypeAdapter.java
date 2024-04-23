package com.example.projecttravel.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.projecttravel.R;
import com.example.projecttravel.model.LocationType;

import java.util.List;

public class LocationTypeAdapter extends RecyclerView.Adapter<LocationTypeAdapter.ViewHolder> {
    private List<LocationType> items;

    public LocationTypeAdapter(List<LocationType> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public LocationTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_location_type, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationTypeAdapter.ViewHolder holder, int position) {
        holder.txtName.setText(items.get(position).getName());
        String id = String.valueOf(items.get(position).getLocationType_id());
        String imageName = "cat" + id; // Tạo tên của tệp ảnh dựa trên ID
        int resourceId = holder.itemView.getContext().getResources()
                .getIdentifier(imageName, "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(resourceId)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imgCate);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView imgCate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            imgCate = itemView.findViewById(R.id.imgCate);
        }
    }
}
