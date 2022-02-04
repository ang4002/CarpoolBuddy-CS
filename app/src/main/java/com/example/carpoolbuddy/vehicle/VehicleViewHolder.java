package com.example.carpoolbuddy.vehicle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carpoolbuddy.R;

public class VehicleViewHolder extends RecyclerView.ViewHolder {
    protected TextView model;
    protected TextView capacity;
    protected TextView basePrice;
    protected TextView owner;
    private ImageButton carProfileBtn;
    private AdapterView.OnItemClickListener mListener;

    public VehicleViewHolder(@NonNull View itemView, VehicleRecyclerViewAdapter.OnItemClickListener listener) {
        super(itemView);

        model = itemView.findViewById(R.id.model);
        capacity = itemView.findViewById(R.id.capacity);
        owner = itemView.findViewById(R.id.owner);
        basePrice = itemView.findViewById(R.id.distance);
        carProfileBtn = itemView.findViewById(R.id.carProfileBtn);

        carProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }
}
