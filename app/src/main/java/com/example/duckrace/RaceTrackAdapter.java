package com.example.duckrace;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import java.util.List;

public class RaceTrackAdapter extends BaseAdapter {
    private final Context context;
    private final List<Duck> ducks;
    private final User currentUser;
    private final LayoutInflater inflater;

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

        // Set animated duck thumb
        int thumbResId = getDuckAnimDrawableRes(position);
        Drawable thumbDrawable = ContextCompat.getDrawable(context, thumbResId);
        holder.duckSeekBar.setThumb(thumbDrawable);

        // Start the animation if it's an AnimationDrawable
        holder.duckSeekBar.post(() -> {
            if (thumbDrawable instanceof AnimationDrawable) {
                ((AnimationDrawable) thumbDrawable).start();
            }
        });

        // Disable user interaction with SeekBar
        holder.duckSeekBar.setEnabled(false);

        return convertView;
    }

    private int getDuckAnimDrawableRes(int position) {
        switch (position) {
            case 0:
                return R.drawable.duck1_anim;
            case 1:
                return R.drawable.duck2_anim;
            case 2:
                return R.drawable.duck3_anim;
            case 3:
                return R.drawable.duck4_anim;
            default:
                return R.drawable.duck1_anim;
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
