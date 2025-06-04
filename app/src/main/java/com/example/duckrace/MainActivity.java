package com.example.duckrace;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar1, seekBar2, seekBar3, seekBar4;
    private Button btnStart, btnReset;
    private RadioGroup betGroup;

    private Handler handler = new Handler();
    private boolean isRacing = false;

    private Runnable raceRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isRacing) return;

            // Randomly increase progress for each duck
            int increment1 = new Random().nextInt(5); // 0 to 4
            int increment2 = new Random().nextInt(5);
            int increment3 = new Random().nextInt(5);
            int increment4 = new Random().nextInt(5);

            seekBar1.setProgress(seekBar1.getProgress() + increment1);
            seekBar2.setProgress(seekBar2.getProgress() + increment2);
            seekBar3.setProgress(seekBar3.getProgress() + increment3);
            seekBar4.setProgress(seekBar4.getProgress() + increment4);

            // Check if any duck has finished
            if (seekBar1.getProgress() >= 100 || seekBar2.getProgress() >= 100 ||
                    seekBar3.getProgress() >= 100 || seekBar4.getProgress() >= 100) {
                isRacing = false;

                int winner = 0;
                if (seekBar1.getProgress() >= 100) winner = 1;
                else if (seekBar2.getProgress() >= 100) winner = 2;
                else if (seekBar3.getProgress() >= 100) winner = 3;
                else if (seekBar4.getProgress() >= 100) winner = 4;

                btnStart.setEnabled(true);
                Toast.makeText(MainActivity.this, "Duck " + winner + " wins!", Toast.LENGTH_LONG).show();

                // Check bet result
                int selectedId = betGroup.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton selectedButton = findViewById(selectedId);
                    if (selectedButton.getText().toString().contains(String.valueOf(winner))) {
                        Toast.makeText(MainActivity.this, "You won the bet!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "You lost the bet.", Toast.LENGTH_SHORT).show();
                    }
                }

                return;
            }

            handler.postDelayed(this, 100); // repeat every 100ms
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar1 = findViewById(R.id.seekBarDuck1);
        seekBar2 = findViewById(R.id.seekBarDuck2);
        seekBar3 = findViewById(R.id.seekBarDuck3);
        seekBar4 = findViewById(R.id.seekBarDuck4);

        btnStart = findViewById(R.id.btnStart);
        btnReset = findViewById(R.id.btnReset);
        betGroup = findViewById(R.id.betGroup);

        btnStart.setOnClickListener(v -> {
            resetRace();
            isRacing = true;
            btnStart.setEnabled(false);
            handler.post(raceRunnable);
        });

        btnReset.setOnClickListener(v -> {
            handler.removeCallbacks(raceRunnable);
            resetRace();
            btnStart.setEnabled(true);
            isRacing = false;
        });

        disableSeekbarsTouch(); // prevent user cheating
    }

    private void resetRace() {
        seekBar1.setProgress(0);
        seekBar2.setProgress(0);
        seekBar3.setProgress(0);
        seekBar4.setProgress(0);
    }

    private void disableSeekbarsTouch() {
        // Prevent user from manually moving the SeekBars
        SeekBar[] bars = {seekBar1, seekBar2, seekBar3, seekBar4};
        for (SeekBar sb : bars) {
            sb.setOnTouchListener((v, event) -> true);
        }
    }
}
