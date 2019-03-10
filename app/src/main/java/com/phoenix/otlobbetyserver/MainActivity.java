package com.phoenix.otlobbetyserver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phoenix.otlobbetyserver.Common.Common;
import com.phoenix.otlobbetyserver.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity {

    MaterialEditText edtPhone,edtPassword;
    FButton btnSignIn;

    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Firebase

        db = FirebaseDatabase.getInstance();
        users = db.getReference("User");

        btnSignIn = (FButton)findViewById(R.id.signIn);
        edtPhone = (MaterialEditText) findViewById(R.id.phoneNumber);
        edtPassword = (MaterialEditText)findViewById(R.id.password);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser(edtPhone.getText().toString(),edtPassword.getText().toString());

            }
        });

    }

    private void signInUser(String phone, String password) {

        if (Common.isConnectedToInternet(getBaseContext())) {

            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please Waiting...");
            mDialog.show();

            final String localPhone = phone;
            final String localPassword = password;

            users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(localPhone).exists())
                    {
                        mDialog.dismiss();
                        User user  = dataSnapshot.child(localPhone).getValue(User.class);
                        user.setPhone(localPhone);
                        if (Boolean.parseBoolean(user.getIsStaff()))
                        {
                            if(user.getPassword().equals(localPassword)){
                                Intent login = new Intent(MainActivity.this,Home.class);
                                Common.currentUser = user;
                                startActivity(login);
                                finish();
                            }else {
                                Toast.makeText(MainActivity.this, "Wrong Password !!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "Please login with Staff Account", Toast.LENGTH_SHORT).show();
                        }
                    }else
                    {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "User not exist in Database", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
                else {
                    Toast.makeText(this, "Please check your connection !!", Toast.LENGTH_SHORT).show();
                    return ;
    }
    }
}

