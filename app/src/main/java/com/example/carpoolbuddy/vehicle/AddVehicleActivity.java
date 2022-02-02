package com.example.carpoolbuddy.vehicle;

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

import com.example.carpoolbuddy.MainActivity;
import com.example.carpoolbuddy.R;
import com.example.carpoolbuddy.vehicle.maps.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AddVehicleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private EditText carModelField;
    private EditText capacityField;
    private EditText basePriceField;
    private Spinner vehicleTypeSpinner;
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();

        carModelField = findViewById(R.id.carModelField);
        capacityField = findViewById(R.id.capacityField);
        basePriceField = findViewById(R.id.basePriceField);
        vehicleTypeSpinner = findViewById(R.id.vehicleTypeSpinner);

//        Setting spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.carTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleTypeSpinner.setAdapter(adapter);
        vehicleTypeSpinner.setOnItemSelectedListener(this);

//        Adding back button
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
            Toast.makeText(AddVehicleActivity.this, "Please enter an integer for the capacity and/or price.", Toast.LENGTH_SHORT).show();
            return;
        }

        Vehicle newVehicle = new Vehicle(owner, model, capacity, vehicleType, basePrice);

        carModelField.getText().clear();
        capacityField.getText().clear();
        basePriceField.getText().clear();

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("newVehicle", newVehicle);
        startActivity(intent);
        finish();
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