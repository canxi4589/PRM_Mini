package com.example.duckrace;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    private EditText usernameEdit, passwordEdit;
    private Button loginButton, registerButton;
    private SharedPreferences prefs;

    // Static user data
    private static final String[] USERNAMES = {"admin", "player1", "player2", "test"};
    private static final String[] PASSWORDS = {"123456", "123456", "123456", "123456"};

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
                        .putInt("balance", prefs.getInt("balance_" + username, 1000))
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
        for (int i = 0; i < USERNAMES.length; i++) {
            if (USERNAMES[i].equals(username) && PASSWORDS[i].equals(password)) {
                return true;
            }
        }

        // Check if user was registered
        String savedPassword = prefs.getString("user_" + username, "");
        return !savedPassword.isEmpty() && savedPassword.equals(password);
    }
}