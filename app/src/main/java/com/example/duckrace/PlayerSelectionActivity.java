package com.example.duckrace;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class PlayerSelectionActivity extends AppCompatActivity {

    private RadioGroup rgPlayerCount;
    private RecyclerView rvPlayerNames;
    private MaterialButton btnContinue;
    private PlayerNameAdapter adapter;
    private List<Player> players = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);

        rgPlayerCount = findViewById(R.id.rgPlayerCount);
        rvPlayerNames = findViewById(R.id.rvPlayerNames);
        btnContinue = findViewById(R.id.btnContinue);

        // Set up RecyclerView
        rvPlayerNames.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlayerNameAdapter();
        rvPlayerNames.setAdapter(adapter);

        // Default selection is 2 players
        updatePlayerCount(2);

        // Handle radio button selection changes
        rgPlayerCount.setOnCheckedChangeListener((group, checkedId) -> {
            int playerCount = 2;
            if (checkedId == R.id.rbPlayer3) {
                playerCount = 3;
            } else if (checkedId == R.id.rbPlayer4) {
                playerCount = 4;
            }
            updatePlayerCount(playerCount);
        });

        // Handle continue button click
        btnContinue.setOnClickListener(v -> {
            if (validatePlayerNames()) {
                startBettingActivity();
            } else {
                Toast.makeText(this, "Please enter names for all players", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePlayerCount(int count) {
        players.clear();
        for (int i = 0; i < count; i++) {
            players.add(new Player("Player " + (i + 1)));
        }
        adapter.notifyDataSetChanged();
    }

    private boolean validatePlayerNames() {
        for (int i = 0; i < players.size(); i++) {
            View view = rvPlayerNames.getChildAt(i);
            if (view != null) {
                TextInputEditText etPlayerName = view.findViewById(R.id.etPlayerName);
                String name = etPlayerName.getText() != null ? etPlayerName.getText().toString() : "";
                if (name.trim().isEmpty()) {
                    return false;
                }
                players.get(i).setName(name);
            } else {
                return false;
            }
        }
        return true;
    }

    private void startBettingActivity() {
        Intent intent = new Intent(this, BettingActivity.class);
        intent.putParcelableArrayListExtra("players", new ArrayList<>(players));
        startActivity(intent);
    }

    // Adapter for player name input fields
    private class PlayerNameAdapter extends RecyclerView.Adapter<PlayerNameAdapter.PlayerNameViewHolder> {

        @NonNull
        @Override
        public PlayerNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_player_name, parent, false);
            return new PlayerNameViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull PlayerNameViewHolder holder, int position) {
            Player player = players.get(position);
            holder.etPlayerName.setHint(getString(R.string.player_name_hint, position + 1));
            holder.etPlayerName.setText(player.getName());
        }

        @Override
        public int getItemCount() {
            return players.size();
        }

        class PlayerNameViewHolder extends RecyclerView.ViewHolder {
            TextInputEditText etPlayerName;

            PlayerNameViewHolder(@NonNull View itemView) {
                super(itemView);
                etPlayerName = itemView.findViewById(R.id.etPlayerName);
            }
        }
    }
}
