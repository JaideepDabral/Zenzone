package com.example.zenzone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenzone.Models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ListUsers extends AppCompatActivity {
    RecyclerView recycle_image;


    String TAG = "AP";

    private DatabaseReference mDatabaseReference;


    ArrayList<Users> lstUsers = new ArrayList<>();

    AppCompatEditText txtSearch;
    String name;

    GroupMemberRecycleViewAdapter groupMemberRecycleViewAdapter;
String gid;
SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recycle_image = findViewById(R.id.recycle_image);
        txtSearch = findViewById(R.id.txtSearch);
        gid=getIntent().getStringExtra("gid");
        groupMemberRecycleViewAdapter = new GroupMemberRecycleViewAdapter(ListUsers.this, lstUsers,gid);
        recycle_image.setHasFixedSize(false);
        final LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycle_image.setLayoutManager(linearLayoutManager);
//        recycle_image.addItemDecoration(new CirclePagerIndicatorDecoration());
        recycle_image.setAdapter(groupMemberRecycleViewAdapter);
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
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constant.DB);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Query query = mDatabaseReference.getRef().child(Constant.users);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Users user = messageSnapshot.getValue(Users.class);
                    user.setUserId(messageSnapshot.getKey());
                    if(!user.getUserId().equalsIgnoreCase(FirebaseAuth.getInstance().getUid())){
                      //  double distance=distance(Double.parseDouble(preferences.getString("latitude","28.77")),user.getLatitude(),Double.parseDouble(preferences.getString("longitude","77.22")),user.getLongitude(),0,0);
                     //   distance=distance/1000.0;//in km
                      //  if (distance <= 50) {
                            lstUsers.add(user);
                       // }
                    }
                }
                groupMemberRecycleViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void search(View v) {
        name = txtSearch.getText().toString();
        if (name.length() > 0) {
            filter(name);
        } else {
            Toast.makeText(getBaseContext(), "Please enter name", Toast.LENGTH_SHORT).show();
        }
    }
    void filter(String text){
        ArrayList<Users> temp = new ArrayList();
        for(Users d: lstUsers){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getFullname().contains(text)||d.getUsername().toLowerCase().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        groupMemberRecycleViewAdapter.updateList(temp);
    }
    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     * <p>
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     *
     * @returns Distance in Meters
     */
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

}
