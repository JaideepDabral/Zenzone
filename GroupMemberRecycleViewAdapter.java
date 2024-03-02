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


import com.example.zenzone.Models.Members;
import com.example.zenzone.Models.Users;
import com.example.zenzone.chat.ChatActivityIndividual;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GroupMemberRecycleViewAdapter extends RecyclerView.Adapter<GroupMemberRecycleViewAdapter.SingleItemRowHolder> {

    private ArrayList<Users> itemsList;
//    private ArrayList<String> membersArrayList;
//    private ArrayList<Members> membersArrayList1;
//
//    private ArrayList<String> membersKeyArrayList;


    SharedPreferences preferences;
    private Activity mContext;
    DatabaseReference databaseReference;
    String group_id;
    public GroupMemberRecycleViewAdapter(Activity context, ArrayList<Users> itemsList, final String group_id) {

        this.mContext = context;

        this.itemsList = itemsList;
//        membersArrayList = new ArrayList<>();
//        membersKeyArrayList = new ArrayList<>();
//        membersArrayList1 = new ArrayList<>();
        this.group_id=group_id;
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.DB);
        preferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
//        databaseReference.child(Constants.members).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                membersArrayList.clear();
//                membersKeyArrayList.clear();
//                membersArrayList1.clear();
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    Members members = dataSnapshot1.getValue(Members.class);
//                    if(members.getGroup_id().equalsIgnoreCase(group_id)){
//                        membersArrayList1.add(members);
//                        membersArrayList.add(members.getUserid());
//                        membersKeyArrayList.add(dataSnapshot1.getKey());
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custum_list_user_inside_group, null);
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
//        holder.btnChat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(mContext, ChatActivityIndividual.class);
//                intent.putExtra("fuserid",user.getUserId());
//                mContext.startActivity(intent);
//            }
//        });
//if(membersArrayList.contains(banner.getUserid())){
//holder.btnAdd.setVisibility(View.GONE);
//holder.tvUserStatus.setVisibility(View.GONE);
//    holder.btnRemove.setVisibility(View.VISIBLE);
//}else {
//    holder.tvUserStatus.setText(membersArrayList.get(i));
//    holder.btnAdd.setVisibility(View.VISIBLE);
//    holder.btnRemove.setVisibility(View.GONE);
//}


        databaseReference.child(Constant.group_members).child(group_id+user.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Members members=dataSnapshot.getValue(Members.class);


                    if(members!=null){
                        if(members.isGroup()){
                            Log.i("USERS", "onDataChange: "+members.getUserid());
                            holder.tvUserStatus.setText(members.getStatus());
                            holder.btnRemove.setVisibility(View.VISIBLE);
                            holder.btnAdd.setVisibility(View.GONE);
                            holder.tvUserStatus.setVisibility(View.VISIBLE);
                            // holder.btnChat.setVisibility(View.VISIBLE);
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
            members.setGroup_id(group_id);
            members.setUserid(user.getUserId());
            members.setRegistered_on(Constant.getDate());
            members.setStatus("pending");
            members.setKey(group_id+user.getUserId());
            members.setGroup(true);
            databaseReference.child(Constant.group_members).child(group_id+user.getUserId()).setValue(members);
                Toast.makeText(mContext, "Member added successfully", Toast.LENGTH_SHORT).show();
                holder.btnRemove.setVisibility(View.VISIBLE);
                holder.btnAdd.setVisibility(View.GONE);
            }
        });
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(Constant.group_members).child(group_id+user.getUserId()).removeValue();
                Toast.makeText(mContext, "Member Removed successfully", Toast.LENGTH_SHORT).show();
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
        protected Button btnAdd, btnRemove;


        public SingleItemRowHolder(View view) {
            super(view);
            this.imgUser = view.findViewById(R.id.imgUser);
            this.tvDisplayName = view.findViewById(R.id.tvDisplayName);
            this.tvDisplayStatus = view.findViewById(R.id.tvDisplayStatus);
            this.btnAdd = view.findViewById(R.id.btnAdd);
            this.btnRemove = view.findViewById(R.id.btnRemove);
            this.tvUserStatus = view.findViewById(R.id.tvUserStatus);
           // this.btnChat = view.findViewById(R.id.btnChat);


        }

    }

    public void updateList(ArrayList<Users> list) {
        this.itemsList = list;
        notifyDataSetChanged();
    }
}