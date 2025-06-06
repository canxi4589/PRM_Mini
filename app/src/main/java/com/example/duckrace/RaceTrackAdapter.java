package com.example.duckrace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class RaceTrackAdapter extends BaseAdapter {
    private Context context;
    private List<Duck> ducks;
    private User currentUser;
    private LayoutInflater inflater;

    public interface OnBetPlacedListener {
        void onBetPlaced(int position, int amount);
    }

    public RaceTrackAdapter(Context context, List<Duck> ducks, User currentUser, OnBetPlacedListener listener) {
        this.context = context;
        this.ducks = ducks;
        this.currentUser = currentUser;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return ducks.size();
    }

    @Override
    public Object getItem(int position) {
        return ducks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.race_track_item, parent, false);
            holder = new ViewHolder();
            holder.duckImage = convertView.findViewById(R.id.duckImage);
            holder.duckName = convertView.findViewById(R.id.duckName);
            holder.betAmount = convertView.findViewById(R.id.betAmount);
            holder.trackContainer = convertView.findViewById(R.id.trackContainer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Duck duck = ducks.get(position);

        // Set duck image
        int duckImageRes = getDuckImageResource(position);
        holder.duckImage.setImageResource(duckImageRes);
        
        // Set duck name
        holder.duckName.setText(duck.getName());

        // Update duck position
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.duckImage.getLayoutParams();
        params.leftMargin = duck.getPosition();
        holder.duckImage.setLayoutParams(params);

        // Set bet amount if exists
        if (duck.getBetAmount() > 0) {
            holder.betAmount.setText(String.valueOf(duck.getBetAmount()));
        } else {
            holder.betAmount.setText("");
        }

        return convertView;
    }

    private int getDuckImageResource(int position) {
        switch (position) {
            case 0: return R.drawable.duck1;
            case 1: return R.drawable.duck2;
            case 2: return R.drawable.duck3;
            case 3: return R.drawable.duck4;
            default: return R.drawable.duck1;
        }
    }

    public void updateDuckPosition(int position, int newPosition, int trackWidth) {
        Duck duck = ducks.get(position);
        duck.setPosition(newPosition);
        notifyDataSetChanged();
    }

    public void resetPositions() {
        for (Duck duck : ducks) {
            duck.setPosition(0);
            duck.setBetAmount(0);
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        ImageView duckImage;
        TextView duckName;
        EditText betAmount;
        FrameLayout trackContainer;
    }
} 