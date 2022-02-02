package com.example.carpoolbuddy.vehicle.maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.widget.TextView;

import com.example.carpoolbuddy.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
    private MarkerOptions currLocation, CIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Get support map fragment
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        currLocation = new MarkerOptions()
                .position(new LatLng(114.20279855072297, 22.287436087238113))
                .title("Current location");
        CIS = new MarkerOptions()
                .position(new LatLng(114.198249, 22.283532))
                .title("CIS");
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
                Log.d("latitude", String.valueOf(currLatLng.latitude));
                convertedCoords.add(currLatLng);
                Log.d("convertedCoords", String.valueOf(convertedCoords.get(0).latitude));
            }

            map.addPolyline(new PolylineOptions().addAll(convertedCoords));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        LatLng deez = new LatLng(22.287436087238113, 114.20279855072297);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(deez, 10));
        map.addMarker(currLocation);
        map.addMarker(CIS);

        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();

        //HTTP request to Openrouteservice API
        try {
            URL url = new URL("https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf6248a2977d6d4c334651a2dff0987537e5b5&start=114.20279855072297,%2022.287436087238113&end=114.198249,%2022.283532");
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
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line = reader.readLine()) != null) {
                    responseContent.append(line);
                }
                reader.close();
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

//    public String createUrl() {
//        https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf6248a2977d6d4c334651a2dff0987537e5b5&start=8.681495,49.41461&end=8.687872,49.420318
//    }
}