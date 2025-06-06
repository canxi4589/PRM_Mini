package com.example.duckrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEdit, passwordEdit;
    private Button loginButton, registerButton;
    private SharedPreferences prefs;

    public static final List<User> USERS = new ArrayList<>(Arrays.asList(
            new User("admin", "123456", 1000),
            new User("player1", "123456", 1000),
            new User("player2", "123456", 1000),
            new User("test", "123456", 1000)
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = getSharedPreferences("HorseRacingPrefs", MODE_PRIVATE);

        initViews();
        setupListeners();
    }

    private void initViews() {
        usernameEdit = findViewById(R.id.usernameEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> {
            String username = usernameEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (validateLogin(username, password)) {
                // Save login state
                prefs.edit()
                        .putBoolean("isLoggedIn", true)
                        .putString("username", username)
                        .putInt("balance", prefs.getInt("balance_" + username, getUserBalance(username)))
                        .apply();

                startActivity(new Intent(this, OptionActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private boolean validateLogin(String username, String password) {
        // Check static users
        for (User user : USERS) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }

        // Check if user was registered
        String savedPassword = prefs.getString("user_" + username, "");
        return !savedPassword.isEmpty() && savedPassword.equals(password);
    }

    private int getUserBalance(String username) {
        for (User user : USERS) {
            if (user.getUsername().equals(username)) {
                return user.getBalance();
            }
        }
        return 1000;
    }
}