package com.example.carpoolbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.carpoolbuddy.AuthActivity.SignInActivity;
import com.example.carpoolbuddy.AuthActivity.SignUpActivity;
import com.example.carpoolbuddy.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

public class UserProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private FirebaseFirestore firestore;
    private User currUserObject;

    private Button signOutBtn;
    private EditText emailField;
    private EditText passwordField;
    private EditText nameField;
    private Spinner roleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        signOutBtn = findViewById(R.id.signOutBtn);
        emailField = findViewById(R.id.emailField);
        nameField = findViewById(R.id.nameField);
        roleSpinner = findViewById(R.id.roleSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
        roleSpinner.setOnItemSelectedListener(this);

        firestore.collection("users").document(currUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    currUserObject = ds.toObject(User.class);

                    emailField.setText(currUserObject.getEmail());
                    nameField.setText(currUserObject.getName());

                    for(int i = 0; i < roleSpinner.getCount(); i++) {
                        if(roleSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(currUserObject.getUserType())) {
                            roleSpinner.setSelection(i);
                            break;
                        }
                    }
                } else {
                    Toast.makeText(UserProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateProfile(View v) {
        String email = emailField.getText().toString();
        String name = nameField.getText().toString();
        String role = roleSpinner.getSelectedItem().toString();

        currUserObject.setEmail(email);
        currUserObject.setName(name);
        currUserObject.setUserType(role);

        currUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    firestore.collection("users").document(currUserObject.getUuid()).set(currUserObject);

                    emailField.setText(currUserObject.getEmail());
                    nameField.setText(currUserObject.getName());

                    for(int i = 0; i < roleSpinner.getCount(); i++) {
                        if(roleSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(role)) {
                            roleSpinner.setSelection(i);
                            break;
                        }
                    }

                    Toast.makeText(UserProfileActivity.this, "Profile successfully updated!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signOut(View v) {
        mAuth.signOut();
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}