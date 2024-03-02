package com.example.zenzone;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.zenzone.Models.Group;
import com.example.zenzone.Models.Members;
import com.example.zenzone.chat.ChatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ViewGroupRecycleViewAdapter extends RecyclerView.Adapter<ViewGroupRecycleViewAdapter.SingleItemRowHolder> {

    private ArrayList<Group> itemsList;


    SharedPreferences preferences;
    private Activity mContext;
    private DatabaseReference mDatabaseReference;

    public ViewGroupRecycleViewAdapter(Activity context, ArrayList<Group> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constant.DB);

    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custum_group_details, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {
        final Group banner = itemsList.get(i);
        holder.tvGroupName.setText(banner.getName());
        holder.tvDescription.setText(banner.getDesc());
        holder.tvCreatedOn.setText(banner.getCreated_on());

        Query query = mDatabaseReference.getRef().child(Constant.group_members);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Members members=messageSnapshot.getValue(Members.class);
                    if(members.isGroup()){
                        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equalsIgnoreCase(members.getUserid())&&banner.getKey().equalsIgnoreCase(members.getGroup_id())){
                            holder.btnAddMemeber.setVisibility(View.GONE);
                            holder.btnChat.setVisibility(View.VISIBLE);
                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("gid", banner.getKey());
                intent.putExtra("userid", preferences.getString("userid", ""));
                mContext.startActivity(intent);
            }
        });
        holder.btnAddMemeber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ListUsersForGroup.class);
                intent.putExtra("gid", banner.getKey());
                intent.putExtra("userid", preferences.getString("userid", ""));
                mContext.startActivity(intent);
            }
        });
        holder.btnMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("gid", banner.getKey());
                intent.putExtra("userid", preferences.getString("userid", ""));
                mContext.startActivity(intent);
            }
        });
        holder.btnRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("gid", banner.getKey());
                intent.putExtra("userid", preferences.getString("userid", ""));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {


        //        protected ImageView itemImage;
        protected TextView tvGroupName, tvDescription,tvCreatedOn;
        protected Button btnChat, btnAddMemeber,btnMembers,btnRequests;


        public SingleItemRowHolder(View view) {
            super(view);
            this.tvGroupName = view.findViewById(R.id.tvGroupName);
            this.tvDescription = view.findViewById(R.id.tvDescription);
            this.tvCreatedOn = view.findViewById(R.id.tvCreatedOn);
            this.btnChat = view.findViewById(R.id.btnChat);
            this.btnAddMemeber = view.findViewById(R.id.btnAddMemeber);
            this.btnMembers = view.findViewById(R.id.btnMembers);
            this.btnRequests = view.findViewById(R.id.btnRequests);


        }

    }


}