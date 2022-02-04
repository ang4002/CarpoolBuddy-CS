package com.example.carpoolbuddy.vehicle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carpoolbuddy.R;
import com.example.carpoolbuddy.models.Vehicle;

import java.util.ArrayList;

public class VehicleRecyclerViewAdapter extends RecyclerView.Adapter<VehicleViewHolder> {
    private ArrayList<Vehicle> allVehicles;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public VehicleRecyclerViewAdapter(ArrayList allVehicles) {
        this.allVehicles = allVehicles;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_row_view, parent, false);
        VehicleViewHolder holder = new VehicleViewHolder(myView, mListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle currVehicle = allVehicles.get(position);

        String model = currVehicle.getModel();
        String basePrice = Double.toString(currVehicle.getBasePrice());
        String maxCapacity = Integer.toString(currVehicle.getCapacity());
        int riderNumber = currVehicle.getRidersUIDs().size();
        String owner = currVehicle.getOwner();
        String capacity = riderNumber + "/" + maxCapacity;

        holder.model.setText("Model: " + model);
        holder.owner.setText("Owner: " + owner);
        holder.basePrice.setText("Base price: $" + basePrice);
        holder.capacity.setText("Capacity: " + capacity + " people");
    }

    @Override
    public int getItemCount() {
        return allVehicles.size();
    }
}
