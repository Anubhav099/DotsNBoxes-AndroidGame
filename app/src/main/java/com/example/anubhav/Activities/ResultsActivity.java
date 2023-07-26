package com.example.anubhav.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.anubhav.databinding.ActivityResultsBinding;

public class ResultsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityResultsBinding binding = ActivityResultsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int score1 = getIntent().getIntExtra("score1", 0);
        int score2 = getIntent().getIntExtra("score2", 0);
        binding.ts1.setText(String.valueOf(score1));
        binding.ts2.setText(String.valueOf(score2));
    }
    public void showMainMenu(View view) {
        startActivity(new Intent(ResultsActivity.this, MenuActivity.class));
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(ResultsActivity.this, MenuActivity.class));
    }
}
