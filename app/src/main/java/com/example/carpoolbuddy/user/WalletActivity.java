package com.example.carpoolbuddy.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carpoolbuddy.MainActivity;
import com.example.carpoolbuddy.R;
import com.example.carpoolbuddy.models.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity allows users to view their wallet, including their balance and their lagosBalance.
 *
 * @author Alvin Ng
 * @version 0.1
 */

public class WalletActivity extends AppCompatActivity {
    private TextView balance;
    private TextView lagosBalance;
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private FirebaseFirestore firestore;
    private User currUserObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        balance = findViewById(R.id.balance);
        lagosBalance = findViewById(R.id.lagosBalance);

        getData();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * This method fetches the user's data from firebase and displays it.
     */
    public void getData() {
        firestore.collection("users").document(currUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot ds = task.getResult();
                    currUserObject = ds.toObject(User.class);

                    balance.setText(String.valueOf(currUserObject.getBalance()));
                    lagosBalance.setText(String.valueOf(currUserObject.getLagosBalance()));
                } else {
                    Toast.makeText(WalletActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This method combines the user's balance with their lagosBalance, and updates firebase accordingly.
     *
     * @param v the object from the xml file.
     */
    public void topUp(View v) {
        if(currUserObject.getLagosBalance() == 0) {
            Toast.makeText(WalletActivity.this, "You have no lagos coins!", Toast.LENGTH_SHORT).show();
            return;
        }

        balance.setText(String.valueOf(currUserObject.getBalance() + currUserObject.getLagosBalance()));
        lagosBalance.setText("0");
        firestore.collection("users").document(currUser.getUid()).update("lagosBalance", 0);
        firestore.collection("users").document(currUser.getUid()).update("balance", FieldValue.increment(currUserObject.getLagosBalance()));
        Toast.makeText(WalletActivity.this, "Successfully topped up.", Toast.LENGTH_SHORT).show();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}