package com.example.projecttravel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projecttravel.model.Location;

import java.util.ArrayList;
import java.util.List;

public class AutoLocationAdapter extends ArrayAdapter<Location> implements Filterable {

    private List<Location> locationsFull;
    private List<Location> locationsFiltered;
    private LayoutInflater inflater;

    public AutoLocationAdapter(Context context, List<Location> locations) {
        super(context, 0, locations);
        locationsFull = new ArrayList<>(locations);
        locationsFiltered = new ArrayList<>(locations);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        Location location = getItem(position);
        if (location != null) {
            textView.setText(location.getName());
        }

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return locationFilter;
    }

    public Filter locationFilter = new Filter() {
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Location> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(locationsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Location location : locationsFull) {
                    if (location.getName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(location);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            locationsFiltered.clear();
            locationsFiltered.addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Location) resultValue).getName();
        }
    };
}
