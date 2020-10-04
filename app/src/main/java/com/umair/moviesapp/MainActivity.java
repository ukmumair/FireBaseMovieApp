package com.umair.moviesapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    MoviesAdapter adapter;
    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    List<MoviesModel> list;
//    InterstitialAd mInterstitialAd;
    DatabaseReference version;
    FirebaseDatabase db;
    DatabaseReference moviesRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.ToolBar);
        progressBar = findViewById(R.id.pbar);
        list = new ArrayList<>();
        db = FirebaseDatabase.getInstance();
        moviesRef = db.getReference("Movies");
        progressBar.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
//        AdView adView = new AdView(this);
//        adView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId("ca-app-pub-5059492081286261/7865039855");
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//        AdView mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//        MobileAds.initialize(MainActivity.this,
//                "ca-app-pub-5059492081286261~7648567378");
//        mInterstitialAd = new InterstitialAd(MainActivity.this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-5059492081286261/2257489956");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                if (item.getTitle()==item.getTitle() && !item.getTitle().equals("Search"))
                {
//                    if (mInterstitialAd.isLoaded()) {
//                            mInterstitialAd.show();
//                        }
//                    else {
                        String category = item.getTitle().toString();
                        intent = new Intent(MainActivity.this,Categories.class);
                        intent.putExtra("CATEGORY",category);
                        intent.putExtra("ORDERBY","movie_type");
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
//                        }
                }
                else
                {
                        intent = new Intent(MainActivity.this, SearchResults.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });
        recyclerView = findViewById(R.id.recView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        FirebaseRecyclerOptions<MoviesModel> options =
                new FirebaseRecyclerOptions.Builder<MoviesModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Movies"), MoviesModel.class)
                        .build();
        adapter = new MoviesAdapter(options,this,progressBar);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        version = db.getReference("App Version");
        version.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ver = Objects.requireNonNull(snapshot.child("latest").getValue()).toString();
                final String dlink = Objects.requireNonNull(snapshot.child("dlink").getValue()).toString();
                if (!getVersionInfo().equals(ver))
                {
                    ShowDialog(dlink,ver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
        clearApplicationData();
    }
    @SuppressLint("LongLogTag")
    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(Objects.requireNonNull(cache.getParent()));
        if (appDir.exists()) {
            String[] children = appDir.list();
            assert children != null;
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("ERROR", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            assert children != null;
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }

        assert dir != null;
        return dir.delete();
    }
    public void ShowDialog(final String download_url, String version)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setCancelable(false)
                .setMessage("Version " +version + " Update Available \nYou Need To Update The App In Order To Use.")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(download_url));
                        startActivity(browserIntent);
                    }
                }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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

    @Override
    protected void onPause() {
        super.onPause();
        version.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ver = Objects.requireNonNull(snapshot.child("latest").getValue()).toString();
                final String dlink = Objects.requireNonNull(snapshot.child("dlink").getValue()).toString();
                if (!getVersionInfo().equals(ver))
                {
                    ShowDialog(dlink,ver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
