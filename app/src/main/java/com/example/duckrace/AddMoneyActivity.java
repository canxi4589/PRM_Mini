package com.example.duckrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddMoneyActivity extends AppCompatActivity {
    private EditText amountEditText;
    private Button confirmButton, suggest50k, suggest100k, suggest200k, backButton;
    private SharedPreferences prefs;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        prefs = getSharedPreferences("HorseRacingPrefs", MODE_PRIVATE);
        currentUsername = prefs.getString("username", "Player");

        initViews();

        suggest50k.setOnClickListener(v -> amountEditText.setText(R.string.value_50k));
        suggest100k.setOnClickListener(v -> amountEditText.setText(R.string.value_100k));
        suggest200k.setOnClickListener(v -> amountEditText.setText(R.string.value_200k));

        confirmButton.setOnClickListener(v -> {
            String input = amountEditText.getText().toString().trim();
            if (input.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số tiền!", Toast.LENGTH_SHORT).show();
                return;
            }

            int maxAmount = 1_000_000_000; // Max 1b VNĐ
            int amount;

            try {
                amount = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số tiền không hợp lệ hoặc quá lớn!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (amount <= 0) {
                Toast.makeText(this, "Số tiền phải lớn hơn 0!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (amount > maxAmount) {
                Toast.makeText(this, "Số tiền nạp tối đa là " + maxAmount + " VND!", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Nạp " + amount + " VND thành công!", Toast.LENGTH_SHORT).show();
            int currentBalance = prefs.getInt("balance_" + currentUsername, 0);
            int newBalance = currentBalance + amount;

            if (newBalance < 0) {
                Toast.makeText(this, "Số dư vượt giới hạn cho phép!", Toast.LENGTH_SHORT).show();
                return;
            }

            prefs.edit().putInt("balance_" + currentUsername, newBalance).apply();

            Intent intent = new Intent(this, OptionActivity.class);
            startActivity(intent);
            finish();
        });

        backButton.setOnClickListener(v -> finish());
    }

    private void initViews() {
        amountEditText = findViewById(R.id.amountEditText);
        confirmButton = findViewById(R.id.confirmButton);
        suggest50k = findViewById(R.id.suggest50k);
        suggest100k = findViewById(R.id.suggest100k);
        suggest200k = findViewById(R.id.suggest200k);
        backButton = findViewById(R.id.backButton);
    }
}
