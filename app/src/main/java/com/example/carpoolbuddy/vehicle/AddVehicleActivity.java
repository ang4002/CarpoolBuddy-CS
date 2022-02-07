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
import com.example.carpoolbuddy.models.vehicles.Bicycle;
import com.example.carpoolbuddy.models.vehicles.ElectricCar;
import com.example.carpoolbuddy.models.vehicles.FossilFuelCar;
import com.example.carpoolbuddy.models.vehicles.Helicopter;
import com.example.carpoolbuddy.models.vehicles.Segway;
import com.example.carpoolbuddy.models.vehicles.Vehicle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * This activity allows users to list their vehicles in the app.
 *
 * @author Alvin Ng
 * @version 0.1
 */

public class AddVehicleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private EditText carModelField;
    private EditText capacityField;
    private EditText maxAltitudeField;
    private EditText maxAirSpeedField;
    private EditText weightField;
    private EditText weightCapacityField;
    private EditText rangeField;
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
        maxAltitudeField = findViewById(R.id.maxAltitudeField);
        maxAirSpeedField = findViewById(R.id.maxAirSpeedField);
        weightField = findViewById(R.id.weightField);
        weightCapacityField = findViewById(R.id.weightCapacityField);
        rangeField = findViewById(R.id.rangeField);
        vehicleTypeSpinner = findViewById(R.id.vehicleTypeSpinner);

//        Setting spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.carTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleTypeSpinner.setAdapter(adapter);
        vehicleTypeSpinner.setOnItemSelectedListener(this);

        vehicleTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String vehicleType = vehicleTypeSpinner.getItemAtPosition(position).toString();

                if(vehicleType.equalsIgnoreCase("bicycle")) {
                    weightField.setVisibility(View.VISIBLE);
                    weightCapacityField.setVisibility(View.VISIBLE);
                    maxAltitudeField.setVisibility(View.GONE);
                    maxAirSpeedField.setVisibility(View.GONE);
                    rangeField.setVisibility(View.GONE);
                } else if(vehicleType.equalsIgnoreCase("electriccar") || vehicleType.equalsIgnoreCase("fossilfuelcar")) {
                    weightField.setVisibility(View.GONE);
                    weightCapacityField.setVisibility(View.GONE);
                    maxAltitudeField.setVisibility(View.GONE);
                    maxAirSpeedField.setVisibility(View.GONE);
                    rangeField.setVisibility(View.VISIBLE);
                } else if(vehicleType.equalsIgnoreCase("helicopter")) {
                    weightField.setVisibility(View.GONE);
                    weightCapacityField.setVisibility(View.GONE);
                    maxAltitudeField.setVisibility(View.VISIBLE);
                    maxAirSpeedField.setVisibility(View.VISIBLE);
                    rangeField.setVisibility(View.GONE);
                } else {
                    weightField.setVisibility(View.GONE);
                    weightCapacityField.setVisibility(View.VISIBLE);
                    maxAltitudeField.setVisibility(View.GONE);
                    maxAirSpeedField.setVisibility(View.GONE);
                    rangeField.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

//        Adding back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * This method creates a vehicle object based on the user's input, adds it to the intent, and leads the user to the
     * MapsActivity screen.
     *
     * @param v the object of the xml file.
     */
    public void addVehicle(View v) {
        Vehicle newVehicle = new Vehicle();
        String owner = currUser.getDisplayName();
        String ownerID = currUser.getUid();
        String model = carModelField.getText().toString();
        String capacityString = capacityField.getText().toString();
        String vehicleType = vehicleTypeSpinner.getSelectedItem().toString();
        String weightString = weightField.getText().toString();
        String weightCapacityString = weightCapacityField.getText().toString();
        String maxAltitudeString = maxAltitudeField.getText().toString();
        String maxAirSpeedString = maxAirSpeedField.getText().toString();
        String rangeString = rangeField.getText().toString();

        int capacity = 0;
        int weight = 0;
        int weightCapacity = 0;
        int rideCost = 0;
        int maxAltitude = 0;
        int maxAirSpeed = 0;
        int distance = 0;
        int range = 0;

        // Attempt to parse strings from inputs into integers
        try {
            capacity = Integer.parseInt(capacityString);
        } catch(Exception e) {
            Toast.makeText(AddVehicleActivity.this, "Please enter an integer for the capacity and/or price.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check for vehicle type, create object accordingly
        if(vehicleType.equalsIgnoreCase("bicycle")) {
            try {
                weight = Integer.parseInt(weightString);
                weightCapacity = Integer.parseInt(weightCapacityString);
            } catch(Exception e) {
                Toast.makeText(AddVehicleActivity.this, "Please enter an integer for the capacity and/or price.", Toast.LENGTH_SHORT).show();
                return;
            }

            newVehicle = new Bicycle(owner, ownerID, model, capacity, vehicleType, rideCost, distance, weight, weightCapacity);
        } else if(vehicleType.equalsIgnoreCase("segway")) {
            try {
                range = Integer.parseInt(rangeString);
                weightCapacity = Integer.parseInt(weightCapacityString);
            } catch(Exception e) {
                Toast.makeText(AddVehicleActivity.this, "Please enter an integer for the capacity and/or price.", Toast.LENGTH_SHORT).show();
                return;
            }

            newVehicle = new Segway(owner, ownerID, model, capacity, vehicleType, rideCost, distance, range, weightCapacity);
        } else if(vehicleType.equalsIgnoreCase("electriccar")) {
            try {
                range = Integer.parseInt(rangeString);
            } catch(Exception e) {
                Toast.makeText(AddVehicleActivity.this, "Please enter an integer for the capacity and/or price.", Toast.LENGTH_SHORT).show();
                return;
            }

            newVehicle = new ElectricCar(owner, ownerID, model, capacity, vehicleType, rideCost, distance, range);
        } else if(vehicleType.equalsIgnoreCase("fossilfuelcar")) {
            try {
                range = Integer.parseInt(rangeString);
            } catch(Exception e) {
                Toast.makeText(AddVehicleActivity.this, "Please enter an integer for the capacity and/or price.", Toast.LENGTH_SHORT).show();
                return;
            }

            newVehicle = new FossilFuelCar(owner, ownerID, model, capacity, vehicleType, rideCost, distance, range);
        } else {
            try {
                maxAltitude = Integer.parseInt(maxAltitudeString);
                maxAirSpeed = Integer.parseInt(maxAirSpeedString);
            } catch(Exception e) {
                Toast.makeText(AddVehicleActivity.this, "Please enter an integer for the capacity and/or price.", Toast.LENGTH_SHORT).show();
                return;
            }

            newVehicle = new Helicopter(owner, ownerID, model, capacity, vehicleType, rideCost, distance, maxAltitude, maxAirSpeed);
        }

        carModelField.getText().clear();
        capacityField.getText().clear();
        maxAltitudeField.getText().clear();
        maxAirSpeedField.getText().clear();
        weightField.getText().clear();;
        weightCapacityField.getText().clear();

        // Put created vehicle in intent and start new activity
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