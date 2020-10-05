package com.umair.moviesapp;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.FirebaseDatabase;

public class Categories extends AppCompatActivity {
    Bundle bundle;
    String category, order_by;
    MoviesAdapter adapter;
    RecyclerView recyclerViewCategory;
    MaterialToolbar toolbar;
    ProgressBar progressBar;
    private AdView adView;
    private InterstitialAd interstitialAd;
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
        AudienceNetworkAds.initialize(this);
        adView = new AdView(this, "799169794203817_799629900824473", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        adContainer.addView(adView);
        adView.loadAd();
        interstitialAd = new InterstitialAd(this, "799169794203817_799180570869406");

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

        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
            }

            @Override
            public void onError(Ad ad, AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
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

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}