package com.example.carpoolbuddy.vehicle.maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.carpoolbuddy.R;
import com.example.carpoolbuddy.vehicle.Vehicle;
import com.example.carpoolbuddy.vehicle.VehicleProfileActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static HttpURLConnection connection;
    private GoogleMap map;
    private MarkerOptions pickUpLocation, CIS;
    private FusedLocationProviderClient client;
    private Button updateBtn;
    private Polyline currPolyline;
    private boolean routeIsValid;
    private Vehicle newVehicle;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Get support map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        updateBtn = findViewById(R.id.updateBtn);
        pickUpLocation = new MarkerOptions()
                .position(new LatLng(22.287436087238113, 114.20279855072297))
                .title("Pick-up location").draggable(true);
        CIS = new MarkerOptions()
                .position(new LatLng(22.283532, 114.198249))
                .title("CIS");
        Intent intent = getIntent();
        newVehicle = (Vehicle) intent.getSerializableExtra("newVehicle");
        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        supportMapFragment.getMapAsync(this);
    }

    public void parseData(String responseBody) {
        try {
            JSONObject data = new JSONObject(responseBody);
            JSONArray features = data.getJSONArray("features");
            JSONObject featuresInner = (JSONObject) features.get(0);
            JSONObject geometry = featuresInner.getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");

            List<LatLng> convertedCoords = new ArrayList<>();

            for(int i = 0; i < coordinates.length(); i++) {
                JSONArray currCoords = coordinates.getJSONArray(i);
                double longitude = (double) currCoords.get(0);
                double latitude = (double) currCoords.get(1);
                LatLng currLatLng = new LatLng(latitude, longitude);
                convertedCoords.add(currLatLng);
            }

            if(currPolyline != null) {
                currPolyline.remove();
            }

            currPolyline = map.addPolyline(new PolylineOptions().addAll(convertedCoords));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(pickUpLocation);
        map.addMarker(CIS);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(pickUpLocation.getPosition(), 15));

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(@NonNull Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                String url = getUrl(marker.getPosition());
                makeRequest(url);

                if(routeIsValid) {
                    Toast.makeText(MapsActivity.this, "Route calculated successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MapsActivity.this, "Could not find valid route. Please readjust your marker.", Toast.LENGTH_SHORT).show();
                    currPolyline.remove();
                }
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }
        });

        String url = getUrl(pickUpLocation.getPosition());
        makeRequest(url);
    }

    public void makeRequest(String urlString) {
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();

        //HTTP request to Openrouteservice API
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            //Request setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();

            if(status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
                routeIsValid = false;

                return;
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
                routeIsValid = true;
            }

            //Parse data and draw polyline
            parseData(responseContent.toString());
        } catch(MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrl(LatLng start) {
        String startCoords = start.longitude + ",%20" + start.latitude;

        String url = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf6248a2977d6d4c334651a2dff0987537e5b5&start="
                + startCoords + "&end=114.198249,%2022.283532";
        return url;
    }

    public void addVehicle(View v) {
        String vehicleID = newVehicle.getVehicleID();

        firestore.collection("vehicles").document(vehicleID).set(newVehicle);
        firestore.collection("users").document(currUser.getUid()).update("vehicles", FieldValue.arrayUnion(vehicleID));

        Intent intent = new Intent(this, VehicleProfileActivity.class);
        intent.putExtra("currVehicle", newVehicle);
        startActivity(intent);
        finish();
    }
}