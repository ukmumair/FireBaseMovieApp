package com.umair.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Vholder> {

    Context context;
    List<MoviesModel> list;
    private InterstitialAd mInterstitialAd;

    public SearchAdapter(Context context, List<MoviesModel> list) {
        this.context = context;
        this.list = list;
        MobileAds.initialize(context,
                "ca-app-pub-5059492081286261~7648567378");
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-5059492081286261/2257489956");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
        });
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movies_list,parent,false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {
        holder.textView.setText(list.get(position).getTitle());
        Glide.with(context).load(list.get(position).getThumbnail())
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(list.get(position).getUrl()), "video/*");
                    context.startActivity(Intent.createChooser(intent, "Complete action using"));
                }
            }
        });
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(list.get(position).getUrl()), "video/*");
                    context.startActivity(Intent.createChooser(intent, "Complete action using"));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Vholder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        public Vholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title_tv);
            imageView = itemView.findViewById(R.id.thumbnail_iv);
        }
    }
}
