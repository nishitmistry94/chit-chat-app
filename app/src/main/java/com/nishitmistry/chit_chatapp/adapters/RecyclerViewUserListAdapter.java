package com.nishitmistry.chit_chatapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.nishitmistry.chit_chatapp.Params;
import com.nishitmistry.chit_chatapp.Controller.FirebaseController;

import com.nishitmistry.chit_chatapp.R;
import com.nishitmistry.chit_chatapp.chats;
import com.nishitmistry.chit_chatapp.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewUserListAdapter extends RecyclerView.Adapter<RecyclerViewUserListAdapter.ViewHolder>{

    private final Context context;
    ArrayList<User> userlist;
    public RecyclerViewUserListAdapter(Context context, ArrayList<User> userlist ){
        this.context=context;
        this.userlist=userlist;
    }
    @NonNull
    @Override
    public RecyclerViewUserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewUserListAdapter.ViewHolder holder, int position) {
        User user =userlist.get(position);
        holder.name.setText(user.getName());
        FirebaseController.getFirebaseController().lastMessage(user.getUid(),holder.lastmessage);
        FirebaseController.getFirebaseController().getImageFromFirebase(user.getUid(), context);
        SharedPreferences sharedPreferences= context.getSharedPreferences(Params.PROFILE_PIC_LINKS,Context.MODE_PRIVATE);
        String link =sharedPreferences.getString("pfp_"+user.getUid(),null);

        if(link!=null)
        {
            Picasso.get().load(link).into(holder.pfp);
        }
    }
    @Override
    public int getItemCount() {
        return userlist.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView lastmessage;
        public ImageView pfp;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.name);
            lastmessage=itemView.findViewById(R.id.lastMessage);
            pfp=itemView.findViewById(R.id.pfp);


            LinearLayout linearLayout=itemView.findViewById(R.id.section_username);
            linearLayout.setOnClickListener(view -> {
                int position =getAdapterPosition();
                Log.d("viewholder", "ViewHolder: "+position);
                User user = userlist.get(position);
                Intent intent =new Intent(context, chats.class);
                intent.putExtra("clickUser",user.getUid());
                intent.putExtra("clickName",user.getName());
                context.startActivity(intent);
            });
            pfp.setOnClickListener(view -> showDialog(getAdapterPosition()));
        }

    }

    public void showDialog(int position){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_userpfpf);
        dialog.show();
        ImageView imageView=dialog.findViewById(R.id.dialog_pfp);
        SharedPreferences sharedPreferences= context.getSharedPreferences(Params.PROFILE_PIC_LINKS,Context.MODE_PRIVATE);
        String link =sharedPreferences.getString("pfp_"+userlist.get(position).getUid(),null);
        if(link!=null){
            Picasso.get().load(link).into(imageView);
        }
        else{
            imageView.setImageResource(R.drawable.profile_image);
        }
    }
}

