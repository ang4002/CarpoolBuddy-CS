package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.carpoolbuddy.AuthActivity.CompleteSignUpActivity;
import com.example.carpoolbuddy.AuthActivity.SignInActivity;
import com.example.carpoolbuddy.Models.Vehicle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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

        firestore.collection("vehicles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("Vehicle", document.toObject(Vehicle.class).toString());
                        allVehicles.add(document.toObject(Vehicle.class));
                        Log.d("Vehicle", Integer.toString(allVehicles.size()));
                    }

                    vehicleRecView.setAdapter(adapter);
                } else {
                    Log.d("Error: ", task.getException().getMessage());
                }
            }
        });

        adapter.setOnItemClickListener(new VehicleRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                goToVehicleProfile(position);
            }
        });

        vehicleRecView.setLayoutManager(new LinearLayoutManager(this));

//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void goToVehicleProfile(int position) {
        Vehicle currVehicle = allVehicles.get(position);
        Intent intent = new Intent(VehiclesInfoActivity.this, VehicleProfileActivity.class);

        intent.putExtra("currVehicle", currVehicle);
        startActivity(intent);
        finish();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivityForResult(myIntent, 0);
//        return true;
//    }
}