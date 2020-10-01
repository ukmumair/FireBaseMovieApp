package com.umair.moviesapp;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchResults extends AppCompatActivity {
    MaterialToolbar toolbar;
    RecyclerView recyclerView;
    MoviesModel model;
    List<MoviesModel> list;
    SearchAdapter searchAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference moviesRef;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        toolbar = findViewById(R.id.toolbarSearchView);
        toolbar.setTitle("Search");
        toolbar.setTitleTextColor(Color.WHITE);
        list = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        moviesRef = firebaseDatabase.getReference("Movies");
        recyclerView = findViewById(R.id.recview_search);
        editText = findViewById(R.id.search_editText);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        searchAdapter = new SearchAdapter(this,list);
        recyclerView.setAdapter(searchAdapter);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty())
                {
                    setAdapter(s.toString());
                }
                else
                {
                    list.clear();
                    recyclerView.removeAllViews();
                }
            }
        });
    }
    private void setAdapter(final String toString) {
        list.clear();
        recyclerView.removeAllViews();
        moviesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!toString.isEmpty())
                {
                    boolean Found = false;
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        String title = ds.child("title").getValue(String.class);
                        String thumbnail = ds.child("thumbnail").getValue(String.class);
                        String movie_type = ds.child("movie_type").getValue(String.class);
                        String movie_genre = ds.child("movie_genre").getValue(String.class);
                        String url = ds.child("url").getValue(String.class);
                        String download_url = ds.child("download_url").getValue(String.class);
                        if (title.toLowerCase().contains(toString.toLowerCase()))
                        {
                            model = new MoviesModel(thumbnail,title,url,movie_type,movie_genre,download_url);
                            list.add(model);
                            Found = true;
                        }
                    }
                    if (!Found)
                    {
                        Toast.makeText(SearchResults.this, "Movie not found", Toast.LENGTH_SHORT).show();
                    }
                    searchAdapter = new SearchAdapter(SearchResults.this,list);
                    recyclerView.setAdapter(searchAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
