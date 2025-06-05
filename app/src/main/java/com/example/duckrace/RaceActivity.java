package com.example.duckrace;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RaceActivity extends AppCompatActivity {

    private static final int MAX_PROGRESS = 1000;
    // Set up 15s
    private static final int RACE_DURATION_MS = 15000;
    // Update every 100ms
    private static final int UPDATE_INTERVAL_MS = 100;

    private ArrayList<Player> players;
    private SeekBar seekBarDuck1, seekBarDuck2, seekBarDuck3, seekBarDuck4;
    private TextView tvRaceStatus;
    private final Handler handler = new Handler();
    private final Random random = new Random();

    private boolean raceFinished = false;
    private int winningDuckId = -1;
    private final Map<Integer, Integer> duckProgress = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        // Get players data
        players = getIntent().getParcelableArrayListExtra("players");

        // Initialize views
        tvRaceStatus = findViewById(R.id.tvRaceStatus);
        seekBarDuck1 = findViewById(R.id.seekBarDuck1);
        seekBarDuck2 = findViewById(R.id.seekBarDuck2);
        seekBarDuck3 = findViewById(R.id.seekBarDuck3);
        seekBarDuck4 = findViewById(R.id.seekBarDuck4);

        // Initialize duck progress tracking
        duckProgress.put(0, 0);
        duckProgress.put(1, 0);
        duckProgress.put(2, 0);
        duckProgress.put(3, 0);

        // Set up the race
        setupRace();

        // Start the race after a short delay
        handler.postDelayed(this::startRace, 1500);
    }

    private void setupRace() {
        // Set the SeekBars to their initial state
        seekBarDuck1.setMax(MAX_PROGRESS);
        seekBarDuck2.setMax(MAX_PROGRESS);
        seekBarDuck3.setMax(MAX_PROGRESS);
        seekBarDuck4.setMax(MAX_PROGRESS);

        seekBarDuck1.setProgress(0);
        seekBarDuck2.setProgress(0);
        seekBarDuck3.setProgress(0);
        seekBarDuck4.setProgress(0);

        // Disable user interaction with SeekBars
        seekBarDuck1.setEnabled(false);
        seekBarDuck2.setEnabled(false);
        seekBarDuck3.setEnabled(false);
        seekBarDuck4.setEnabled(false);
    }

    private void startRace() {
        // Start moving the ducks
        handler.post(raceRunnable);
    }

    private final Runnable raceRunnable = new Runnable() {
        @Override
        public void run() {
            if (raceFinished) return;

            // Move each duck by a random amount
            moveDucks();

            // Check if any duck has crossed the finish line
            checkForWinner();

            // Schedule next update if race is still ongoing
            if (!raceFinished) {
                handler.postDelayed(this, UPDATE_INTERVAL_MS);
            } else {
                showResults();
            }
        }
    };

    private void moveDucks() {
        // Calculate various speeds for ducks to make the race interesting
        int duck1Move = random.nextInt(15) + 5;
        int duck2Move = random.nextInt(15) + 5;
        int duck3Move = random.nextInt(15) + 5;
        int duck4Move = random.nextInt(15) + 5;

        // Update duck positions
        updateDuckProgress(0, duck1Move);
        updateDuckProgress(1, duck2Move);
        updateDuckProgress(2, duck3Move);
        updateDuckProgress(3, duck4Move);
    }

    private void updateDuckProgress(int duckId, int moveAmount) {
        // Get current progress
        int currentProgress = duckProgress.get(duckId);

        // Calculate new progress
        int newProgress = Math.min(currentProgress + moveAmount, MAX_PROGRESS);

        // Update progress tracking
        duckProgress.put(duckId, newProgress);

        // Update UI based on duck ID
        switch (duckId) {
            case 0:
                seekBarDuck1.setProgress(newProgress);
                break;
            case 1:
                seekBarDuck2.setProgress(newProgress);
                break;
            case 2:
                seekBarDuck3.setProgress(newProgress);
                break;
            case 3:
                seekBarDuck4.setProgress(newProgress);
                break;
        }
    }

    private void checkForWinner() {
        // Check if any duck has crossed the finish line
        for (int duckId = 0; duckId < 4; duckId++) {
            if (duckProgress.get(duckId) >= MAX_PROGRESS && !raceFinished) {
                raceFinished = true;
                winningDuckId = duckId;
                break;
            }
        }
    }

    private void showResults() {
        // Find winners - players who bet on the winning duck
        List<Player> winners = new ArrayList<>();
        for (Player player : players) {
            if (player.getSelectedDucks().contains(winningDuckId)) {
                winners.add(player);
            }
        }

        // Start result activity
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("winningDuckId", winningDuckId);
        intent.putParcelableArrayListExtra("winners", new ArrayList<>(winners));
        intent.putParcelableArrayListExtra("players", players);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(raceRunnable);
    }
}
