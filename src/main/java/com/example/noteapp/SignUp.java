package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText msignupemail, msignuppassword;
    private RelativeLayout msignup;
    private TextView mgotologin;
    private FirebaseAuth firebaseauth;
    private static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        msignupemail = findViewById(R.id.signupemail);
        msignuppassword = findViewById(R.id.signuppassword);
        msignup = findViewById(R.id.signup);
        mgotologin = findViewById(R.id.gotologin);

        firebaseauth = FirebaseAuth.getInstance();

        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, MainActivity.class));
            }
        });

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = msignupemail.getText().toString().trim();
                String password = msignuppassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                    Toast.makeText(getApplicationContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 7) {
                    Toast.makeText(getApplicationContext(), "Minimum 7 characters required", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseauth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Registration Successful");
                                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            } else {
                                Log.e(TAG, "Registration Unsuccessful", task.getException());
                                Toast.makeText(getApplicationContext(), "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser firebaseuser = firebaseauth.getCurrentUser();
        if (firebaseuser != null) {
            firebaseuser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent");
                        Toast.makeText(getApplicationContext(), "Email sent, verify and login again", Toast.LENGTH_SHORT).show();
                        firebaseauth.signOut();
                        finish();
                        startActivity(new Intent(SignUp.this, MainActivity.class));
                    } else {
                        Log.e(TAG, "Failed to send email", task.getException());
                        Toast.makeText(getApplicationContext(), "Failed to send email", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Log.e(TAG, "User is null");
            Toast.makeText(getApplicationContext(), "Fail to send Email", Toast.LENGTH_SHORT).show();
        }
    }
}
