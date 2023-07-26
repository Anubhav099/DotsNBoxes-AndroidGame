package com.example.anubhav.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.anubhav.R;
import com.example.anubhav.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {
    private int[] scores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGameBinding binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String[] colors = getResources().getStringArray(R.array.playerColors);
        binding.gameCanvas.setPlayers(colors);
        scores = new int[2];
        binding.gameCanvas.setListener(new GameCanvas.CanvasListener() {
            @Override
            public void onSquareAdded(int player) {
                scores[player]++;
            }
            @Override
            public void onGridCompleted() {
                showResults();
            }
        });
    }

    public void showResults() {

        int score1 = scores[0];
        int score2 = scores[1];

        Intent intent = new Intent(GameActivity.this, ResultsActivity.class);
        intent.putExtra("score1", score1);
        intent.putExtra("score2", score2);
        startActivity(intent);
    }
}
