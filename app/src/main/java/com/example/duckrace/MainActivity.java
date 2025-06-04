package com.example.duckrace;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    SeekBar[] seekBars = new SeekBar[4];
    Runnable[] runnables = new Runnable[4];
    Handler handler = new Handler();
    Random random = new Random();
    boolean isRacing = false;
    RadioGroup betGroup;
    Button btnStart, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBars[0] = findViewById(R.id.seekBarDuck1);
        seekBars[1] = findViewById(R.id.seekBarDuck2);
        seekBars[2] = findViewById(R.id.seekBarDuck3);
        seekBars[3] = findViewById(R.id.seekBarDuck4);

        for (SeekBar sb : seekBars) {
            sb.setThumb(getResources().getDrawable(R.drawable.duck_idle));
        }

        betGroup = findViewById(R.id.betGroup);
        btnStart = findViewById(R.id.btnStart);
        btnReset = findViewById(R.id.btnReset);

        btnStart.setOnClickListener(v -> startRace());
        btnReset.setOnClickListener(v -> resetRace());
    }

    void startRace() {
        if (isRacing) return;
        isRacing = true;

        for (int i = 0; i < 4; i++) {
            final int index = i;
            animateDuck(seekBars[i], R.drawable.duck1_walk);
            runnables[i] = new Runnable() {
                @Override
                public void run() {
                    if (seekBars[index].getProgress() >= 100) {
                        endRace(index);
                        return;
                    }
                    seekBars[index].setProgress(seekBars[index].getProgress() + random.nextInt(5));
                    handler.postDelayed(this, 100);
                }
            };
            handler.post(runnables[i]);
        }
    }

    void endRace(int winnerIndex) {
        isRacing = false;
        for (Runnable r : runnables) {
            handler.removeCallbacks(r);
        }
        for (SeekBar sb : seekBars) {
            animateDuck(sb, R.drawable.duck_idle);
        }

        int betId = betGroup.getCheckedRadioButtonId();
        int bet = -1;
        if (betId == R.id.betDuck1) bet = 0;
        else if (betId == R.id.betDuck2) bet = 1;
        else if (betId == R.id.betDuck3) bet = 2;
        else if (betId == R.id.betDuck4) bet = 3;

        if (bet == winnerIndex) {
            Toast.makeText(this, "Bạn thắng cược!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Thua rồi!", Toast.LENGTH_SHORT).show();
        }
    }

    void resetRace() {
        for (Runnable r : runnables) {
            handler.removeCallbacks(r);
        }
        for (SeekBar sb : seekBars) {
            sb.setProgress(0);
            animateDuck(sb, R.drawable.duck_idle);
        }
        isRacing = false;
    }

    void animateDuck(SeekBar seekBar, int drawableRes) {
        Drawable thumb = getResources().getDrawable(drawableRes);
        seekBar.setThumb(thumb);
        if (thumb instanceof AnimationDrawable) {
            ((AnimationDrawable) thumb).start();
        }
    }
}