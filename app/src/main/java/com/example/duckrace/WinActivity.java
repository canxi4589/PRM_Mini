package com.example.duckrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WinActivity extends AppCompatActivity {
    private TextView resultTitle, winnerText, winningsText, balanceText;
    private Button playAgainButton, mainMenuButton;
    private MediaPlayer resultSound;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        prefs = getSharedPreferences("HorseRacingPrefs", MODE_PRIVATE);

        initViews();
        displayResult();
        setupListeners();
    }

    private void initViews() {
        resultTitle = findViewById(R.id.resultTitle);
        winnerText = findViewById(R.id.winnerText);
        winningsText = findViewById(R.id.winningsText);
        balanceText = findViewById(R.id.balanceText);
        playAgainButton = findViewById(R.id.playAgainButton);
        mainMenuButton = findViewById(R.id.mainMenuButton);
    }

    private void displayResult() {
        String winner = getIntent().getStringExtra("winner");
        int winnings = getIntent().getIntExtra("winnings", 0);
        int balance = prefs.getInt("balance", 0);

        winnerText.setText("🏆 " + winner + " 🏆");

        if (winnings > 0) {
            resultTitle.setText("🎉 CHÚC MỪNG! 🎉");
            winningsText.setText("Bạn đã thắng: " + winnings + " VND");
            winningsText.setTextColor(getResources().getColor(android.R.color.holo_green_light));
//            playWinSound();
        } else {
            resultTitle.setText("😔 THẤT BẠI 😔");
            winningsText.setText("Bạn đã thua cuộc");
            winningsText.setTextColor(getResources().getColor(android.R.color.holo_red_light));
//            playLoseSound();
        }

        balanceText.setText("Số dư hiện tại: " + balance + " VND");
    }

    private void setupListeners() {
        playAgainButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RaceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        mainMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

//    private void playWinSound() {
//        try {
//            resultSound = MediaPlayer.create(this, R.raw.win_sound);
//            if (resultSound != null) {
//                resultSound.start();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void playLoseSound() {
//        try {
//            resultSound = MediaPlayer.create(this, R.raw.lose_sound);
//            if (resultSound != null) {
//                resultSound.start();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (resultSound != null) {
            resultSound.release();
            resultSound = null;
        }
    }
}