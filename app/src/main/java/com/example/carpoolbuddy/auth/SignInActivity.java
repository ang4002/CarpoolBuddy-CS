package com.example.carpoolbuddy.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carpoolbuddy.MainActivity;
import com.example.carpoolbuddy.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity allows users to sign in using google sign-in or using their email.
 *
 * @author Alvin Ng
 * @version 0.1
 */

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "GoogleActivity";
    public static final int RC_SIGN_IN = 9001;

    private EditText emailField;
    private EditText passwordField;
    private TextView goSignUpBtn;
    private SignInButton googleSignInBtn;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseFirestore firestore;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);
        googleSignInBtn = findViewById(R.id.googleSignInBtn);
        goSignUpBtn = findViewById(R.id.goSignUpBtn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(SignInActivity.this, gso);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();


        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        goSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * This method takes the email and password input by the user and brings them to the MainActivity if there is a
     * matching user on firebase.
     *
     * @param v the object of the xml file.
     * @throws Exception if the sign in is unsuccessful (e.g. if the user does not exist)
     */

    public void signIn(View v) {
        String emailString = emailField.getText().toString();
        String passwordString = passwordField.getText().toString();

        if(!emailString.isEmpty() && !passwordString.isEmpty()) {
            mAuth.signInWithEmailAndPassword(emailString, passwordString)
                    .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(SignInActivity.this, "One or more fields are empty.", Toast.LENGTH_SHORT).show();
        }
    }

    public void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(SignInActivity.this, task.getException().getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This method takes in the user's idToken, and signs them in using the credential created using said token
     * that will allow firebase to authenticate the user. If the user is logging in for the first time, their name,
     * email, and id will be stored in an intent, and they will be sent to the CompleteSignUp activity. Otherwise,
     * they will be instantly sent to the MainActivity.
     *
     * @param idToken an ID token used to create a firebase authentication credential
     * @throws Exception if the sign in is unsuccessful (e.g. if the user does not exist)
     */
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            AuthResult result = task.getResult();
                            Intent intent;

                            if(!user.getEmail().endsWith(".cis.edu.hk")) {
                                Toast.makeText(SignInActivity.this, "You are not part of CIS!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // If the user is new, send them to the CompleteSignUpActivity class
                            if(result.getAdditionalUserInfo().isNewUser()) {
                                intent = new Intent(SignInActivity.this, CompleteSignUpActivity.class);
                                Bundle extras = new Bundle();

                                extras.putString("name", user.getDisplayName());
                                extras.putString("email", user.getEmail());
                                extras.putString("id", user.getUid());
                                intent.putExtras(extras);
                                startActivity(intent);
                            } else {
                                intent = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}