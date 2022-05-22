package com.example.navmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, phone, password;
    private TextView takeToLogin;
    private Button btn;
    private DatabaseReference ref;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ref = FirebaseDatabase.getInstance().getReference();
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.pass);
        btn = (Button) findViewById(R.id.register);
        takeToLogin = (TextView) findViewById(R.id.hyperlink);
        loader = new ProgressDialog(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "name cannot empty", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(phone.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "phone cannot empty", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "password cannot empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    addUser(name.getText().toString(),
                            phone.getText().toString(),
                            password.getText().toString()
                    );
                }
            }
        });

        takeToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    private void addUser(String name, String phn, String pass) {

        loader.setTitle("Creating Account");
        loader.setMessage("Please wait, while we are Creating your Account");
        loader.setCanceledOnTouchOutside(false);
        loader.show();

        HashMap<String, Object> newUser = new HashMap<>();
        newUser.put("name",name);
        newUser.put("phone",phn);
        newUser.put("password", pass);

        ref.child("Users").child(phn).updateChildren(newUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loader.dismiss();
                            Toast.makeText(RegisterActivity.this, "User Added", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        else{
                            loader.dismiss();
                            Toast.makeText(RegisterActivity.this, "Error occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}