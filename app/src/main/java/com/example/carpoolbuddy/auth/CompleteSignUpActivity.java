package com.example.carpoolbuddy.auth;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carpoolbuddy.MainActivity;
import com.example.carpoolbuddy.R;
import com.example.carpoolbuddy.models.users.User;
import com.google.firebase.firestore.FirebaseFirestore;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * This activity allows users who are signing in using google for the first time to select their role.
 *
 * @author Alvin Ng
 * @version 0.1
 */

public class CompleteSignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Spinner roleSpinner;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_sign_up);

        roleSpinner = findViewById(R.id.roleSpinner);
        firestore = FirebaseFirestore.getInstance();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
        roleSpinner.setOnItemSelectedListener(this);
    }

    /**
     * This method gets the user's input from this activity's intent, creates a user object, and adds it to firebase.
     *
     * @param v the object of the xml file.
     */
    public void completeSignUp(View v) {
        String role = roleSpinner.getSelectedItem().toString();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String name = extras.getString("name");
        String email = extras.getString("email");
        String id = extras.getString("id");

        User newUser = new User(name, email, role, id);
        firestore.collection("users").document(id).set(newUser);

        Intent nextScreen = new Intent(CompleteSignUpActivity.this, MainActivity.class);
        startActivity(nextScreen);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}