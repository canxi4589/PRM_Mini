package com.example.duckrace;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WinActivity extends AppCompatActivity {
    private TextView resultTitle, winnerText, winningsText, balanceText;
    private Button playAgainButton, mainMenuButton;
    private MediaPlayer resultSound;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        // Get current user from intent
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        if (currentUser == null) {
            finish();
            return;
        }

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

        winnerText.setText("🏆 " + winner + " 🏆");

        if (winnings > 0) {
            resultTitle.setText("🎉 CHÚC MỪNG! 🎉");
            winningsText.setText("Bạn đã thắng: " + winnings + " VND");
            winningsText.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            resultTitle.setText("😔 THẤT BẠI 😔");
            winningsText.setText("Bạn đã thua cuộc");
            winningsText.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }

        balanceText.setText("Số dư hiện tại: " + currentUser.getBalance() + " VND");
    }

    private void setupListeners() {
        playAgainButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RaceActivity.class);
            intent.putExtra("currentUser", currentUser);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        mainMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("currentUser", currentUser);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (resultSound != null) {
            resultSound.release();
            resultSound = null;
        }
    }
}