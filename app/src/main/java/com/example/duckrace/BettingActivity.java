package com.example.duckrace;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class BettingActivity extends AppCompatActivity {

    private TextView tvPlayerTurn;
    private CheckBox cbDuck1, cbDuck2, cbDuck3, cbDuck4;
    private MaterialButton btnPlaceBet;

    private ArrayList<Player> players;
    private int currentPlayerIndex = 0;
    private boolean allPlayersPlacedBets = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting);

        // Get players from intent
        players = getIntent().getParcelableArrayListExtra("players");
        if (players == null || players.isEmpty()) {
            Toast.makeText(this, "No players found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        tvPlayerTurn = findViewById(R.id.tvPlayerTurn);
        cbDuck1 = findViewById(R.id.cbDuck1);
        cbDuck2 = findViewById(R.id.cbDuck2);
        cbDuck3 = findViewById(R.id.cbDuck3);
        cbDuck4 = findViewById(R.id.cbDuck4);
        btnPlaceBet = findViewById(R.id.btnPlaceBet);

        // Setup first player's turn
        updatePlayerTurnUI();

        // Handle place bet button click
        btnPlaceBet.setOnClickListener(v -> {
            if (validateSelection()) {
                savePlayerBets();
                moveToNextPlayer();
            } else {
                Toast.makeText(this, "Please select at least one duck", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePlayerTurnUI() {
        // Update UI to show current player's turn
        Player currentPlayer = players.get(currentPlayerIndex);
        tvPlayerTurn.setText(getString(R.string.player_turn, currentPlayer.getName()));

        // Clear previous selections
        cbDuck1.setChecked(false);
        cbDuck2.setChecked(false);
        cbDuck3.setChecked(false);
        cbDuck4.setChecked(false);

        // Update button text based on player status
        if (allPlayersPlacedBets) {
            btnPlaceBet.setText(R.string.btn_start_race);
        } else {
            btnPlaceBet.setText(R.string.btn_place_bet);
        }
    }

    private boolean validateSelection() {
        return cbDuck1.isChecked() || cbDuck2.isChecked() || cbDuck3.isChecked() || cbDuck4.isChecked();
    }

    private void savePlayerBets() {
        Player currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.clearSelectedDucks();

        if (cbDuck1.isChecked()) currentPlayer.addSelectedDuck(0);
        if (cbDuck2.isChecked()) currentPlayer.addSelectedDuck(1);
        if (cbDuck3.isChecked()) currentPlayer.addSelectedDuck(2);
        if (cbDuck4.isChecked()) currentPlayer.addSelectedDuck(3);
    }

    private void moveToNextPlayer() {
        // Move to next player
        currentPlayerIndex++;

        // If all players have placed bets, start the race immediately
        if (currentPlayerIndex >= players.size()) {
            startRaceActivity();
            return;
        }

        // Otherwise, update UI for the next player's turn
        updatePlayerTurnUI();
    }

    private void startRaceActivity() {
        Intent intent = new Intent(this, RaceActivity.class);
        intent.putParcelableArrayListExtra("players", players);
        startActivity(intent);
    }
}
