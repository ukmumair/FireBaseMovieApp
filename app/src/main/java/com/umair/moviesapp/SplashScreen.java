package com.umair.moviesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1500;
    FirebaseDatabase db;
    DatabaseReference ver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        db = FirebaseDatabase.getInstance();
        ver = db.getReference("App Version");
        ver.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String version = Objects.requireNonNull(snapshot.child("latest").getValue()).toString();
                final String dlink = Objects.requireNonNull(snapshot.child("dlink").getValue()).toString();
                if (!getVersionInfo().equals(version))
                {
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            Intent UpdateIntent = new Intent(SplashScreen.this, UDPage.class);
                            UpdateIntent.putExtra("DLINK",dlink);
                            SplashScreen.this.startActivity(UpdateIntent);
                            SplashScreen.this.finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                }
                else
                {
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                            SplashScreen.this.startActivity(mainIntent);
                            SplashScreen.this.finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getVersionInfo()
    {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        assert packageInfo != null;
        return packageInfo.versionName;
    }

}