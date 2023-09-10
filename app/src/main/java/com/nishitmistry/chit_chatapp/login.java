package com.nishitmistry.chit_chatapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nishitmistry.chit_chatapp.Controller.FirebaseController;

import java.util.Objects;

public class login extends AppCompatActivity {
 private FirebaseController firebaseController;
 private EditText email;
 private EditText password;
 private Button login;
 private Button signupBut;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseController= FirebaseController.getFirebaseController();
        email=findViewById(R.id.editEmailAddress);
        password=findViewById(R.id.editPassword);
        login =findViewById(R.id.loginButton);
        signupBut =findViewById(R.id.signupButton);

        getSupportActionBar().hide();
        loginAuthenticate();
    }

    private void loginAuthenticate(){
        try {
            if (firebaseController.getAuth().getCurrentUser().getUid() != null) {
                openMainActivity();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        login.setOnClickListener(view -> {
            String sEmail=email.getText().toString();
            String sPassword=password.getText().toString();
            if(sEmail.length()<=0||sPassword.length()<=0)
            {
                Toast.makeText(login.this, "Enter the Credentials", Toast.LENGTH_SHORT).show();
                return;
            }
            firebaseController.getAuth()
                    .signInWithEmailAndPassword(email.getText()
                            .toString(),password.getText().toString())
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {
                            openMainActivity();
                        }
                        else
                        {
                            Toast.makeText(login.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            password.setText("");
                        }
                    });
        });
        signupBut.setOnClickListener(view -> {
            openMainActivity();
        });
    }
    private void openMainActivity(){
        Intent intent = new Intent(login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}