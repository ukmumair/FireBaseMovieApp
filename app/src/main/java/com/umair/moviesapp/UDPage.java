package com.umair.moviesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UDPage extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ud_page);
        db = FirebaseDatabase.getInstance();
        version = db.getReference("App Version");
    }

    public void BtnExit(View view) {
        finish();
    }

    public void BtnUpdate(View view) {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String Dlink = bundle.getString("DLINK");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Dlink));
        startActivity(browserIntent);
    }
}