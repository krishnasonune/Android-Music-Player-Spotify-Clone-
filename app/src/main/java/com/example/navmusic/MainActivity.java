package com.example.navmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.navmusic.Models.Users;
import com.example.navmusic.Prevalent.prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    
    private DatabaseReference ref;
    private ProgressDialog loader;
    private ImageView img;
    private TextView noIntText;
    private LottieAnimationView lottieAnimationView1, lottieAnimationView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains);

        lottieAnimationView1 = (LottieAnimationView) findViewById(R.id.animationView);
        lottieAnimationView2 = (LottieAnimationView) findViewById(R.id.internet_conn);
        img = (ImageView) findViewById(R.id.appName);
        noIntText = (TextView) findViewById(R.id.no_int_text);

        Dexter.withContext(this)
                .withPermission(Manifest.permission.INTERNET)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        System.exit(0);
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


        if (!isConnected()) {
            img.setVisibility(View.INVISIBLE);
            lottieAnimationView2.setVisibility(View.VISIBLE);
            noIntText.setVisibility(View.VISIBLE);
        }
        else {
            img.setVisibility(View.VISIBLE);
            lottieAnimationView1.setVisibility(View.VISIBLE);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    String phn = Paper.book().read(prevalent.phn);
                    String pass = Paper.book().read(prevalent.pass);

                    if (TextUtils.isEmpty(phn) || TextUtils.isEmpty(pass)) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        validateUser(phn, pass);
                    }

                }
            }, 5500);
        }

          loader = new ProgressDialog(this);
          Paper.init(this);
        
          



    }

    private void validateUser(String phn, String pass) {
        loader.setTitle("Logging In");
        loader.setMessage("Please wait, while we are Checking your credentials");
        loader.setCanceledOnTouchOutside(false);
        loader.show();

        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(phn).exists()) {
                    Users userData = snapshot.child(phn).getValue(Users.class);
                    if ( userData.getPhone().equals(phn) ) {
                        if ( userData.getPassword().equals(pass) ) {
                            loader.dismiss();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            prevalent.userName = userData.getName();
                            prevalent.Phone = userData.getPhone();
                            startActivity(intent);
                        }
                        else{
                            loader.dismiss();
                            Toast.makeText(MainActivity.this, "wrong pass", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
                else {
                    loader.dismiss();
                    Toast.makeText(MainActivity.this, "account not exist", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }


}