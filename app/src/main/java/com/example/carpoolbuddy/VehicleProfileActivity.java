package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carpoolbuddy.Models.User;
import com.example.carpoolbuddy.Models.Vehicle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class VehicleProfileActivity extends AppCompatActivity {
    private TextView vehicleModel;
    private TextView basePrice;
    private TextView riders;
    private TextView owner;
    private TextView capacity;
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private FirebaseFirestore firestore;
    private User currUserObject;
    private Vehicle currVehicle;
    private ImageButton joinCarBtn;
    private ImageButton leaveCarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_profile);

        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        vehicleModel = findViewById(R.id.vehicleModel);
        basePrice = findViewById(R.id.basePrice);
        riders = findViewById(R.id.riders);
        owner = findViewById(R.id.owner);
        capacity = findViewById(R.id.capacity);
        joinCarBtn = findViewById(R.id.joinCarBtn);
        leaveCarBtn = findViewById(R.id.leaveCarBtn);

        firestore.collection("users").document(currUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    currUserObject = ds.toObject(User.class);
                } else {
                    Toast.makeText(VehicleProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = getIntent();
        currVehicle = (Vehicle)intent.getSerializableExtra("currVehicle");

        String modelString = currVehicle.getModel();
        String basePriceString = Double.toString(currVehicle.getBasePrice());
        String maxCapacityString = Integer.toString(currVehicle.getCapacity());
        String riderNumberString = Integer.toString(currVehicle.getRidersUIDs().size());
        String ownerString = currVehicle.getOwner();
        String capacityString = riderNumberString + "/" + maxCapacityString;
        String ridersString = "";

        if(currVehicle.getRidersNames().isEmpty()) {
            ridersString = "none";
        } else {
            ridersString = String.join(", ", currVehicle.getRidersNames());
        }

        owner.setText("Owner: " + ownerString);
        capacity.setText("Capacity: " + capacityString + " people");
        vehicleModel.setText("Model: " + modelString);
        basePrice.setText("Base price: $" + basePriceString);
        riders.setText("Riders: " + ridersString);

        if(currVehicle.getRidersUIDs().contains(currUser.getUid())) {
            leaveCarBtn.setVisibility(View.VISIBLE);
            joinCarBtn.setVisibility(View.GONE);
        } else {
            leaveCarBtn.setVisibility(View.GONE);
            joinCarBtn.setVisibility(View.VISIBLE);
        }
    }

    public void joinCar(View v) {
        if (currVehicle.getCapacity() == currVehicle.getRidersUIDs().size()) {
            Toast.makeText(VehicleProfileActivity.this, "This ride has already reached maximum capacity.", Toast.LENGTH_SHORT).show();
        } else if (currVehicle.getOwner().equals(currUserObject.getName())) {
            Toast.makeText(VehicleProfileActivity.this, "You are the owner of this vehicle!", Toast.LENGTH_SHORT).show();
        } else {
            currVehicle.addRiderUID(currUserObject.getUuid());
            currVehicle.addRiderName(currUserObject.getName());

            firestore.collection("vehicles").document(currVehicle.getVehicleID()).set(currVehicle);
            finish();
            startActivity(getIntent());
            Toast.makeText(VehicleProfileActivity.this, "You have been added to this ride!", Toast.LENGTH_SHORT).show();
            }
    }

    public void leaveCar(View v) {
        currVehicle.getRidersUIDs().remove(currUserObject.getUuid());
        currVehicle.getRidersNames().remove(currUserObject.getName());

        firestore.collection("vehicles").document(currVehicle.getVehicleID()).set(currVehicle);
        finish();
        startActivity(getIntent());
        Toast.makeText(VehicleProfileActivity.this, "You have been removed from this ride!", Toast.LENGTH_SHORT).show();
    }
}