package com.umair.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MoviesAdapter extends FirebaseRecyclerAdapter<MoviesModel , MoviesAdapter.Vholder> {
    private Context context;
    ProgressBar progressBar;
//    private InterstitialAd mInterstitialAd;
    public MoviesAdapter(@NonNull FirebaseRecyclerOptions<MoviesModel> options , Context context , @Nullable  ProgressBar progressBar) {
        super(options);
        this.context = context;
        this.progressBar = progressBar;
//        MobileAds.initialize(context,
//                "ca-app-pub-5059492081286261~7648567378");
//        mInterstitialAd = new InterstitialAd(context);
//        mInterstitialAd.setAdUnitId("ca-app-pub-5059492081286261/2257489956");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener(){
//        });
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull Vholder holder, int position, @NonNull final MoviesModel model) {
        holder.textView.setText(model.getTitle());
        Glide.with(context)
                .load(model.getThumbnail())
                .into(holder.imageView);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (interstitialAd.isAdLoaded()) {
//                    interstitialAd.show();
//                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(model.getUrl()), "video/*");
                    context.startActivity(Intent.createChooser(intent, "Complete action using"));
//                }
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                } else {
//                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(model.getUrl()), "video/*");
                    context.startActivity(Intent.createChooser(intent, "Complete action using"));
//                }
            }
        });
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list,parent,false);
        return new Vholder(view);
    }

    public static class Vholder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;
        public Vholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.thumbnail_iv);
            textView = itemView.findViewById(R.id.title_tv);
        }
    }

}
