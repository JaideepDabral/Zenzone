package com.example.zenzone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.zenzone.Models.Group;
import com.example.zenzone.Models.Members;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class CreateGroup extends AppCompatActivity {
    TextInputEditText txtGroupName, txtGroupDesc;
    DatabaseReference mDatabaseReference;
    RecyclerView recycle_banner;
    ViewGroupRecycleViewAdapter viewGroupRecycleViewAdapter;
    ArrayList lstGroups = new ArrayList<Group>();
    String search;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        search = getIntent().getStringExtra("search");
        txtGroupName = findViewById(R.id.txtGroupName);
        txtGroupDesc = findViewById(R.id.txtGroupDesc);
        userid = FirebaseAuth.getInstance().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constant.DB);
        recycle_banner = findViewById(R.id.list);
        viewGroupRecycleViewAdapter = new ViewGroupRecycleViewAdapter(CreateGroup.this, lstGroups);
        recycle_banner.setHasFixedSize(false);
        final LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycle_banner.setLayoutManager(linearLayoutManager);
        recycle_banner.setAdapter(viewGroupRecycleViewAdapter);
        mDatabaseReference.child(Constant.group).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstGroups.clear();
                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final Group group = dataSnapshot1.getValue(Group.class);
                    group.setKey(dataSnapshot1.getKey());
                    Log.i("CREATE GROUP", "onDataChange: search" + search);
                    if (group.getAdmin().equalsIgnoreCase(userid)) {
                        lstGroups.add(group);
                    }

                        mDatabaseReference.child(Constant.group_members).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                    Members members = dataSnapshot2.getValue(Members.class);
                                    if(members.isGroup()){
                                        if (group.getKey().equalsIgnoreCase(members.getGroup_id())
                                                && members.getUserid().equalsIgnoreCase(userid)
                                                &&members.getStatus().equalsIgnoreCase("Accepted")) {
                                            if (!lstGroups.contains(group)) {
                                                lstGroups.add(group);
                                            }

                                        }
                                    }

                                }
                                viewGroupRecycleViewAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                viewGroupRecycleViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        mDatabaseReference.child(Constants.members).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                lstGroups.clear();
//                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
//                    Group group=dataSnapshot1.getValue(Group.class);
//                    group.setKey(dataSnapshot1.getKey());
//                    if(group.getAdmin().equalsIgnoreCase(FirebaseAuth.getInstance().getUid())){
//                        lstGroups.add(group);
//                    }
//                }
//                viewGroupRecycleViewAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }

    String group_name, group_desc;

    public void submit(View view) {
        group_name = txtGroupName.getText().toString();
        group_desc = txtGroupDesc.getText().toString();
        if (group_name.length() > 0 || group_desc.length() > 0) {
            Group group = new Group();
            group.setAdmin(FirebaseAuth.getInstance().getUid());
            group.setCreated_on(Constant.getDateTime());
            group.setName(group_name);
            group.setDesc(group_desc);
            group.setCategory(search);

            mDatabaseReference.child(Constant.group).push().setValue(group);
            Toast.makeText(getBaseContext(), "Group Created successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "Please enter details", Toast.LENGTH_SHORT).show();
        }


    }
}