package com.example.carpoolbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileBtn = findViewById(R.id.profileBtn);
    }

    public void goToProfile(View v) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void goToAddVehicle(View v) {
        Intent intent = new Intent(this, AddVehicleActivity.class);
        startActivity(intent);
    }

    public void seeVehicles(View v) {
        Intent intent = new Intent(this, VehiclesInfoActivity.class);
        startActivity(intent);
        finish();
    }
}