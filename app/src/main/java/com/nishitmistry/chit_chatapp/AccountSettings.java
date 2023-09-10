package com.nishitmistry.chit_chatapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nishitmistry.chit_chatapp.Controller.FirebaseController;
import com.squareup.picasso.Picasso;

public class AccountSettings extends AppCompatActivity {
    private ImageView pfp;
    private FirebaseController firestoreController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        pfp=findViewById(R.id.settingspfp);
        firestoreController =new FirebaseController();
        String uid=firestoreController.getAuth().getCurrentUser().getUid();
        firestoreController.getImageFromFirebase(uid,this);
        SharedPreferences sharedPreferences= getSharedPreferences(Params.PROFILE_PIC_LINKS,MODE_PRIVATE);
        String link=sharedPreferences.getString("pfp_"+uid,null);
        if (link!=null){
            Picasso.get().load(link).into(pfp);
        }
        pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

    }
    public void imageChooser(){
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        launchImageChooser.launch(intent);
    }

    ActivityResultLauncher<Intent> launchImageChooser
            = registerForActivityResult(
            new ActivityResultContracts
                    .StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    if (data != null
                            && data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        firestoreController.addImageToFirestore(selectedImageUri,AccountSettings.this);
                        SharedPreferences sharedPreferences= getSharedPreferences(Params.PROFILE_PIC_LINKS,MODE_PRIVATE);
                        String link=sharedPreferences.getString("pfp_"+firestoreController.getAuth().getCurrentUser().getUid()
                                ,null);
                        if (link!=null){
                            Picasso.get().load(link).into(pfp);
                        }
                    }
                }
            });
}