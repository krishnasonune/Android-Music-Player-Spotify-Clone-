package com.example.navmusic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.navmusic.Models.Users;
import com.example.navmusic.Prevalent.prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText phone, password;
    private TextView takeToReg;
    private Button btn;
    private DatabaseReference ref;
    private ProgressDialog loader;
    private CheckBox remeber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        phone = (EditText) findViewById(R.id.lphone);
        password = (EditText) findViewById(R.id.lpass);
        takeToReg = (TextView) findViewById(R.id.registerpg);
        btn = (Button) findViewById(R.id.llogin);
        remeber = (CheckBox) findViewById(R.id.rememberme);
        loader = new ProgressDialog(this);
        Paper.init(this);

        takeToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loader.setTitle("Logging In");
                loader.setMessage("Please wait, while we are Checking your credentials");
                loader.setCanceledOnTouchOutside(false);
                loader.show();

                validateUser(phone.getText().toString(),
                        password.getText().toString()
                        );
            }
        });

    }

    private void validateUser(String phn, String pass) {
        if (remeber.isChecked()) {
            Paper.book().write(prevalent.phn, phn);
            Paper.book().write(prevalent.pass, pass);
        }

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(phn).exists()) {
                    Users userData = snapshot.child(phn).getValue(Users.class);
                    if ( userData.getPhone().equals(phn) ) {
                        if ( userData.getPassword().equals(pass) ) {
                            loader.dismiss();
                            prevalent.Phone = userData.getPhone();
                            prevalent.userName = userData.getName();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else{
                            loader.dismiss();
                            Toast.makeText(LoginActivity.this, "wrong pass", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
                else {
                    loader.dismiss();
                    Toast.makeText(LoginActivity.this, "account not exist", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}