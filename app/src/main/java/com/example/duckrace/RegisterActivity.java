package com.example.duckrace;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEdit, passwordEdit, confirmPasswordEdit;
    private Button registerButton, backButton;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        prefs = getSharedPreferences("HorseRacingPrefs", MODE_PRIVATE);

        initViews();
        setupListeners();
    }

    private void initViews() {
        usernameEdit = findViewById(R.id.usernameEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        confirmPasswordEdit = findViewById(R.id.confirmPasswordEdit);
        registerButton = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.backButton);
    }

    private void setupListeners() {
        registerButton.setOnClickListener(v -> {
            String username = usernameEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();
            String confirmPassword = confirmPasswordEdit.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (username.length() < 3) {
                Toast.makeText(this, "Tên đăng nhập phải có ít nhất 3 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            for (User user : LoginActivity.USERS) {
                if (user.getUsername().equals(username)) {
                    Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (prefs.contains("user_" + username)) {
                Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }

            prefs.edit()
                    .putString("user_" + username, password)
                    .putInt("balance_" + username, 1000)
                    .apply();


            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            finish();
        });

        backButton.setOnClickListener(v -> finish());
    }
}