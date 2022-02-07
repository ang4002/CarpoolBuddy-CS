package com.example.carpoolbuddy.vehicle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.carpoolbuddy.MainActivity;
import com.example.carpoolbuddy.R;
import com.example.carpoolbuddy.models.vehicles.Vehicle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This activity allows users to view all of the currently available vehicles.
 *
 * @author Alvin Ng
 * @version 0.1
 */

public class VehiclesInfoActivity extends AppCompatActivity {
    private RecyclerView vehicleRecView;
    private FirebaseFirestore firestore;
    private ArrayList<Vehicle> allVehicles;
    private VehicleRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles_info);

        allVehicles = new ArrayList<>();
        vehicleRecView = findViewById(R.id.vehicleRecView);
        firestore = FirebaseFirestore.getInstance();
        adapter = new VehicleRecyclerViewAdapter(allVehicles);

        getAndPopulateData();

        adapter.setOnItemClickListener(new VehicleRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                goToVehicleProfile(position);
            }
        });

        vehicleRecView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * This method fetches data from firebase and populates the recyclerView using said data.
     *
     */
    public void getAndPopulateData() {
        firestore.collection("vehicles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        Vehicle currVehicle = document.toObject(Vehicle.class);

                        if(currVehicle.isOpen()) {
                            allVehicles.add(currVehicle);
                        }
                    }

                    vehicleRecView.setAdapter(adapter);
                } else {
                    Log.d("Error: ", task.getException().getMessage());
                }
            }
        });
    }
    public void goToVehicleProfile(int position) {
        Vehicle currVehicle = allVehicles.get(position);
        Intent intent = new Intent(VehiclesInfoActivity.this, VehicleProfileActivity.class);

        intent.putExtra("currVehicle", currVehicle);
        startActivity(intent);
        finish();
    }

    public void goToAddVehicle(View v) {
        Intent intent = new Intent(this, AddVehicleActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
}