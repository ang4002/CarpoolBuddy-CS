package com.example.carpoolbuddy.AuthActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carpoolbuddy.MainActivity;
import com.example.carpoolbuddy.Models.User;
import com.example.carpoolbuddy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText emailField;
    private EditText passwordField;
    private EditText nameField;
    private Spinner roleSpinner;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);
        roleSpinner = findViewById(R.id.roleSpinner);
        nameField = findViewById(R.id.nameField);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
        roleSpinner.setOnItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public void updateUI(AuthResult result) {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void signUp(View v) {
        System.out.print("Sign up");
        String emailString = emailField.getText().toString();
        String passwordString = passwordField.getText().toString();

        if(!emailString.isEmpty() && !passwordString.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();

                        String name = nameField.getText().toString();
                        String email = emailField.getText().toString();
                        String role = roleSpinner.getSelectedItem().toString();
                        String id = user.getUid();

                        User newUser = new User(name, email, role, id);
                        firestore.collection("users").document(id).set(newUser);
                        Log.d("SIGN UP", "Sign up successful");
                        updateUI(task.getResult());
                    } else {
                        Log.d("SIGN UP", "An error occurred", task.getException());
                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(SignUpActivity.this, "One or more fields are empty.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}