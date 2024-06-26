package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword<forgotpassword> extends AppCompatActivity {

    private EditText mforgotpassword;
    private Button mpasswordrecover;
    private TextView mbacktologin;


    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password3);



        mforgotpassword=findViewById(R.id.forgotpassword);
        mpasswordrecover=findViewById(R.id.passwordrecover);
        mbacktologin=findViewById(R.id.backtologin);

        firebaseAuth=FirebaseAuth.getInstance();

        mbacktologin.setOnClickListener(new View.OnClickListener()
        {
            @Override
                    public void onClick(View v)
            {
                Intent intent = new Intent(ForgotPassword.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mpasswordrecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=mforgotpassword.getText().toString().trim();
                if(mail.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Enter your mail first", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    //we have to send mail

                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Mail Sent",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPassword.this,MainActivity.class));
                            }

                            else
                            {
                                Toast.makeText(getApplicationContext(),"account doesnt exist",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });


    }
}