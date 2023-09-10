package com.nishitmistry.chit_chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nishitmistry.chit_chatapp.Controller.FirebaseController;
import com.nishitmistry.chit_chatapp.adapters.RecyclerViewChatsAdapter;
import com.nishitmistry.chit_chatapp.model.Messages;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;

public class chats extends AppCompatActivity implements RecyclerViewChatsAdapter.onChatLongClickListener {

    private ImageView sentButton;
    String TAG= chats.class.getName();
    private FirebaseController firebaseController;
    private EditText textField;
    ArrayList<Messages> messagesArrayList;
    RecyclerView recyclerViewChats;
    RecyclerViewChatsAdapter recyclerViewChatsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(TAG);
        firebaseController=FirebaseController.getFirebaseController();
        setContentView(R.layout.activity_chats);
        sentButton=findViewById(R.id.sent_button);
        textField=findViewById(R.id.text_field);
        messagesArrayList=new ArrayList<>();
        Intent intent=getIntent();
        String name=intent.getStringExtra("clickName");
        String recipient=intent.getStringExtra("clickUser");
        getSupportActionBar().setTitle(name);

        String chatRoomRecipient =firebaseController.getAuth().getCurrentUser().getUid().concat(recipient);
        String chatRoom =recipient.concat(firebaseController.getAuth().getCurrentUser().getUid());
        recyclerViewChats=findViewById(R.id.recyclerViewChats);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));
        sentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textField.getText().length()>0) {
                    Formatter format = new Formatter();
                    Calendar calender = Calendar.getInstance();
                    format = new Formatter();
                    format.format("%tl:%tM", calender,calender);
                    Messages messages = new Messages(firebaseController.getAuth().getCurrentUser().getUid(), textField.getText().toString(),format.toString());
                    firebaseController.getDatabaseReference().child("chats").child(chatRoom).child("messages").push().setValue(messages);
                    firebaseController.getDatabaseReference().child("chats").child(chatRoomRecipient).child("messages").push().setValue(messages);
                    textField.setText("");
                }
        }
        });
        recyclerViewChatsAdapter=new RecyclerViewChatsAdapter(chats.this,messagesArrayList,this);
        recyclerViewChats.setAdapter(recyclerViewChatsAdapter);
        firebaseController.getDatabaseReference()
                .child("chats").child(chatRoom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Messages messages= dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                recyclerViewChatsAdapter.notifyDataSetChanged();
                recyclerViewChats.scrollToPosition(recyclerViewChatsAdapter.getItemCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerViewChats.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v,
                                       int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    recyclerViewChats.scrollBy(0, oldBottom - bottom);
                }
            }
        });


    }


    @Override
    public void onChatLongClick(int position, View v) {

    }
}