package com.nishitmistry.chit_chatapp.Controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nishitmistry.chit_chatapp.Params;
import com.nishitmistry.chit_chatapp.model.Messages;


import java.util.Objects;

public class FirebaseController {

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;

    private static FirebaseController firebaseController =null;
    public FirebaseController() {
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }
    public static FirebaseController getFirebaseController(){
        if(firebaseController ==null){
            firebaseController =new FirebaseController();
        }
        return firebaseController;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public FirebaseFirestore getFirestore() {
        return firestore;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseStorage getFirebaseStorage() {
        return firebaseStorage;
    }


    public void addImageToFirestore(Uri uri, Context context) {
        final StorageReference reference = firebaseStorage.getReference()
                .child("users").child(auth.getCurrentUser().getUid()).child("profile pic");

        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                databaseReference.child(Params.FIRE_DB_USER).child(auth.getCurrentUser()
                                                .getUid()).child(Params.FIRE_DB_USER_PFP).setValue(uri.toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(context, "Image Uploaded Successfully", Toast.LENGTH_LONG);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Image Upload Failed!!", Toast.LENGTH_LONG);
                                            }
                                        });
                            }
                        });
                    }
                });
    }

    public void getImageFromFirebase(String uid,Context context) {

        databaseReference.child(Params.FIRE_DB_USER).child(uid)
                .child(Params.FIRE_DB_USER_PFP).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String link=null;
                        try {
                            link=snapshot.getValue().toString();
                        }
                        catch (Exception e){

                        }
                        SharedPreferences sharedPreferences= context
                                .getSharedPreferences(Params.PROFILE_PIC_LINKS,Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit =sharedPreferences.edit();
                        edit.putString("pfp_"+uid,link);
                        edit.commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void lastMessage(String currentUser, TextView textView)
    {
        DatabaseReference drefmsg= databaseReference
                .child("chats")
                .child(Objects.requireNonNull(auth.getCurrentUser()).getUid().concat(currentUser));
        Query lastQuery = drefmsg
                .child("messages")
                .orderByKey()
                .limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot messageSnapshot) {
                String last_message="NO MESSAGES ";
                for(DataSnapshot dataSnapshot:messageSnapshot.getChildren()) {
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    assert messages != null;
                    last_message = messages.getMessage();
                }
                textView.setText(last_message);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseController", "lastMessage onCancelled: "+databaseError.getMessage());
            }
        });

    }


}
