package com.example.duckrace;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ImageView ivWinningDuck;
    private TextView tvWinnerAnnouncement;
    private MaterialButton btnPlayAgain;

    private int winningDuckId;
    private ArrayList<Player> winners;
    private ArrayList<Player> allPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Get data from intent
        winningDuckId = getIntent().getIntExtra("winningDuckId", -1);
        winners = getIntent().getParcelableArrayListExtra("winners");
        allPlayers = getIntent().getParcelableArrayListExtra("players");

        // Initialize views
        ivWinningDuck = findViewById(R.id.ivWinningDuck);
        tvWinnerAnnouncement = findViewById(R.id.tvWinnerAnnouncement);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);

        // Display results
        displayResults();

        // Play again button click handler
        btnPlayAgain.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlayerSelectionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void displayResults() {
        // Set the winning duck image
        switch (winningDuckId) {
            case 0:
                ivWinningDuck.setImageResource(R.drawable.duck1);
                break;
            case 1:
                ivWinningDuck.setImageResource(R.drawable.duck2);
                break;
            case 2:
                ivWinningDuck.setImageResource(R.drawable.duck3);
                break;
            case 3:
                ivWinningDuck.setImageResource(R.drawable.duck4);
                break;
        }

        // Create winner announcement message
        StringBuilder message = new StringBuilder();

        if (winners != null && !winners.isEmpty()) {
            if (winners.size() == 1) {
                // Single winner
                message.append(getString(R.string.winner_announcement, winners.get(0).getName()));
            } else {
                // Multiple winners
                message.append(winners.get(0).getName());

                for (int i = 1; i < winners.size() - 1; i++) {
                    message.append(", ").append(winners.get(i).getName());
                }

                if (winners.size() > 1) {
                    message.append(" and ").append(winners.get(winners.size() - 1).getName());
                }

                message.append(" won the race!");
            }
        } else {
            // No winners
            message.append("No player bet on the winning duck!");
        }

        tvWinnerAnnouncement.setText(message.toString());
    }
}
