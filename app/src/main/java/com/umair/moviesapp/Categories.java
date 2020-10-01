package com.umair.moviesapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.FirebaseDatabase;

public class Categories extends AppCompatActivity {
    Bundle bundle;
    String category, order_by;
    MoviesAdapter adapter;
    RecyclerView recyclerViewCategory;
    MaterialToolbar toolbar;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        toolbar = findViewById(R.id.toolBar);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        bundle = getIntent().getExtras();
        assert bundle != null;
        category = bundle.getString("CATEGORY");
        order_by = bundle.getString("ORDERBY");
        toolbar.setTitle(category);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-5059492081286261/7865039855");
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adViewCategories);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerViewCategory = findViewById(R.id.recCategory);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(Categories.this,3,RecyclerView.VERTICAL,false);
        recyclerViewCategory.setLayoutManager(gridLayoutManager);
        FirebaseRecyclerOptions<MoviesModel> options =
                new FirebaseRecyclerOptions.Builder<MoviesModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Movies").orderByChild(order_by).equalTo(category.toLowerCase()),MoviesModel.class)
                        .build();
        adapter = new MoviesAdapter(options,Categories.this,progressBar);
        recyclerViewCategory.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.startListening();
    }

}