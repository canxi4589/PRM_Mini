package com.example.duckrace;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements RaceTrackAdapter.OnBetPlacedListener {
    private TextView balanceText;
    private Button startButton;
    private Button resetButton;
    private Button backButton;
    private ListView raceTrackList;
    private User currentUser;
    private MediaPlayer raceSound;
    private boolean isRacing = false;
    private Random random = new Random();
    private Handler handler = new Handler();
    private List<Duck> ducks;
    private RaceTrackAdapter adapter;
    private static final int TRACK_MAX_PROGRESS = 100; // SeekBar max value
    private static final int RACE_UPDATE_INTERVAL = 100; // Update every 100ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_track);

        // Get current user from intent
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        if (currentUser == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        initializeDucks();
        setupListeners();
        updateBalance();

        // Initialize adapter
        adapter = new RaceTrackAdapter(this, ducks, currentUser, this);
        raceTrackList.setAdapter(adapter);
    }

    private void initViews() {
        balanceText = findViewById(R.id.balanceText);
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);
        backButton = findViewById(R.id.backButton);
        raceTrackList = findViewById(R.id.raceTrackList);
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
                Intent intent = new Intent();
                intent.putExtra("currentUser", currentUser);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void startRace() {
        if (isRacing) return;

        // Validate and collect all bets
        boolean hasBets = false;
        int totalBetAmount = 0;
        boolean hasInvalidBets = false;
        String errorMessage = "";

        for (int i = 0; i < ducks.size(); i++) {
            Duck duck = ducks.get(i);
            View listItem = raceTrackList.getChildAt(i);
            if (listItem != null) {
                EditText betAmountEditText = listItem.findViewById(R.id.betAmount);
                if (betAmountEditText != null) {
                    String betText = betAmountEditText.getText().toString().trim();

                    if (!betText.isEmpty()) {
                        try {
                            int betAmount = Integer.parseInt(betText);
                            if (betAmount <= 0) {
                                hasInvalidBets = true;
                                errorMessage = "Số tiền cược phải lớn hơn 0";
                                break;
                            }
                            if (betAmount < 1000) {
                                hasInvalidBets = true;
                                errorMessage = "Số tiền cược tối thiểu là 1,000 VND";
                                break;
                            }
                            duck.setBetAmount(betAmount);
                            hasBets = true;
                            totalBetAmount += betAmount;
                        } catch (NumberFormatException e) {
                            hasInvalidBets = true;
                            errorMessage = "Số tiền cược không hợp lệ";
                            break;
                        }
                    }
                }
            }
        }

        if (hasInvalidBets) {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!hasBets) {
            Toast.makeText(this, "Vui lòng đặt cược trước khi bắt đầu", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if user has enough balance for all bets
        if (totalBetAmount > currentUser.getBalance()) {
            Toast.makeText(this, "Số dư không đủ cho tổng số tiền cược", Toast.LENGTH_SHORT).show();
            return;
        }

        // Deduct total bet amount
        currentUser.setBalance(currentUser.getBalance() - totalBetAmount);
        updateBalance();

        isRacing = true;
        startButton.setEnabled(false);
        resetButton.setEnabled(false);
        backButton.setEnabled(false);

        // Start race simulation
        simulateRace();
    }

    private void simulateRace() {
        if (!isRacing) return;

        boolean raceFinished = false;
        Duck winner = null;

        for (int i = 0; i < ducks.size(); i++) {
            Duck duck = ducks.get(i);
            int currentPosition = duck.getPosition();

            // Random speed between 1-4 progress points per update
            int speed = random.nextInt(4) + 1;
            int newPosition = currentPosition + speed;

            // Check if duck finished the race
            if (newPosition >= TRACK_MAX_PROGRESS) {
                newPosition = TRACK_MAX_PROGRESS;
                if (!raceFinished) {
                    raceFinished = true;
                    winner = duck;
                }
            }

            duck.setPosition(newPosition);

            // Update SeekBar progress
            updateSeekBarProgress(i, newPosition);
        }

        if (raceFinished && winner != null) {
            finishRace(winner);
        } else {
            handler.postDelayed(this::simulateRace, RACE_UPDATE_INTERVAL);
        }
    }

    private void updateSeekBarProgress(int duckIndex, int progress) {
        View listItem = raceTrackList.getChildAt(duckIndex);
        if (listItem != null) {
            SeekBar seekBar = listItem.findViewById(R.id.duckSeekBar);
            if (seekBar != null) {
                seekBar.setProgress(progress);
            }
        }
    }

    private void finishRace(Duck winner) {
        isRacing = false;
        startButton.setEnabled(true);
        resetButton.setEnabled(true);
        backButton.setEnabled(true);

        if (raceSound != null) {
            raceSound.release();
            raceSound = null;
        }

        // Calculate winnings
        int winnerBet = winner.getBetAmount();
        int winnings = 0;
        if (winnerBet > 0) {
            winnings = winnerBet * 2; // 2x payout for correct bet
            currentUser.setBalance(currentUser.getBalance() + winnings);
            raceSound = MediaPlayer.create(this, R.raw.win);
            raceSound.start();
            updateBalance();
        }else{
            raceSound = MediaPlayer.create(this, R.raw.lose);
            raceSound.start();
        }

        // Show result
        String message = "🏆 " + winner.getName() + " thắng!";
        if (winnings > 0) {
            message += "\nBạn thắng: " + String.format("%,d VND", winnings);
        } else {
            message += "\nBạn không đặt cược cho vịt thắng cuộc.";
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // Optional: Start WinActivity if you have it
        Intent intent = new Intent(this, WinActivity.class);
        intent.putExtra("currentUser", currentUser);
        intent.putExtra("winner", winner.getName());
        intent.putExtra("winnings", winnings);
        startActivity(intent);
    }

    private void resetRace() {
        if (isRacing) return;

        // Reset duck positions
        for (Duck duck : ducks) {
            duck.setPosition(0);
            duck.setBetAmount(0);
        }

        // Reset all SeekBars to 0
        for (int i = 0; i < ducks.size(); i++) {
            updateSeekBarProgress(i, 0);
        }

        // Clear bet input fields
        for (int i = 0; i < raceTrackList.getChildCount(); i++) {
            View listItem = raceTrackList.getChildAt(i);
            if (listItem != null) {
                EditText betAmountEditText = listItem.findViewById(R.id.betAmount);
                if (betAmountEditText != null) {
                    betAmountEditText.setText("");
                }
            }
        }

        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Đã reset cuộc đua", Toast.LENGTH_SHORT).show();
    }

    private void updateBalance() {
        balanceText.setText(String.format("Số dư: %,d VND", currentUser.getBalance()));
    }

    private void initializeDucks() {
        ducks = new ArrayList<>();
        ducks.add(new Duck("Thunder", "🐎", 0, 0));
        ducks.add(new Duck("Lightning", "🐴", 0, 0));
        ducks.add(new Duck("Storm", "🦄", 0, 0));
        ducks.add(new Duck("Wind", "🐎", 0, 0));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (raceSound != null) {
            raceSound.release();
            raceSound = null;
        }
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBetPlaced(int position, int amount) {
        Duck duck = ducks.get(position);
        duck.setBetAmount(amount);
        Toast.makeText(this,
                "Đã đặt cược " + String.format("%,d", amount) + " VND cho vịt " + duck.getName(),
                Toast.LENGTH_SHORT).show();
    }
}