package com.example.zenzone;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenzone.Models.Members;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RequestFound extends AppCompatActivity {
    RecyclerView recycle_image;


    String TAG = "AP";

    private DatabaseReference mDatabaseReference;


    ArrayList<Members> lstUsers = new ArrayList<>();
Button btnSearch;
    AppCompatEditText txtSearch;
    String name;

    RequestRecycleViewAdapter userRecycleViewAdapter;
String gid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recycle_image = findViewById(R.id.recycle_image);
        txtSearch=findViewById(R.id.txtSearch);
        btnSearch=findViewById(R.id.btnSearch);
        txtSearch.setVisibility(View.GONE);
        btnSearch.setVisibility(View.GONE);
        userRecycleViewAdapter = new RequestRecycleViewAdapter(RequestFound.this, lstUsers,gid);
        recycle_image.setHasFixedSize(false);
        final LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycle_image.setLayoutManager(linearLayoutManager);
//        recycle_image.addItemDecoration(new CirclePagerIndicatorDecoration());
        recycle_image.setAdapter(userRecycleViewAdapter);
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
//                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constant.DB);

        Query query = mDatabaseReference.getRef().child(Constant.members);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstUsers.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Members user = messageSnapshot.getValue(Members.class);
                    if(user.getUserid().equalsIgnoreCase(FirebaseAuth.getInstance().getUid())){
                        lstUsers.add(user);
                    }

                }
                userRecycleViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    public void search(View v) {
//        name = txtSearch.getText().toString();
//        if (name.length() > 0) {
//            filter(name);
//        } else {
//            Toast.makeText(getBaseContext(), "Please enter name", Toast.LENGTH_SHORT).show();
//        }
//    }
//    void filter(String text){
//        ArrayList<Members> temp = new ArrayList();
//        for(Members d: lstUsers){
//            //or use .equal(text) with you want equal match
//            //use .toLowerCase() for better matches
//            if(d.getName().contains(text)||d.getName().toLowerCase().contains(text)){
//                temp.add(d);
//            }
//        }
//        //update recyclerview
//        userRecycleViewAdapter.updateList(temp);
//    }
}
