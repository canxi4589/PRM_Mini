package com.example.duckrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OptionActivity extends AppCompatActivity {
    private TextView welcomeText, balanceText;
    private Button playButton, addMoneyButton, instructionsButton, logoutButton, pickNumOfPlayers;
    private SharedPreferences prefs;
    private MediaPlayer backgroundMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_option);
        prefs = getSharedPreferences("HorseRacingPrefs", MODE_PRIVATE);
        if (!prefs.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        initViews();
        setupListeners();
        updateUI();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void initViews() {
        welcomeText = findViewById(R.id.welcomeText);
        balanceText = findViewById(R.id.balanceText);
        playButton = findViewById(R.id.playButton);
        addMoneyButton = findViewById(R.id.addMoneyButton);
        instructionsButton = findViewById(R.id.instructionsButton);
        logoutButton = findViewById(R.id.logoutButton);
        pickNumOfPlayers = findViewById(R.id.pickNumOfPlayers);
    }

    private void setupListeners() {
        playButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });

//        addMoneyButton.setOnClickListener(v -> {
//            startActivity(new Intent(this, AddMoneyActivity.class));
//        });
//
//        instructionsButton.setOnClickListener(v -> {
//            startActivity(new Intent(this, InstructionsActivity.class));
//        });
        pickNumOfPlayers.setOnClickListener(v -> {
           startActivity(new Intent(this, PlayerSelectionActivity.class));
        });

        logoutButton.setOnClickListener(v -> {
            prefs.edit().putBoolean("isLoggedIn", false).apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void updateUI() {
        String username = prefs.getString("username", "Player");
        int balance = prefs.getInt("balance", 1000);

        welcomeText.setText("Chào mừng, " + username + "!");
        balanceText.setText("Số dư: " + balance + " VND");
    }

//    private void playBackgroundMusic() {
//        try {
//            backgroundMusic = MediaPlayer.create(this, R.raw.background_music);
//            if (backgroundMusic != null) {
//                backgroundMusic.setLooping(true);
//                backgroundMusic.start();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundMusic != null) {
            backgroundMusic.release();
            backgroundMusic = null;
        }
    }
}