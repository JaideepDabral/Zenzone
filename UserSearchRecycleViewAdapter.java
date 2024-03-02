package com.example.zenzone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.zenzone.Fragments.ViewPostsCurated;
import com.example.zenzone.Fragments.ViewPostsPrivate;
import com.example.zenzone.Models.Members;
import com.example.zenzone.Models.Users;
import com.example.zenzone.chat.ChatActivityIndividual;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class UserSearchRecycleViewAdapter extends RecyclerView.Adapter<UserSearchRecycleViewAdapter.SingleItemRowHolder> {

    private ArrayList<Users> itemsList;
//    private ArrayList<String> membersArrayList;
//    private ArrayList<Members> membersArrayList1;
//
//    private ArrayList<String> membersKeyArrayList;


    SharedPreferences preferences;
    private Activity mContext;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    public UserSearchRecycleViewAdapter(Activity context, ArrayList<Users> itemsList) {

        this.mContext = context;

        this.itemsList = itemsList;
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.DB);
        preferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        firebaseAuth=FirebaseAuth.getInstance();

    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custum_list_user, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {

        final Users user = itemsList.get(i);

        holder.tvDisplayName.setText(user.getFullname());

        holder.tvDisplayStatus.setText("Gender: "+user.getGender());
        Picasso.get().load(user.getProfile()).into(holder.imgUser);
        holder.btnAdd.setText("Send friend request");
        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, ChatActivityIndividual.class);
                intent.putExtra("fuserid",user.getUserId());
                mContext.startActivity(intent);
            }
        });
        holder.imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, ViewPostsPrivate.class);
                intent.putExtra("fuserid",user.getUserId());
                mContext.startActivity(intent);
            }
        });

//if(membersArrayList.contains(banner.getUserid())){
//holder.btnAdd.setVisibility(View.GONE);
//holder.tvUserStatus.setVisibility(View.GONE);
//    holder.btnRemove.setVisibility(View.VISIBLE);
//}else {
//    holder.tvUserStatus.setText(membersArrayList.get(i));
//    holder.btnAdd.setVisibility(View.VISIBLE);
//    holder.btnRemove.setVisibility(View.GONE);
//}
        databaseReference.child(Constant.members).child(firebaseAuth.getUid()+user.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    Members members=dataSnapshot.getValue(Members.class);
                    if(members!=null){
                        if(!members.isGroup()){

                                Log.i("USERS", "onDataChange: "+members.getUserid());
                                holder.tvUserStatus.setText(members.getStatus());
                                if(members.getStatus().equalsIgnoreCase("pending")){
                                    holder.btnChat.setVisibility(View.GONE);
                                }
                                else {
                                    holder.btnChat.setVisibility(View.VISIBLE);
                                }
                                holder.btnRemove.setVisibility(View.VISIBLE);
                                holder.btnAdd.setVisibility(View.GONE);
                                holder.tvUserStatus.setVisibility(View.VISIBLE);





                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.child(Constant.members).child(user.getUserId()+firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Members members=dataSnapshot.getValue(Members.class);
                if(members!=null){
                    if(!members.isGroup()){

                        Log.i("USERS", "onDataChange: "+members.getUserid());
                        holder.tvUserStatus.setText(members.getStatus());
                        if(members.getStatus().equalsIgnoreCase("pending")){
                            holder.btnChat.setVisibility(View.GONE);
                        }
                        else {
                            holder.btnChat.setVisibility(View.VISIBLE);
                        }
                        holder.btnRemove.setVisibility(View.VISIBLE);
                        holder.btnAdd.setVisibility(View.GONE);
                        holder.tvUserStatus.setVisibility(View.VISIBLE);





                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Members members=new Members();
            members.setGroup_id(user.getUserId());
            members.setUserid(firebaseAuth.getUid());
            members.setRegistered_on(Constant.getDate());
            members.setStatus("pending");
            members.setKey(firebaseAuth.getUid()+user.getUserId());
            members.setGroup(false);
            databaseReference.child(Constant.members).child(firebaseAuth.getUid()+user.getUserId()).setValue(members);
                Toast.makeText(mContext, "Friend request sent successfully", Toast.LENGTH_SHORT).show();
                holder.btnRemove.setVisibility(View.VISIBLE);
                holder.btnAdd.setVisibility(View.GONE);
            }
        });
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(Constant.members).child(firebaseAuth.getUid()+user.getUserId()).removeValue();
                Toast.makeText(mContext, "Friend Removed successfully", Toast.LENGTH_SHORT).show();
                holder.btnRemove.setVisibility(View.GONE);
                holder.btnAdd.setVisibility(View.VISIBLE);
                holder.tvUserStatus.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public static class SingleItemRowHolder extends RecyclerView.ViewHolder {

        //        protected TextView tvTitle;
        protected ImageView imgUser;

        //        protected ImageView itemImage;
        protected TextView tvDisplayName, tvDisplayStatus,tvUserStatus;
        protected Button btnAdd, btnRemove,btnChat;


        public SingleItemRowHolder(View view) {
            super(view);
            this.imgUser = view.findViewById(R.id.imgUser);
            this.tvDisplayName = view.findViewById(R.id.tvDisplayName);
            this.tvDisplayStatus = view.findViewById(R.id.tvDisplayStatus);
            this.btnAdd = view.findViewById(R.id.btnAdd);
            this.btnRemove = view.findViewById(R.id.btnRemove);
            this.tvUserStatus = view.findViewById(R.id.tvUserStatus);
            this.btnChat = view.findViewById(R.id.btnChat);


        }

    }

    public void updateList(ArrayList<Users> list) {
        this.itemsList = list;
        notifyDataSetChanged();
    }
}