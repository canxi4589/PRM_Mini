package com.example.duckrace;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddMoneyActivity extends AppCompatActivity {
    private TextView currentBalanceText;
    private Button add10k, add50k, add100k, add500k, backButton;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);

        // Get current user from intent
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        if (currentUser == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupListeners();
        updateBalance();
    }

    private void initViews() {
        currentBalanceText = findViewById(R.id.currentBalanceText);
        add10k = findViewById(R.id.add10k);
        add50k = findViewById(R.id.add50k);
        add100k = findViewById(R.id.add100k);
        add500k = findViewById(R.id.add500k);
        backButton = findViewById(R.id.backButton);
    }

    private void setupListeners() {
        add10k.setOnClickListener(v -> addMoney(10000));
        add50k.setOnClickListener(v -> addMoney(50000));
        add100k.setOnClickListener(v -> addMoney(100000));
        add500k.setOnClickListener(v -> addMoney(500000));
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, OptionActivity.class);
            intent.putExtra("currentUser", currentUser);
            startActivity(intent);
            finish();
        });
    }

    private void addMoney(int amount) {
        int newBalance = currentUser.getBalance() + amount;

        if (newBalance > 10000000) {
            Toast.makeText(this, "Số dư tối đa là 10,000,000 VND", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.setBalance(newBalance);
        updateBalance();

        Toast.makeText(this, "Đã nạp " + amount + " VND thành công!", Toast.LENGTH_SHORT).show();
    }

    private void updateBalance() {
        currentBalanceText.setText("Số dư hiện tại: " + currentUser.getBalance() + " VND");
    }
}