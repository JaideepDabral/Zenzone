package com.example.zenzone;

import android.app.Activity;
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

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;


import com.example.zenzone.Models.Members;
import com.example.zenzone.Models.Users;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RequestRecycleViewAdapter extends RecyclerView.Adapter<RequestRecycleViewAdapter.SingleItemRowHolder> {

    private ArrayList<Members> itemsList;
//    private ArrayList<String> membersArrayList;
//    private ArrayList<Members> membersArrayList1;
//
//    private ArrayList<String> membersKeyArrayList;


    SharedPreferences preferences;
    private Activity mContext;
    Toolbar toolbar;
    DatabaseReference databaseReference;
    String group_id;
    public RequestRecycleViewAdapter(Activity context, ArrayList<Members> itemsList, final String group_id) {

        this.mContext = context;
        this.toolbar = toolbar;
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custum_list_user_requests, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {

        final Members members = itemsList.get(i);


        holder.tvUserStatus.setText(members.getStatus());
        if(!members.isGroup()){
            if(members.getStatus().equalsIgnoreCase("pending")){
                holder.btnAccept.setVisibility(View.VISIBLE);
                holder.btnRemove.setVisibility(View.VISIBLE);
            }else{
                holder.btnRemove.setVisibility(View.VISIBLE);
                holder.btnAccept.setVisibility(View.GONE);
            }

            databaseReference.child(Constant.users)
                    .child(members.getGroup_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Users user=dataSnapshot.getValue(Users.class);
                            if(user!=null){
                                Log.i("USERS", "onDataChange: "+members.getUserid());
                                holder.tvDisplayName.setText(user.getFullname());
                                holder.tvDisplayStatus.setText("Friend request");
                                Picasso.get().load(user.getProfile()).into(holder.imgUser);

                                holder.tvUserStatus.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    members.setStatus("Accepted");
                    databaseReference.child(Constant.members).child(members.getKey()).setValue(members);
                    Toast.makeText(mContext, "Request accepted successfully", Toast.LENGTH_SHORT).show();
                    holder.btnRemove.setVisibility(View.VISIBLE);
                    holder.btnAccept.setVisibility(View.GONE);
                }
            });
            holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child(Constant.members).child(members.getKey()).removeValue();
                    Toast.makeText(mContext, "Request Removed successfully", Toast.LENGTH_SHORT).show();
                    holder.btnRemove.setVisibility(View.GONE);
                    holder.btnAccept.setVisibility(View.VISIBLE);
                    holder.tvUserStatus.setVisibility(View.GONE);
                }
            });

        }else{
            //for group requests
            if(members.getStatus().equalsIgnoreCase("pending")){
                holder.btnAccept.setVisibility(View.VISIBLE);
                holder.btnRemove.setVisibility(View.VISIBLE);
            }else{
                holder.btnRemove.setVisibility(View.VISIBLE);
                holder.btnAccept.setVisibility(View.GONE);
            }

            databaseReference.child(Constant.users)
                    .child(members.getUserid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Users user=dataSnapshot.getValue(Users.class);
                            if(user!=null){
                                Log.i("USERS", "onDataChange: "+members.getUserid());
                                holder.tvDisplayName.setText(user.getFullname());
                                holder.tvDisplayStatus.setText("Group member request");
                                Picasso.get().load(user.getProfile()).into(holder.imgUser);

                                holder.tvUserStatus.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    members.setStatus("Accepted");
                    databaseReference.child(Constant.group_members).child(members.getKey()).setValue(members);
                    Toast.makeText(mContext, "Request accepted successfully", Toast.LENGTH_SHORT).show();
                    holder.btnRemove.setVisibility(View.VISIBLE);
                    holder.btnAccept.setVisibility(View.GONE);
                }
            });
            holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseReference.child(Constant.group_members).child(members.getKey()).removeValue();
                    Toast.makeText(mContext, "Request Removed successfully", Toast.LENGTH_SHORT).show();
                    holder.btnRemove.setVisibility(View.GONE);
                    holder.btnAccept.setVisibility(View.VISIBLE);
                    holder.tvUserStatus.setVisibility(View.GONE);
                }
            });

        }


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
        protected Button btnAccept, btnRemove;


        public SingleItemRowHolder(View view) {
            super(view);
            this.imgUser = view.findViewById(R.id.imgUser);
            this.tvDisplayName = view.findViewById(R.id.tvDisplayName);
            this.tvDisplayStatus = view.findViewById(R.id.tvDisplayStatus);
            this.btnAccept = view.findViewById(R.id.btnAccept);
            this.btnRemove = view.findViewById(R.id.btnRemove);
            this.tvUserStatus = view.findViewById(R.id.tvUserStatus);


        }

    }

    public void updateList(ArrayList<Members> list) {
        this.itemsList = list;
        notifyDataSetChanged();
    }
}