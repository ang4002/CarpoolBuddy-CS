package com.example.carpoolbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.carpoolbuddy.user.UserProfileActivity;
import com.example.carpoolbuddy.vehicle.AddVehicleActivity;
import com.example.carpoolbuddy.vehicle.VehiclesInfoActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button profileBtn;
    private TextView displayText;

    private FirebaseUser currUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileBtn = findViewById(R.id.profileBtn);
        displayText = findViewById(R.id.displayText);

        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();

        displayText.setText("Hello " + currUser.getDisplayName());
    }

    public void goToProfile(View v) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToAddVehicle(View v) {
        Intent intent = new Intent(this, AddVehicleActivity.class);
        startActivity(intent);
        finish();
    }

    public void seeVehicles(View v) {
        Intent intent = new Intent(this, VehiclesInfoActivity.class);
        startActivity(intent);
        finish();
    }
}