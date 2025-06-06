package com.example.duckrace;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.List;

public class DuckRaceAdapter extends BaseAdapter {
    private Context context;
    private List<Duck> horses;
    private LayoutInflater inflater;
    private SharedPreferences prefs;

    public DuckRaceAdapter(Context context, List<Duck> horses, SharedPreferences prefs) {
        this.context = context;
        this.horses = horses;
        this.prefs = prefs;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return horses.size();
    }

    @Override
    public Object getItem(int position) {
        return horses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.duck_race_item, parent, false);
            holder = new ViewHolder();
            holder.horseIcon = convertView.findViewById(R.id.horseIcon);
            holder.horseName = convertView.findViewById(R.id.horseName);
            holder.progressBar = convertView.findViewById(R.id.progressBar);
            holder.betAmountEdit = convertView.findViewById(R.id.betAmountEdit);
            holder.betButton = convertView.findViewById(R.id.betButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Duck horse = horses.get(position);

        holder.horseIcon.setText(horse.getIcon());
        holder.horseName.setText(horse.getName());
        holder.progressBar.setProgress(horse.getPosition());

        // Update bet amount display
        if (horse.getBetAmount() > 0) {
            holder.betAmountEdit.setText(String.valueOf(horse.getBetAmount()));
        } else {
            holder.betAmountEdit.setText("");
        }

        holder.betButton.setOnClickListener(v -> {
            String betText = holder.betAmountEdit.getText().toString().trim();
            if (betText.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập số tiền cược", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int betAmount = Integer.parseInt(betText);
                if (betAmount <= 0) {
                    Toast.makeText(context, "Số tiền cược phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (betAmount < 1000) {
                    Toast.makeText(context, "Số tiền cược tối thiểu là 1,000 VND", Toast.LENGTH_SHORT).show();
                    return;
                }

                int currentBalance = prefs.getInt("balance", 0);
                int totalCurrentBets = 0;
                for (Duck h : horses) {
                    totalCurrentBets += h.getBetAmount();
                }

                // Remove current bet for this horse from total
                totalCurrentBets -= horse.getBetAmount();

                if (totalCurrentBets + betAmount > currentBalance) {
                    Toast.makeText(context, "Số dư không đủ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                horse.setBetAmount(betAmount);
                Toast.makeText(context, "Đã đặt cược " + betAmount + " VND cho " + horse.getName(),
                        Toast.LENGTH_SHORT).show();

            } catch (NumberFormatException e) {
                Toast.makeText(context, "Số tiền cược không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView horseIcon;
        TextView horseName;
        ProgressBar progressBar;
        EditText betAmountEdit;
        Button betButton;
    }
}