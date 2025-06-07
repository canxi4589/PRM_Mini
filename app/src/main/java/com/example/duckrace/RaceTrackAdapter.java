package com.example.duckrace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
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
            holder.duckSeekBar = convertView.findViewById(R.id.duckSeekBar);
            holder.duckName = convertView.findViewById(R.id.duckName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Duck duck = ducks.get(position);

        // Set duck name
        holder.duckName.setText(duck.getName());

        // Set SeekBar progress
        holder.duckSeekBar.setProgress(duck.getPosition());

        // Optionally, set the thumb dynamically if you have different duck images
        // holder.duckSeekBar.setThumb(context.getDrawable(getDuckImageResource(position)));

        // Disable user interaction
        holder.duckSeekBar.setEnabled(false);

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
        SeekBar duckSeekBar;
        TextView duckName;
        EditText betAmount;
    }
} 