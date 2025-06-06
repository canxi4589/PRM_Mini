package com.example.duckrace;

import android.content.Intent;
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
    private User currentUser;
    private MediaPlayer backgroundMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_option);

        // Get current user from intent
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        if (currentUser == null) {
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
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
        });

        addMoneyButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddMoneyActivity.class);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
            finish();
        });

        pickNumOfPlayers.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlayerSelectionActivity.class);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void updateUI() {
        welcomeText.setText("Chào mừng, " + currentUser.getUsername() + "!");
        balanceText.setText("Số dư: " + currentUser.getBalance() + " VND");
    }

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