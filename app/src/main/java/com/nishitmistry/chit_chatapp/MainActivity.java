package com.nishitmistry.chit_chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nishitmistry.chit_chatapp.Controller.FirebaseController;
import com.nishitmistry.chit_chatapp.adapters.RecyclerViewUserListAdapter;
import com.nishitmistry.chit_chatapp.model.User;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ArrayList<User> userList;

    RecyclerView recyclerView;
    RecyclerViewUserListAdapter recyclerViewUserListAdapter;

    private FirebaseController firebaseController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseController=FirebaseController.getFirebaseController();
        setContentView(R.layout.activity_main);
        userList =new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseController.getDatabaseReference()
                .child("Users")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot postSnapshot:snapshot.getChildren()){
                    User currentUser=postSnapshot.getValue(User.class);
                    if(!currentUser.getUid().equals(firebaseController.getAuth().getCurrentUser().getUid()))
                    {
                        userList.add(currentUser);
                    }
                }
                recyclerViewUserListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerViewUserListAdapter =new RecyclerViewUserListAdapter(MainActivity.this,userList);
        recyclerView.setAdapter(recyclerViewUserListAdapter);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.menuLogout:
                FirebaseAuth
                        .getInstance().signOut();
                intent =new Intent(MainActivity.this,login.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.menuHelp:
                Toast.makeText(this, "help on the way", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuSettings:
                intent =new Intent(MainActivity.this,AccountSettings.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewUserListAdapter.notifyDataSetChanged();
    }

}
