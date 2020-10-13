package com.umair.moviesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    public MoviesAdapter adapter;
    MaterialToolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    List<MoviesModel> list;
    FirebaseDatabase db;
    DatabaseReference moviesRef;
    private AdView mAdView;
    InterstitialAd mInterstitialAd;
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
        MobileAds.initialize(this,
                "ca-app-pub-5059492081286261~7648567378");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5059492081286261/2257489956");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getTitle()==item.getTitle() && !item.getTitle().equals("Search"))
                {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                    else {
                        String category = item.getTitle().toString();
                        intent = new Intent(MainActivity.this, Categories.class);
                        intent.putExtra("CATEGORY", category);
                        intent.putExtra("ORDERBY", "movie_type");
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
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
}
