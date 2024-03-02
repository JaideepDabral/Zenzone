package com.example.zenzone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.zenzone.Models.Posts;
import com.example.zenzone.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {
    ImageView Profile;
    EditText edtUsername, edtFullName, edtGender, edtDOB;
    MaterialButton btnNext;

    FirebaseDatabase database;
    DatabaseReference myRef;
    private static final int POST_IMAGE_REQUEST_CODE = 1;
    FirebaseAuth mAuth;
    StorageReference mStorageRef;
    FirebaseStorage storage;
    RadioButton rdbPrivate,rdbPublic;
    boolean isPrivate=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);
        init();
        database = FirebaseDatabase.getInstance();
        myRef =  database.getReference(Constant.DB);
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference().child("post_images/");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, POST_IMAGE_REQUEST_CODE);
            }
        });
        rdbPrivate = findViewById(R.id.rdbPrivate);
        rdbPublic = findViewById(R.id.rdbPublic);
        rdbPrivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isPrivate=true;
                }
            }
        });
        rdbPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    isPrivate=false;
                }
            }
        });

    }

    private void init() {
        Profile = (ImageView) findViewById(R.id.profile_image);
        edtFullName = (EditText) findViewById(R.id.edt_fullname);
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtGender = (EditText) findViewById(R.id.edt_gender);
        edtDOB = (EditText) findViewById(R.id.edt_dob);

        edtUsername.setTextColor(Color.WHITE);
        edtFullName.setTextColor(Color.WHITE);
        edtGender.setTextColor(Color.WHITE);
        edtDOB.setTextColor(Color.WHITE);

        btnNext = (MaterialButton) findViewById(R.id.Next_btn);
    }

    private void updateProfile() {
        String username = edtUsername.getText().toString();
        String fullname = edtFullName.getText().toString();
        String email = getIntent().getStringExtra("email").toString().trim();
        String pass = getIntent().getStringExtra("pass").toString().trim();
        String gender = edtGender.getText().toString();
        String dob = edtDOB.getText().toString();
if(imageUri!=null){
    UploadTask uploadTask = mStorageRef.putFile(imageUri);

    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
            if (task.isSuccessful()) {
                Task<Uri> DownloaduriTask = mStorageRef.getDownloadUrl();
                DownloaduriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (DownloaduriTask.isSuccessful()) {
                            String key=myRef.child("posts")
                                    .push().getKey();
                            Users users = new Users(username, fullname, email, pass, gender, dob,DownloaduriTask.getResult().toString(),isPrivate);
                            myRef.child("Users").child(FirebaseAuth.getInstance().getUid()).setValue(users)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                                                finishAffinity();
                                            } else {
                                                Toast.makeText(ProfileActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                    }
                });
            }
        }
    });

}else {
    Toast.makeText(this, "Please select profile pic", Toast.LENGTH_SHORT).show();
}
       

    }
    Uri imageUri;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == POST_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data.getData() != null) {

            imageUri = data.getData();
            Profile.setImageURI(imageUri);




        }
    }
}