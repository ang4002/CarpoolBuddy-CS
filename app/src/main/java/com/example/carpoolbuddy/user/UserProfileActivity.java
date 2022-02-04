package com.example.carpoolbuddy.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.carpoolbuddy.MainActivity;
import com.example.carpoolbuddy.R;
import com.example.carpoolbuddy.auth.SignInActivity;
import com.example.carpoolbuddy.models.Alumni;
import com.example.carpoolbuddy.models.Student;
import com.example.carpoolbuddy.models.Teacher;
import com.example.carpoolbuddy.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private FirebaseFirestore firestore;

    private Button signOutBtn;
    private EditText emailField;
    private EditText nameField;
    private EditText infoField;
    private EditText parentUIDsField;
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
        infoField = findViewById(R.id.infoField);
        roleSpinner = findViewById(R.id.roleSpinner);
        parentUIDsField = findViewById(R.id.parentUIDsField);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
        roleSpinner.setOnItemSelectedListener(this);

        getData();

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String role = roleSpinner.getItemAtPosition(position).toString();

                if(role.equalsIgnoreCase("teacher")) {
                    infoField.setHint("In-school title");
                    infoField.setText("");
                    parentUIDsField.setVisibility(View.GONE);
                } else if(role.equalsIgnoreCase("parent")) {
                    infoField.setHint("Children UIDs");
                    parentUIDsField.setVisibility(View.GONE);
                } else if(role.equalsIgnoreCase("alumni")) {
                    infoField.setHint("Graduation year");
                    parentUIDsField.setVisibility(View.GONE);
                } else {
                    infoField.setHint("Graduation year");
                    parentUIDsField.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void getData() {
        firestore.collection("users").document(currUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();

                    User currUserObject = ds.toObject(User.class);
                    emailField.setText(currUserObject.getEmail());
                    nameField.setText(currUserObject.getName());
                    String userType = currUserObject.getUserType();

                    if(userType.equalsIgnoreCase("alumni")) {
                        Alumni alumniObject = ds.toObject(Alumni.class);
                        infoField.setText(alumniObject.getGraduateYear());
                    } else if(userType.equalsIgnoreCase("student")) {
                        Student studentObject = ds.toObject(Student.class);
                        infoField.setText(studentObject.getGraduatingYear());
                    } else if(userType.equalsIgnoreCase("teacher")) {
                        Teacher teacherObject = ds.toObject(Teacher.class);
                        infoField.setText(teacherObject.getInSchoolTitle());
                    }

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

        //Update user name and user type
        firestore.collection("users").document(currUser.getUid()).update("name", name);
        firestore.collection("users").document(currUser.getUid()).update("userType", role);

        //Update display name
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        currUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        }
                    }
                });

        //Update email
        currUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                }
            }
        });

        Toast.makeText(UserProfileActivity.this, "Profile successfully updated!", Toast.LENGTH_SHORT).show();
    }

    public void signOut(View v) {
        Intent intent = new Intent(this, SignInActivity.class);
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