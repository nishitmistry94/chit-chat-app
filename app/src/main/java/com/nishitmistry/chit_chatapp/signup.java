package com.nishitmistry.chit_chatapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.nishitmistry.chit_chatapp.Controller.FirebaseController;
import com.nishitmistry.chit_chatapp.model.User;

public class signup extends AppCompatActivity {
    EditText edtname;
    EditText edtemail;
    EditText edtpassword;
    Button signupButton;
    private FirebaseController firebaseController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseController=FirebaseController.getFirebaseController();
        setContentView(R.layout.activity_signup);

        edtname=findViewById(R.id.editName);
        edtemail=findViewById(R.id.editEmailAddress);
        edtpassword=findViewById(R.id.editPassword);
        signupButton=findViewById(R.id.signupButton);

        getSupportActionBar().hide();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sName =edtname.getText().toString();
                String sEmail=edtemail.getText().toString();
                String sPassword=edtpassword.getText().toString();
                if(sName.length() == 0 || sEmail.length() == 0 || sPassword.length() == 0)
                {
                    Toast.makeText(signup.this, "Enter the Credentials", Toast.LENGTH_SHORT).show();
                    return;
                }
                firebaseController.getAuth()
                        .createUserWithEmailAndPassword(sEmail,sPassword)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            addUserToDatabase(sName,sEmail,sPassword,
                                    firebaseController.getAuth()
                                            .getCurrentUser().getUid().toString());
                            Toast.makeText(signup.this, "Signup Successfull", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(signup.this,login.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
    private void addUserToDatabase(String name,String email,String password,String uid)
    {
        firebaseController.getDatabaseReference()
                .child(Params.FIRE_DB_USER)
                .child(uid).setValue(new User(name,email,password,uid,null));
    }
}