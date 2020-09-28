package com.umair.moviesapp;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SearchResults extends AppCompatActivity {
    MaterialToolbar toolbar;
    EditText editText;
    RecyclerView recyclerView;
    MoviesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        toolbar = findViewById(R.id.toolbarSearchView);
        toolbar.setTitle("Search");
        toolbar.setTitleTextColor(Color.WHITE);
        editText = findViewById(R.id.search_editText);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        recyclerView = findViewById(R.id.recview_search);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
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
                SearchMovie(s.toString());
            }
        });
    }
    private void SearchMovie(final String toString) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference movies = database.getReference("Movies");
        if (!toString.equals(""))
        {
            FirebaseRecyclerOptions<MoviesModel> options =
                    new FirebaseRecyclerOptions.Builder<MoviesModel>()
                            .setQuery(movies.orderByChild("title").startAt(toString).endAt(toString+"\uf8ff"), MoviesModel.class)
                            .build();
            adapter = new MoviesAdapter(options,SearchResults.this,null);
            adapter.startListening();
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else
        {
            adapter.stopListening();
        }
    }

}
