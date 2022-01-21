package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.carpoolbuddy.Models.User;
import com.example.carpoolbuddy.Models.Vehicle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AddVehicleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private EditText carModelField;
    private EditText capacityField;
    private EditText basePriceField;
    private Spinner vehicleTypeSpinner;
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private User currUserObject;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        carModelField = findViewById(R.id.carModelField);
        capacityField = findViewById(R.id.capacityField);
        basePriceField = findViewById(R.id.basePriceField);
        vehicleTypeSpinner = findViewById(R.id.vehicleTypeSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.carTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleTypeSpinner.setAdapter(adapter);
        vehicleTypeSpinner.setOnItemSelectedListener(this);

        firestore.collection("users").document(currUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    currUserObject = ds.toObject(User.class);
                } else {
                    Toast.makeText(AddVehicleActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addVehicle(View v) {
        String owner = currUser.getDisplayName();
        String model = carModelField.getText().toString();
        String capacityString = capacityField.getText().toString();
        String basePriceString = basePriceField.getText().toString();
        String vehicleType = vehicleTypeSpinner.getSelectedItem().toString();
        int capacity = 0;
        int basePrice = 0;

        try {
            capacity = Integer.parseInt(capacityString);
            basePrice = Integer.parseInt(basePriceString);
        } catch(Exception e) {
            Toast.makeText(AddVehicleActivity.this, "Please enter a number for the capacity and/or price.", Toast.LENGTH_SHORT).show();
            return;
        }

        Vehicle newVehicle = new Vehicle(owner, model, capacity, vehicleType, basePrice);

        currUserObject.addOwnedVehicle(newVehicle.getVehicleID());
        firestore.collection("vehicles").document(newVehicle.getVehicleID()).set(newVehicle);
        firestore.collection("users").document(currUser.getUid()).update("ownedVehicles", currUserObject.getOwnedVehicles());

        carModelField.getText().clear();
        capacityField.getText().clear();
        basePriceField.getText().clear();

        Toast.makeText(AddVehicleActivity.this, "Vehicle successfully added!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {}

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
}