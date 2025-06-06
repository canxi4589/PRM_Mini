package com.example.duckrace;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    private ListView horseListView;
    private Button startButton, resetButton, backButton;
    private TextView balanceText;
    private DuckRaceAdapter adapter;
    private List<Duck> horses;
    private SharedPreferences prefs;
    private MediaPlayer raceSound;
    private boolean isRacing = false;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("HorseRacingPrefs", MODE_PRIVATE);

        initViews();
        setupHorses();
        setupListeners();
        updateBalance();
    }

    private void initViews() {
        horseListView = findViewById(R.id.horseListView);
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);
        backButton = findViewById(R.id.backButton);
        balanceText = findViewById(R.id.balanceText);
    }

    private void setupHorses() {
        horses = new ArrayList<>();
        horses.add(new Duck("Thunder", "🐎", 0, 0));
        horses.add(new Duck("Lightning", "🐴", 0, 0));
        horses.add(new Duck("Storm", "🦄", 0, 0));
        horses.add(new Duck("Wind", "🐎", 0, 0));
        horses.add(new Duck("Fire", "🐴", 0, 0));

        adapter = new DuckRaceAdapter(this, horses, prefs);
        horseListView.setAdapter(adapter);
    }

    private void setupListeners() {
        startButton.setOnClickListener(v -> {
            if (!isRacing) {
                startRace();
            }
        });

        resetButton.setOnClickListener(v -> {
            if (!isRacing) {
                resetRace();
            }
        });

        backButton.setOnClickListener(v -> {
            if (!isRacing) {
                finish();
            }
        });
    }

    private void startRace() {
        // Check if any bets were placed
        boolean hasBets = false;
        int totalBetAmount = 0;

        for (Duck horse : horses) {
            if (horse.getBetAmount() > 0) {
                hasBets = true;
                totalBetAmount += horse.getBetAmount();
            }
        }

        if (!hasBets) {
            Toast.makeText(this, "Vui lòng đặt cược trước khi bắt đầu!", Toast.LENGTH_SHORT).show();
            return;
        }

        int currentBalance = prefs.getInt("balance", 0);
        if (totalBetAmount > currentBalance) {
            Toast.makeText(this, "Số dư không đủ!", Toast.LENGTH_SHORT).show();
            return;
        }

        isRacing = true;
        startButton.setEnabled(false);
        resetButton.setEnabled(false);
        backButton.setEnabled(false);

        // Deduct bet amounts from balance
        prefs.edit().putInt("balance", currentBalance - totalBetAmount).apply();
        updateBalance();

//        playRaceSound();
        simulateRace();
    }

    private void simulateRace() {
        Handler handler = new Handler();
        final int[] raceStep = {0};
        final int maxSteps = 100;

        Runnable raceRunnable = new Runnable() {
            @Override
            public void run() {
                if (raceStep[0] < maxSteps) {
                    // Update horse positions
                    for (Duck horse : horses) {
                        int speed = random.nextInt(3) + 1; // Random speed 1-3
                        horse.setPosition(Math.min(100, horse.getPosition() + speed));
                    }

                    adapter.notifyDataSetChanged();
                    raceStep[0]++;

                    // Check for winner
                    Duck winner = null;
                    for (Duck horse : horses) {
                        if (horse.getPosition() >= 100) {
                            winner = horse;
                            break;
                        }
                    }

                    if (winner != null) {
                        finishRace(winner);
                    } else {
                        handler.postDelayed(this, 100);
                    }
                } else {
                    // Time limit reached, find horse with highest position
                    Duck winner = Collections.max(horses,
                            Comparator.comparingInt(Duck::getPosition));
                    finishRace(winner);
                }
            }
        };

        handler.post(raceRunnable);
    }

    private void finishRace(Duck winner) {
        isRacing = false;
        startButton.setEnabled(true);
        resetButton.setEnabled(true);
        backButton.setEnabled(true);

        if (raceSound != null) {
            raceSound.stop();
        }

        // Calculate winnings
        int totalWinnings = 0;
        if (winner.getBetAmount() > 0) {
            totalWinnings = winner.getBetAmount() * 5; // 5x multiplier for winner
        }

        // Update balance
        int currentBalance = prefs.getInt("balance", 0);
        prefs.edit().putInt("balance", currentBalance + totalWinnings).apply();

        // Show result
        Intent resultIntent = new Intent(this, WinActivity.class);
        resultIntent.putExtra("winner", winner.getName());
        resultIntent.putExtra("winnings", totalWinnings);
        startActivity(resultIntent);
    }

    private void resetRace() {
        for (Duck horse : horses) {
            horse.setPosition(0);
            horse.setBetAmount(0);
        }
        adapter.notifyDataSetChanged();
        updateBalance();
    }

    private void updateBalance() {
        int balance = prefs.getInt("balance", 0);
        balanceText.setText("Số dư: " + balance + " VND");
    }

//    private void playRaceSound() {
//        try {
//            raceSound = MediaPlayer.create(this, R.raw.race_sound);
//            if (raceSound != null) {
//                raceSound.start();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (raceSound != null) {
            raceSound.release();
            raceSound = null;
        }
    }
}